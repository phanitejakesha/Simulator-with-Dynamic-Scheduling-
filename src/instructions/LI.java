package instructions;

import java.util.List;

import units.FunctionalUnitTypes;

public class LI extends DataInst{
	
	private DestinationVal dest;
	private int immediate;

	public LI(String dest, String imm) {
		this.dest = new DestinationVal(dest, 0);
		this.immediate = Integer.valueOf(imm);
		this.fType = FunctionalUnitTypes.NInteger;
	}
	
	@Override
	public void execInst() {
		this.dest.setValue(this.immediate);
		
	}

	@Override
	public String toString() {
		String result = "LI "+dest.getDestination()+","+immediate;
		return result;
	}
	
	@Override
	public DestinationVal getDestinationReg() {
		// TODO Auto-generated method stub
		return dest;
	}

	@Override
	public List<SourceVal> getSourceReg() {
		// TODO Auto-generated method stub
		return null;
	}

}
