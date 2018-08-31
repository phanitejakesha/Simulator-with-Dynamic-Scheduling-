package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class DSUBI extends Argument3N1Immediate{

	public DSUBI(String d, String s1, String imm) {
		super(d, s1, imm);
		this.fType = FunctionalUnitTypes.NInteger;
		this.iType = InstructionTypes.NArithmetic;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue()-immediate);
	}

	@Override
	public String toString() {
		String result = "DSUBI "+dest.getDestination()+","+source1.getSource()+","+immediate;
		return result;
	}
	
}
