package runme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import controllers.ClkCycle;
import controllers.ConfigManager;
import controllers.Constants;
import controllers.FromNToBinaryConvertors;
import controllers.RunManager;
import inputParsers.MemoryParser;
import inputParsers.ResultWriter;
import instructions.BEQ;
import instructions.BNE;
import instructions.DataInst;
import instructions.HLT;
import instructions.J;
import instructions.LD;
import instructions.LW;
import instructions.SD;
import instructions.SW;
import instructions.SourceVal;
import memoryBlocks.DCache;
import memoryBlocks.DCacheInfo;
import memoryBlocks.ICache;
import memoryBlocks.MemBus;
import units.FuncUnitStatus;
import units.FunctionalUnit;
import units.FunctionalUnitTypes;
import units.INTRegisters;
import units.InstructionTypes;

public class ProgramManager {

	private FunctionalUnit fu;
	private ArrayList<DataInst> read;
	private ArrayList<DataInst> fetch;
	private ArrayList<DataInst> issue;
	private ArrayList<DataInst> writeBackPhase;
	private ArrayList<DataInst> executePhase;
	private ICache iCache;
	private DCache dCache;
	private ClkCycle clock;
	private int WB_INDEX;
	private int EXEC_INDEX;
	private int READ_INDEX;
	private int ISSUE_INDEX;
	private int FETCH_INDEX;
	private ConfigManager cManager;
	private RunManager runManager;
	private INTRegisters registers;
	private int pc;

	public ProgramManager(){
		clock = new ClkCycle();
		registers = new INTRegisters();
		pc = 0;

		cManager = ConfigManager.getInstance();
		runManager = RunManager.getInstance();
		ConfigManager cm =  ConfigManager.getInstance();
		initCache(ConfigManager.getInstance());

		fu = new FunctionalUnit(cm.getFPMULTIPLIER_Units(), cm.getFPDIVIDER_Units(), cm.getFPADDER_Units());
		executePhase = new ArrayList<DataInst>();
		writeBackPhase = new ArrayList<DataInst>();
		issue = new ArrayList<DataInst>();
		fetch = new ArrayList<DataInst>();
		read = new ArrayList<DataInst>();

		WB_INDEX = 4;
		EXEC_INDEX = 3;
		READ_INDEX = 2;
		ISSUE_INDEX = 1;
		FETCH_INDEX = 0;
	}

	public void initCache(ConfigManager cm){
		MemBus bus = new MemBus();
		iCache = new ICache(cm.getICACHE_NUM_BLOCKS(), cm.getICACHE_BLOCK_SIZE(), clock, bus);
		dCache = new DCache(clock, bus);
	}

	public void executeProgram(){
		boolean isDone;
		boolean isStall = false;
		boolean isFlush = false;
		int instIssueNum = 0;
		boolean makeStall = false;
		ArrayList<DataInst> fetchingNextWord = new ArrayList<DataInst>();
		ArrayList<DataInst> completedInstructions = new ArrayList<DataInst>();
		while(true){
			isDone = true;

			ArrayList<DataInst> completeElgible = new ArrayList<DataInst>();
			ArrayList<DataInst> branchComplete = new ArrayList<DataInst>();
			ArrayList<DataInst> haltBranchComplete = new ArrayList<DataInst>();
			ArrayList<DataInst> flushedComplete = new ArrayList<DataInst>();
			if(writeBackPhase.size() != 0){
				isDone = false;
				for(int i = 0 ; i < writeBackPhase.size(); i++){
					DataInst inst = writeBackPhase.get(i);
					if(inst.endCycle[this.WB_INDEX] < clock.getCycleCount() ){
						if(inst.getDestinationReg() != null && fu.isWARHazardPresent(inst)){
							inst.WARHZ = true;
						}else{
							completeElgible.add(inst);
						}
					}
				}
			}

			if(executePhase.size() != 0){
				isDone = false;
				ArrayList<DataInst> writeElgible = new ArrayList<DataInst>();
				for(int i = 0 ; i < executePhase.size(); i++){
					DataInst inst = executePhase.get(i);
					if(inst.endCycle[this.EXEC_INDEX] < clock.getCycleCount()){
						writeElgible.add(inst);
					}
				}
				for(int i = 0 ; i < writeElgible.size(); i++){
					DataInst inst = writeElgible.get(i);
					executePhase.remove(inst);
					inst.endCycle[this.WB_INDEX] = (int)(this.clock.getCycleCount()+1-1);
					inst.endCycle[this.EXEC_INDEX] = (int)(this.clock.getCycleCount()-1);
					writeBackPhase.add(inst);
				}
			}
			
			ArrayList<DataInst> executeLDSTElgible = new ArrayList<DataInst>();
			if(read.size() != 0){
				isDone = false;
				ArrayList<DataInst> excuteElgible = new ArrayList<DataInst>();
				for(int i = 0; i < read.size(); i++){
					DataInst inst = read.get(i);
					if(inst.endCycle[this.READ_INDEX] < clock.getCycleCount()){
						List<SourceVal> sourceList = inst.getSourceReg();
						if(sourceList != null){
							boolean hasRAW = false;
							for(int j =0 ; j < sourceList.size(); j++){
								String register = sourceList.get(j).getSource();
								if( this.fu.isRAWHazardPresent(inst) == true){
									hasRAW = true;
								}
							}
							if(hasRAW == true){
								inst.RAWHZ = true;
							}else{
								excuteElgible.add(inst);	
							}
						}else{
							excuteElgible.add(inst);
						}
					}
				}
				for(int i =0; i < excuteElgible.size(); i++){
					DataInst inst = excuteElgible.get(i);
					List<SourceVal> sourceReg = inst.getSourceReg();
					if(sourceReg != null){
						if(sourceReg.size() > 0){
							SourceVal reg = sourceReg.get(0);
							String regName = reg.getSource();
							reg.setValue(registers.readFrom(regName));
						}
						if(sourceReg.size() > 1){
							SourceVal reg = sourceReg.get(1);
							String regName = reg.getSource();
							reg.setValue(registers.readFrom(regName));
						}
					}
					
					FuncUnitStatus fStatus = inst.fStatus;
					fStatus.setIssource1Ready(false);
					fStatus.setIssource2Ready(false);
					
					inst.endCycle[this.READ_INDEX] = (int) (this.clock.getCycleCount()-1);
					
					inst.execInst();
					if(inst.iType == InstructionTypes.ConditionalBranch){
						makeStall = false;
						if(inst instanceof BEQ){
							BEQ bq = (BEQ)inst;
							if(bq.getComparedResult()){
								isFlush = true;
								String label = bq.getBranchToLabel();
								pc = runManager.getAddressforLabel(label);
							}
						}else if(inst instanceof BNE){
							BNE bne = (BNE)inst;
							if(bne.getComparedResult()){
								isFlush = true;
								String label = bne.getBranchToLabel();
								pc = runManager.getAddressforLabel(label);
							}
						}
						branchComplete.add(inst);
					} 
					else if(inst.fType == FunctionalUnitTypes.FPAdder){
						inst.endCycle[this.EXEC_INDEX] = (int) (cManager.getFPADDER_Cycles()+clock.getCycleCount()-1);
						executePhase.add(inst);
					}else if(inst.fType == FunctionalUnitTypes.FPDivider){
						inst.endCycle[this.EXEC_INDEX] = (int) (cManager.getFPDIVIDER_Cycles()+clock.getCycleCount()-1);
						executePhase.add(inst);
					}else if(inst.fType == FunctionalUnitTypes.FPMultiplier){
						inst.endCycle[this.EXEC_INDEX] = (int) (cManager.getFPMULTIPLIER_Cycles()+clock.getCycleCount()-1);
						executePhase.add(inst);
					}else if(inst.fType == FunctionalUnitTypes.NInteger){
						inst.endCycle[this.EXEC_INDEX] = (int) (1 + clock.getCycleCount()-1);
						executePhase.add(inst);
					}else if(inst.fType == FunctionalUnitTypes.LoadNStoreUnit){
						executeLDSTElgible.add(inst);
						executePhase.add(inst);
					}
					read.remove(inst);

				}
			} 
			
			if(issue.size() != 0 && isStall == false){
				isDone = false;
				ArrayList<DataInst> readElgible = new ArrayList<DataInst>();
				for(int i = 0 ; i < issue.size(); i++){
					DataInst inst = issue.get(i);
					
					if(isFlush){
						issue.remove(inst);
						inst.endCycle[this.ISSUE_INDEX] = (int)(this.clock.getCycleCount()-1);
						flushedComplete.add(inst);
						continue;
					}
					
					if(inst instanceof HLT){
						haltBranchComplete.add(inst);
						continue;
					}
					
					if(inst instanceof J){
						J jumpInst = (J) inst;
						String jumpLbl = jumpInst.getJumpTo();
						haltBranchComplete.add(inst);
						pc = runManager.getAddressforLabel(jumpLbl);
						isFlush = true;
						continue;
					}
					
					if(inst.entryCycle[this.ISSUE_INDEX] < clock.getCycleCount()){
						if( fu.isAvailable(inst.fType) == false){
							inst.STRUCTHZ = true;
							continue;
						}
						if(inst.getDestinationReg() == null){
							readElgible.add(inst);
						}
						else if(fu.isWAWHazard(inst.getDestinationReg().getDestination()) == false){
							readElgible.add(inst);
						}
						else{
							inst.WAWHZ = true;
						}
					}
				}
				for(int i = 0 ; i < readElgible.size(); i++){
					DataInst inst = readElgible.get(i);
					if(inst.iType == InstructionTypes.ConditionalBranch){
						makeStall = true;
					}
					FuncUnitStatus st = fu.getFunctionalUnit(inst);
					inst.fStatus = st;
					inst.endCycle[this.READ_INDEX] = (int) (this.clock.getCycleCount()+1-1);
					inst.endCycle[this.ISSUE_INDEX] = (int) (this.clock.getCycleCount()-1);
					issue.remove(inst);
					read.add(inst);
				}
				for(int i = 0 ; i < haltBranchComplete.size(); i++){
					DataInst inst = haltBranchComplete.get(i);
					issue.remove(inst);
				}
			}
			
			if(fetch.size() == 0 && isStall == false){


				DataInst inst = runManager.getInstructioninAddress(pc);
				if(inst != null){
					int clockCycles = this.iCache.getInst(pc);
					isDone = false;
					inst.endCycle[this.FETCH_INDEX] = (int) (clock.getCycleCount()+clockCycles-1);
					pc = pc  + 1;
					inst.instIssueNum = instIssueNum;
					instIssueNum = instIssueNum +1;
					fetch.add(inst);
				}
			}else if(isStall == false){
				isDone = false;
				DataInst inst = fetch.get(0);
				if(issue.size() == 0 && inst.endCycle[this.FETCH_INDEX] < clock.getCycleCount()){
					fetch.remove(inst); 
					inst.endCycle[this.FETCH_INDEX] = (int) (clock.getCycleCount()-1);
					if(isFlush == false){
						inst.endCycle[this.ISSUE_INDEX] = (int) (clock.getCycleCount()+1-1);
						issue.add(inst);
					}else{
						isFlush = false;
						flushedComplete.add(inst);
					}
					DataInst newinst = runManager.getInstructioninAddress(pc);

					if(newinst != null){
						int clockCycles = this.iCache.getInst(pc);
						isDone = false;
						newinst.endCycle[this.FETCH_INDEX] = (int) (clock.getCycleCount()+clockCycles-1);
						newinst.instIssueNum = instIssueNum;
						instIssueNum = instIssueNum +1;
						fetch.add(newinst);
						pc = pc  + 1;
					}

				}
			}
			
			for(int i = 0 ; i < flushedComplete.size(); i++){
				DataInst inst = flushedComplete.get(i);				
				completedInstructions.add(inst);
			}
			
			for(int i = 0 ; i < branchComplete.size(); i++){
				DataInst inst = branchComplete.get(i);
				inst.endCycle[this.READ_INDEX] = (int)(clock.getCycleCount()-1);
				FuncUnitStatus st = inst.fStatus;
				fu.writeToFunctionalStatus(inst);
				
				completedInstructions.add(inst);

			}
			
			for(int i = 0 ; i < haltBranchComplete.size(); i++){
				DataInst inst = haltBranchComplete.get(i);
				inst.endCycle[this.ISSUE_INDEX] = (int) (clock.getCycleCount()-1);
				issue.remove(inst);
				
				completedInstructions.add(inst);
			} 
			
			fetchingNextWord = new ArrayList<DataInst>();
			for(int i = 0 ; i < executeLDSTElgible.size(); i++){
				DataInst inst = executeLDSTElgible.get(i);
				if(inst instanceof LD){
					long addressvalue = inst.addressValue;
					DCacheInfo info = dCache.getData(addressvalue);
					inst.endCycle[this.EXEC_INDEX] = (int) (clock.getCycleCount()+info.getClockCycles() -1);
					int value = FromNToBinaryConvertors.toInt(info.getData());
					fetchingNextWord.add(inst);
					inst.getDestinationReg().setValue(value);
				}else if(inst instanceof SD){
					long addressvalue = inst.addressValue;
					int numCycles = dCache.updateValue(addressvalue,"10000000100000001000000010000000");
					inst.endCycle[this.EXEC_INDEX] = (int) (clock.getCycleCount()+numCycles -1);
					fetchingNextWord.add(inst);
				}else if(inst instanceof LW){
					long addressvalue = inst.addressValue;
					DCacheInfo info = dCache.getData(addressvalue);
					inst.endCycle[this.EXEC_INDEX] = (int) (clock.getCycleCount()+info.getClockCycles() -1);
					int value = FromNToBinaryConvertors.toInt(info.getData());
					inst.getDestinationReg().setValue(value);
				}else if(inst instanceof SW){
					long addressvalue = inst.addressValue;
					List<SourceVal> srcReg = inst.getSourceReg();
					String defaultValue = "10000000";
					if(srcReg != null){
						int val = srcReg.get(0).getValue();
						defaultValue = FromNToBinaryConvertors.toBinary(val);
					}
					int numCycles = dCache.updateValue(addressvalue, defaultValue);
					inst.endCycle[this.EXEC_INDEX] = (int) (clock.getCycleCount()+numCycles -1);
				}
			}
			
			for(int i = 0 ; i < completeElgible.size(); i++){
				DataInst inst = completeElgible.get(i);
				writeBackPhase.remove(inst);
				if(inst.getDestinationReg() != null){
					String regName = inst.getDestinationReg().getDestination();
					int val = inst.getDestinationReg().getValue();
					registers.writeTo(regName, val);
				}
				
				completedInstructions.add(inst);
				FuncUnitStatus st = inst.fStatus;
				fu.writeToFunctionalStatus(inst);
			}
			if(isDone == true){
				dCache.updateDataToMemory();
				MemoryParser.getInstance().pushMemDataToFile();
				StoretoFile(completedInstructions);
				break;
			}
			if(makeStall == true){
				isStall = true;
			}
			if(makeStall == false){
				isStall = false;
			}
			clock.tick();
			for(int i = 0 ; i < fetchingNextWord.size();i++){
				DataInst inst = fetchingNextWord.get(i);
				if(inst instanceof LD){
					long addressvalue = inst.addressValue;
					DCacheInfo info = dCache.getData(addressvalue+4);
					inst.endCycle[this.EXEC_INDEX] = inst.endCycle[this.EXEC_INDEX] +info.getClockCycles();
					int value = FromNToBinaryConvertors.toInt(info.getData());
					inst.getDestinationReg().setValue(value);
				}else if(inst instanceof SD){
					long addressvalue = inst.addressValue;
					int numCycles2 = dCache.updateValue(addressvalue+4,"10000000100000001000000010000000");
					inst.endCycle[this.EXEC_INDEX] = inst.endCycle[this.EXEC_INDEX] +numCycles2;
				}
			}
		}
	}
	
	class IssueComparator implements Comparator<DataInst>{

		@Override
		public int compare(DataInst arg0, DataInst arg1) {
			if(arg0.instIssueNum < arg1.instIssueNum){
				return -1;
			}
			return 1;
		}
	}
	
	public void StoretoFile(ArrayList<DataInst> totalInstructions){
		
		StringBuilder sb = new StringBuilder();
		Collections.sort(totalInstructions, new IssueComparator());		
		String Header = String.format(Constants.outputPrintFormat,Constants.headerarr);
		System.out.println(Header);
		sb.append(Header);
		sb.append(System.lineSeparator());
		for(int i = 0 ; i < totalInstructions.size(); i++){
			DataInst inst = totalInstructions.get(i);
			if(i==totalInstructions.size()-1 && inst.iType.equals(InstructionTypes.HALT)){
				inst.endCycle[1] = 0;
			}
			System.out.println(inst.getoutputstr());
			sb.append(inst.getoutputstr());
			sb.append(System.lineSeparator());
		}
		System.out.println(this.iCache.getICacheStatistics());
		System.out.println(this.dCache.getDCacheStatistics());
		sb.append(this.iCache.getICacheStatistics());
		sb.append(System.lineSeparator());
		sb.append(this.dCache.getDCacheStatistics());
		sb.append(System.lineSeparator());
		
		ResultWriter.getInstance().writeToFile(Constants.resultFilePath, sb.toString());
	}
}