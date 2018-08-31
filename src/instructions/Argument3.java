package instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class Argument3 extends DataInst{
	
	public SourceVal source1 = null;
	public SourceVal source2 = null;
	public DestinationVal dest = null;
	
	public Argument3 (String d, String s1, String s2){
		this.source1 = new SourceVal(s1,0);
		this.source2 = new SourceVal(s2,0);
		this.dest = new DestinationVal(d,0);
	}

	@Override
	public DestinationVal getDestinationReg() {
		return this.dest;
		
	}

	@Override
	public List<SourceVal> getSourceReg() {
		List<SourceVal> sourcelist = new ArrayList<SourceVal>();
		sourcelist.add(this.source1);
		sourcelist.add(this.source2);
		return sourcelist;
	}

}
