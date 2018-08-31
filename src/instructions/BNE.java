package instructions;

import java.util.ArrayList;
import java.util.List;

import units.FunctionalUnitTypes;
import units.InstructionTypes;

public class BNE extends DataInst{

	private SourceVal source1;
	private SourceVal source2;
	private String branchToLabel;
	
	public BNE(String s1, String s2, String label) {
		this.source1 = new SourceVal(s1, 0);
		this.source2 = new SourceVal(s2, 0);
		this.branchToLabel = label;
		this.iType = InstructionTypes.ConditionalBranch;
		this.fType = FunctionalUnitTypes.BRANCH;
	}
	
	public boolean getComparedResult(){
		return (source1.getValue() != source2.getValue());
	}
	
	public String getBranchToLabel(){
		return this.branchToLabel;
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
		List<SourceVal> sourcelist = new ArrayList<SourceVal>();
		sourcelist.add(this.source1);
		sourcelist.add(this.source2);
		return sourcelist;
	}
	
	@Override
	public String toString() {
		String result = "BNE " + " " + source1.getSource() + ", "+ source2.getSource() + ", " + branchToLabel;
		return result;
	}

}