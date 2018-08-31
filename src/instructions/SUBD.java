package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class SUBD extends Argument3{

	public SUBD(String des, String source1, String source2) {
		super(des, source1, source2);
		this.fType = FunctionalUnitTypes.FPAdder;
		this.iType = InstructionTypes.FADDNSUB;
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue()-source2.getValue());
		
	}
	
	@Override
	public String toString() {
		String result = "SUB.D "+dest.getDestination()+","+source1.getSource()+","+source2.getSource();
		return result;
	}

}
