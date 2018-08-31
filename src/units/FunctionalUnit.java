package units;

import java.util.ArrayList;
import java.util.List;

import instructions.DataInst;
import instructions.SourceVal;

public class FunctionalUnit {

	private ArrayList<FuncUnitStatus> fp;
	private int FPDIVIDER_Units;
	private int FPADDER_Units;
	private int FPMULTIPLIER_Units;
	
	public FunctionalUnit(int multipliers, int dividers, int adders){
		
		fp = new ArrayList<FuncUnitStatus>();
		this.FPADDER_Units = adders;
		this.FPDIVIDER_Units = dividers;
		this.FPMULTIPLIER_Units = multipliers;
		for(int i = 0 ; i < this.FPMULTIPLIER_Units; i++){
			fp.add(new FuncUnitStatus(FunctionalUnitTypes.FPMultiplier));
		}
		for(int i = 0; i < this.FPADDER_Units; i++){
			fp.add(new FuncUnitStatus(FunctionalUnitTypes.FPAdder));
		}
		for(int i = 0; i < this.FPDIVIDER_Units; i++){
			fp.add(new FuncUnitStatus(FunctionalUnitTypes.FPDivider));
		}
		fp.add(new FuncUnitStatus(FunctionalUnitTypes.NInteger));
		fp.add(new FuncUnitStatus(FunctionalUnitTypes.LoadNStoreUnit));
		fp.add(new FuncUnitStatus(FunctionalUnitTypes.BRANCH));
	}
	
	public boolean isAvailable(FunctionalUnitTypes fType){
		for(int i = 0 ; i < fp.size(); i++){
			if(fp.get(i).getfType() == fType && fp.get(i).isStatus() == false){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isWAWHazard(String register){
		for(int i = 0 ; i < fp.size(); i++){
			if(fp.get(i).isStatus() == true && fp.get(i).getDestReg().equals(register)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRAWHazardPresent(DataInst inst){
		FuncUnitStatus fStatus = inst.fStatus;
		if(fStatus.isIssource1Ready() == true && fStatus.isIssource2Ready() == true){
			return false;
		}
		return true;
	}
	
	public boolean isWARHazardPresent(DataInst inst){
	    if( inst.getDestinationReg() == null){
	    	return false;
	    }
	    String destReg = inst.getDestinationReg().getDestination();
		for(int i = 0 ; i < fp.size(); i++){
			FuncUnitStatus fStatus = inst.fStatus;
			if(fStatus.isStatus()){
				if(destReg.equals(fStatus.getSourceReg1()) && fStatus.isIssource1Ready() == true){
					return true;
				}
				if(destReg.equals(fStatus.getSourceReg2()) && fStatus.isIssource2Ready() == true){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isReady(String register){
		for(int i = 0; i < fp.size(); i++){
			FuncUnitStatus fStatus = fp.get(i);
			if(fStatus.isStatus() == true && fStatus.getDestReg().equals(register)){
				return false;
			}
		}
		return true;
	}
	
	public FuncUnitStatus getFunctionalUnit(DataInst ist){
		
		String destRegister = "";
		String srcRegister1  = "";
		String srcRegister2 = "";
		boolean src1Ready = true;
		boolean src2Ready = true;
		if(ist.getDestinationReg() != null){
			destRegister = ist.getDestinationReg().getDestination();
		}
		List<SourceVal> srcReg = ist.getSourceReg();
		if(srcReg != null && srcReg.size() > 0){
			srcRegister1 = ist.getSourceReg().get(0).getSource();
			src1Ready = isReady(srcRegister1);
		}
		if(srcReg != null &&srcReg.size() > 1){
			srcRegister2 = ist.getSourceReg().get(1).getSource();
			src2Ready = isReady(srcRegister2);
		}
		for(int i = 0; i < fp.size(); i++){
			if(fp.get(i).isStatus() == false && ist.fType == fp.get(i).getfType()){
				FuncUnitStatus status = fp.get(i);
				status.setfType(ist.fType);
				status.setIssource1Ready(src1Ready);
				status.setIssource2Ready(src2Ready);
				status.setSourceReg1(srcRegister1);
				status.setSourceReg2(srcRegister2);
				status.setDestReg(destRegister);
				status.setStatus(true);
				return status;
			}
		}
		return null;
	}
	
	public void writeToFunctionalStatus(DataInst inst){
		FuncUnitStatus fStatus = inst.fStatus;
		if(inst.getDestinationReg() == null){	
			fStatus.clear();
			return;
		}
		String destRegister = inst.getDestinationReg().getDestination();
		for(int i = 0 ; i < fp.size(); i++){
			FuncUnitStatus myStatus = fp.get(i);
			if(myStatus.getSourceReg1().equals(destRegister)){
				myStatus.setIssource1Ready(true);
			}
			if(myStatus.getSourceReg2().equals(destRegister)){
				myStatus.setIssource2Ready(true);
			}
		}
		fStatus.clear();
	}
}
