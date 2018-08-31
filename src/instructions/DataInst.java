package instructions;

import controllers.Constants;
import units.FuncUnitStatus;
import units.FunctionalUnitTypes;
import units.InstructionTypes;

public abstract class DataInst implements Inst{

	public boolean RAWHZ;
	public boolean WARHZ;
	public boolean WAWHZ;
	public boolean STRUCTHZ;
	public int[] entryCycle;
	public int[] endCycle;
	public FunctionalUnitTypes fType;
	public InstructionTypes iType;
	public int instIssueNum;
	public long addressValue;
	public FuncUnitStatus fStatus;
	private String instStr = "";
	private String labelStr = "";
	
	public  DataInst(){
		this.RAWHZ = false;
		this.WARHZ = false;
		this.WAWHZ = false;
		this.STRUCTHZ = false;
		this.entryCycle = new int[5];
		this.endCycle = new int[5];
		
		this.fType = FunctionalUnitTypes.UNKNOWN;
		this.iType = InstructionTypes.UNKNOWN;
		
	}

	public void setLabeltoOutputStr(String str){
		this.labelStr   = str;
	}
	
	public void setInstOutptuStr(String str){
		this.instStr = str;
	}
	
	public String getoutputstr(){
		
		String[] testarr = new String[10];
		testarr[0] = this.labelStr;
		testarr[1] = this.instStr;
		//StringBuilder sb = new StringBuilder();
		
		//writing the end cycle values
		for(int index=0; index<this.endCycle.length;index++){
			if(this.endCycle[index]!=0){
				testarr[index+2] = this.endCycle[index]+"";
			}else{
				testarr[index+2] = "";
			}
		}
		
		if(this.RAWHZ){
			testarr[7] = "Y";
		}else{
			testarr[7] = "N";
		}
		
//		if(this.WARHZ){
//			sb.append("Y\t");
//		}else{
//			sb.append("N\t");
//		}
		
		if(this.WAWHZ){
			testarr[8] = "Y";
		}else{
			testarr[8] = "N";
		}
		
		if(this.STRUCTHZ){
			testarr[9] = "Y";
		}else{
			testarr[9] = "N";
		}
		
		return String.format(Constants.outputPrintFormat,testarr);
	}
	
}
