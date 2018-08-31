package instructions;

import java.util.List;

public class J extends DataInst{
	
	String jumpTo;

	public J(String destLabel) {
		this.jumpTo = destLabel;
	}
	
	
	public String getJumpTo() {
		return jumpTo;
	}


	@Override
	public String toString() {
		return "J "+this.jumpTo;
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

}
