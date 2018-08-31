package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class ADDD extends Argument3{
	
	public ADDD(String destination, String source1, String source2){
		super(destination,source1,source2);
		this.fType = FunctionalUnitTypes.FPAdder;
		this.iType = InstructionTypes.FADDNSUB;
		
	}

	@Override
	public void execInst() {
		dest.setValue(source1.getValue()+source2.getValue());
		
	}
	
	@Override
	public String toString() {
		String result = "ADD.D "+dest.getDestination()+","+source1.getSource()+","+source2.getSource();
		return result;
	}

}
