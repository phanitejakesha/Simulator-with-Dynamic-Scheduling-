package instructions;

import java.util.List;

public interface Inst {
	
	public void execInst();
	public DestinationVal getDestinationReg();
	public List<SourceVal> getSourceReg();

}
