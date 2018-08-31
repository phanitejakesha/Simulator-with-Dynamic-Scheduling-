package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class LW extends Argument3N1Immediate{

	public LW(String d, String s1, String imm) {
		super(d, s1, imm);
		this.iType = InstructionTypes.LSW;
		this.fType = FunctionalUnitTypes.LoadNStoreUnit;
	}

	@Override
	public void execInst() {
		this.addressValue = immediate+source1.getValue();
	}

	@Override
	public String toString() {
		String result = "LW "+dest.getDestination()+","+immediate+"("+source1.getSource()+")";
		return result;
	}
}
