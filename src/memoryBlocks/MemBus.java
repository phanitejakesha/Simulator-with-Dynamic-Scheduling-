package memoryBlocks;

public class MemBus {

	private long memBusIsBusyTill;
	
	public MemBus(){
		this.memBusIsBusyTill = 0;
	}

	public boolean isBusBusy(long clkCycleCount){
		if(this.memBusIsBusyTill > clkCycleCount){
			return true;
		}
		return false;
	}

	public void updateBusBusyTill(long clkCycleCount){
		this.memBusIsBusyTill = clkCycleCount;
	}
	
	public long getBusBusyTill(){
		return this.memBusIsBusyTill;
	}
}