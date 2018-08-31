package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class DIVD extends Argument3{

	public DIVD(String d, String s1, String s2) {
		super(d, s1, s2);
		this.iType = InstructionTypes.FDIV;
		this.fType = FunctionalUnitTypes.FPDivider;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue()/source2.getValue());
		
	}
	
	@Override
	public String toString() {
		String result = "DIV.D "+dest.getDestination()+","+source1.getSource()+","+source2.getSource();
		return result;
	}

}
