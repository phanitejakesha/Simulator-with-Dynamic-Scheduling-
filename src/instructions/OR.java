package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class OR extends Argument3{

	public OR(String d, String s1, String s2) {
		super(d, s1, s2);
		this.iType = InstructionTypes.NArithmetic;
		this.fType = FunctionalUnitTypes.NInteger;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue() | source2.getValue());		
	}
	
	@Override
	public String toString() {
		String result = "OR "+dest.getDestination()+","+source1.getSource()+","+source2.getSource();
		return result;
	}

}
