package instructions;

import java.util.List;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class HLT extends DataInst{
	
	public HLT() {
		this.iType = InstructionTypes.HALT;
		this.fType = FunctionalUnitTypes.UNKNOWN; 
	}

	@Override
	public void execInst() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DestinationVal getDestinationReg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SourceVal> getSourceReg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "HLT";
	}
}
