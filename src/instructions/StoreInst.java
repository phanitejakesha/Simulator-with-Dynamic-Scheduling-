package instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class StoreInst extends DataInst{
	public SourceVal source1;
	public SourceVal source2;
	public int immediate;
	
	public StoreInst(String s1, String s2, String imm){
		this.source1 = new SourceVal(s1, 0);
		this.source2 = new SourceVal(s2, 0);
		this.immediate = Integer.valueOf(imm);
	}
	
	@Override
	public DestinationVal getDestinationReg() {
		return null;
		
	}
	
	@Override
	public List<SourceVal> getSourceReg() {
		List<SourceVal> sourcelist = new ArrayList<SourceVal>();
		sourcelist.add(this.source1);
		sourcelist.add(this.source2);
		return sourcelist;
	}

	

}
