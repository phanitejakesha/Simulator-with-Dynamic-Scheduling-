package instructions;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class SW extends StoreInst{

	public SW(String s1, String s2, String imm) {
		super(s1, s2, imm);
		this.iType = InstructionTypes.LSW;
		this.fType = FunctionalUnitTypes.LoadNStoreUnit;
	}
	
	@Override
	public void execInst() {
		this.addressValue = this.immediate+ this.source2.getValue();
	}
	
	public SourceVal getValuetowriteToMemory(){
		return this.source1;
	}

	@Override
	public String toString() {
		String result = "SW "+source1.getSource()+","+immediate+"("+source2.getSource()+")";
		return result;
	}
}
