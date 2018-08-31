package instructions;

import java.util.List;

import units.FunctionalUnitTypes;

public class LUI extends DataInst{

	private DestinationVal dest;
	private int immediate;

	public LUI(String dest, String imm) {
		this.dest = new DestinationVal(dest, 0);
		this.immediate = Integer.valueOf(imm);
		this.fType = FunctionalUnitTypes.NInteger;
	}
	
	@Override
	public void execInst() {
		this.dest.setValue((int)(Math.pow(2, 16)*this.immediate));
	}

	@Override
	public DestinationVal getDestinationReg() {
		return dest;
	}

	@Override
	public List<SourceVal> getSourceReg() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		String result = "LUI "+dest.getDestination()+","+immediate;
		return result;
	}

}
