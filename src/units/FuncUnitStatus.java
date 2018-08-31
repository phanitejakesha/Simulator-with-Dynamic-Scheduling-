package units;

public class FuncUnitStatus {

	private FunctionalUnitTypes fType;
	private String sourceReg1;
	private String sourceReg2;
	private String destReg;
	private boolean status;
	private boolean issource1Ready;
	private boolean issource2Ready;
	
	public FuncUnitStatus(FunctionalUnitTypes ftype){
		this.fType = ftype;
		this.sourceReg1 = "";
		this.sourceReg2= "";
		this.destReg = "";
		this.status = false;
		this.issource1Ready = false;
		this.issource2Ready = false;
	}
	
	public void clear(){
		this.sourceReg1 = "";
		this.sourceReg2 = "";
		this.issource1Ready = false;
		this.issource2Ready = false;
		this.destReg = "";
		this.status = false;
	}

	public FunctionalUnitTypes getfType() {
		return fType;
	}

	public String getSourceReg1() {
		return sourceReg1;
	}

	public String getSourceReg2() {
		return sourceReg2;
	}

	public String getDestReg() {
		return destReg;
	}

	public boolean isStatus() {
		return status;
	}

	public boolean isIssource1Ready() {
		return issource1Ready;
	}

	public boolean isIssource2Ready() {
		return issource2Ready;
	}

	public void setfType(FunctionalUnitTypes fType) {
		this.fType = fType;
	}

	public void setSourceReg1(String sourceReg1) {
		this.sourceReg1 = sourceReg1;
	}

	public void setSourceReg2(String sourceReg2) {
		this.sourceReg2 = sourceReg2;
	}

	public void setDestReg(String destReg) {
		this.destReg = destReg;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setIssource1Ready(boolean issource1Ready) {
		this.issource1Ready = issource1Ready;
	}

	public void setIssource2Ready(boolean issource2Ready) {
		this.issource2Ready = issource2Ready;
	}
	
	
	
}
