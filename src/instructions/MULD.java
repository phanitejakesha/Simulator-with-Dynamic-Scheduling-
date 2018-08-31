package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class MULD extends Argument3{

	public MULD(String d, String s1, String s2) {
		super(d, s1, s2);
		this.iType = InstructionTypes.FMUL;
		this.fType = FunctionalUnitTypes.FPMultiplier;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue()*source2.getValue());
	}
	
	@Override
	public String toString() {
		String result = "MUL.D "+dest.getDestination()+","+source1.getSource()+","+source2.getSource();
		return result;
	}

}
