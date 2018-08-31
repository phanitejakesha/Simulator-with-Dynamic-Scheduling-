package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class ANDI extends Argument3N1Immediate{

	public ANDI(String d, String s1, String imm) {
		super(d, s1, imm);
		this.iType = InstructionTypes.NArithmetic;
		this.fType = FunctionalUnitTypes.NInteger;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue() & immediate);
	}
	
	@Override
	public String toString() {
		String result = "ANDI "+dest.getDestination()+","+source1.getSource()+","+immediate;
		return result;
	}

}
