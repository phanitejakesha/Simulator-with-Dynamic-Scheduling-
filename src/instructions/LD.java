package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class LD extends Argument3N1Immediate{

	public LD(String d, String s1, String imm) {
		super(d, s1, imm);
		this.iType = InstructionTypes.LSD;
		this.fType = FunctionalUnitTypes.LoadNStoreUnit;
	}

	@Override
	public void execInst() {
		this.addressValue = source1.getValue()+immediate;
	}

	@Override
	public String toString() {
		String result = "LD "+dest.getDestination()+","+immediate+"("+source1.getSource()+")";
		return result;
	}
}
