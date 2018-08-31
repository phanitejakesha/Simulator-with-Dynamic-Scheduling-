package memoryBlocks;

public class DCacheInfo {

	private String data;
	private int clockCycles;
	
	public DCacheInfo(String data, int clockCycles){
		this.data = data;
		this.clockCycles = clockCycles;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getClockCycles() {
		return clockCycles;
	}

	public void setClockCycles(int clockCycles) {
		this.clockCycles = clockCycles;
	}
	
	
}
