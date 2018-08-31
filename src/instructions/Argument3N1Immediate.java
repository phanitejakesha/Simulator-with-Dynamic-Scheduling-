package instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class Argument3N1Immediate extends DataInst{
	
	public SourceVal source1 = null;
	public int immediate ;
	public DestinationVal dest = null;

	public Argument3N1Immediate(String d, String s1, String imm) {
		this.dest = new DestinationVal(d, 0);
		this.source1 = new SourceVal(s1, 0);
		this.immediate = Integer.valueOf(imm);
	}
	
	@Override
	public DestinationVal getDestinationReg() {
		return this.dest;
	}

	@Override
	public List<SourceVal> getSourceReg() {
		List<SourceVal> sourcelist = new ArrayList<SourceVal>();
		sourcelist.add(this.source1);
		return sourcelist;
	}

}
