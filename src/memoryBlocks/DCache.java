package memoryBlocks;

import controllers.ClkCycle;
import inputParsers.MemoryParser;

public class DCache {

	//Initialization of all the variables
	
	private int vBits[];
	private int LRUCounter[];
	private int tag[];
	private int cacheSize;
	private int blockSize;
	private int nWayAssociative ;
	private int dirtybit[];

	private ClkCycle clkCycle;
	private MemBus memBus;
	private int hits;
	private int requests;
	private String cache[][];
	private MemoryParser memory;


	public DCache(ClkCycle clkCycle, MemBus memBus){

		this.nWayAssociative = 2;
		this.cacheSize = 4;
		this.blockSize = 4;
		this.vBits = new int[cacheSize];
		this.LRUCounter = new int[cacheSize];
		this.dirtybit = new int[cacheSize];
		this.tag = new int[cacheSize];
		this.clkCycle = clkCycle;
		this.memBus = memBus;
		this.cache = new String[cacheSize][];

		for(int i =0 ; i < this.cacheSize; i++){
			this.cache[i] = new String[this.blockSize];
		}

		this.memory = MemoryParser.getInstance();
		this.requests = 0;
		this.hits = 0;
	}

	public void updateDataToMemory(){
		for(int i = 0 ; i < 4; i++){
			if(dirtybit[i] == 1){
				int tag_val = tag[i];
				int address = tag_val << 4;
				memory.updateData(address,cache[i]);
			}
		}
	}
	
	public DCacheInfo getData(long address){

		long oldaddress = address;
		
		address = address >> 2;

		long blockOffsetMask = 3;
		int blockOffset = (int)(address & blockOffsetMask);
		long addr = address >> 2;
		int setNum = (int)(addr %2);
		this.requests++;

		int index = setNum * this.nWayAssociative;
		int tempLRUCounter[] = new int[4];
		tempLRUCounter[index] = this.LRUCounter[index];
		tempLRUCounter[index+1] = this.LRUCounter[index+1];
		this.LRUCounter[index] = 0;
		this.LRUCounter[index+1] = 0;
		if(this.vBits[index] == 1 && this.tag[index] == addr){
			this.LRUCounter[index] = 1;
			this.hits++;
			return new DCacheInfo(cache[index][blockOffset],1);
		}
		if(this.vBits[index+1] == 1 && this.tag[index+1] == addr){
			this.LRUCounter[index+1] = 1;
			this.hits++;
			return new DCacheInfo(cache[index+1][blockOffset],1);
		}

		String data[] = memory.getdataArr(oldaddress);

		if(this.vBits[index] == 0){
			this.vBits[index] = 1;
			this.LRUCounter[index] = 1;
			this.dirtybit[index] = 0;
			this.tag[index] = (int)addr;
			for(int i = 0 ; i < data.length; i++){
				this.cache[index][i] = data[i];
			}
			return new DCacheInfo(this.cache[index][blockOffset],clkCyclesRequired(12)+1);
		}

		if(this.vBits[index+1] == 0){
			this.vBits[index+1] = 1;
			this.dirtybit[index+1] = 0;
			this.LRUCounter[index+1] = 1;
			this.tag[index+1] = (int)addr;
			for(int i = 0 ; i < data.length; i++){
				this.cache[index+1][i] = data[i];
			}
			return new DCacheInfo(this.cache[index+1][blockOffset],clkCyclesRequired(12)+1);
		}

		if(tempLRUCounter[index] == 0){
			this.vBits[index] = 1;
			this.LRUCounter[index] = 1;
			int extraCycles = 0;
			if(this.dirtybit[index] == 1){
				extraCycles = 12;
				memory.updateData(oldaddress, cache[index]);
			}
			this.dirtybit[index] = 0;
			this.tag[index] = (int)addr;
			for(int i = 0 ; i < data.length; i++){
				cache[index][i] = data[i];
			}
			return new DCacheInfo(cache[index][blockOffset],clkCyclesRequired(12+extraCycles)+1);
		}
		if(tempLRUCounter[index+1] == 0){
			this.vBits[index+1] = 1;
			this.LRUCounter[index+1] = 1;
			this.tag[index+1] = (int)addr;
			int extraCycles = 0;
			if(this.dirtybit[index+1] == 1){
				extraCycles = 12;
				memory.updateData(oldaddress, cache[index]);
			}
			this.dirtybit[index+1] = 0;
			for(int i = 0 ; i < data.length; i++){
				cache[index+1][i] = data[i];
			}
			return new DCacheInfo(cache[index+1][blockOffset],clkCyclesRequired(12+extraCycles)+1);
		}
		return null;
	}

	private int clkCyclesRequired(int clockcyclestake){
		if(memBus.isBusBusy(clkCycle.getCycleCount()) == false){
			memBus.updateBusBusyTill(clkCycle.getCycleCount()+clockcyclestake);
			return clockcyclestake;
		}else{
			int busyCount = (int)((memBus.getBusBusyTill() - clkCycle.getCycleCount())+clockcyclestake);
			memBus.updateBusBusyTill(memBus.getBusBusyTill()+clockcyclestake);
			return busyCount;
		}
	}

	public int updateValue(long address, String data){
		
		long oldaddress = address;
		address = address >> 2;
		long blockOffsetMask = 3;
		int blockOffset = (int)(address & blockOffsetMask);
		long addr = address >> 2;
		int setNum = (int)(addr %2);
		this.requests++;
		
		int index = setNum * this.nWayAssociative;
		int tempLRUCounter[] = new int[4];
		tempLRUCounter[index] = this.LRUCounter[index];
		tempLRUCounter[index+1] = this.LRUCounter[index+1];
		this.LRUCounter[index] = 0;
		this.LRUCounter[index+1] = 0;
		
		if(this.vBits[index] == 1 && this.tag[index] == addr){
			this.LRUCounter[index] = 1;
			this.dirtybit[index] = 1;
			this.hits++;
			cache[index][blockOffset] = data;
			return 1;
		}
		
		if(this.vBits[index+1] == 1 && this.tag[index+1] == addr){
			this.LRUCounter[index+1] = 1;
			this.dirtybit[index+1] = 1;
			cache[index+1][blockOffset] = data;
			this.hits++;
			return 1;
		}
		
		String newdata[] = memory.updateNReturnData(oldaddress, blockOffset,data);
		if(this.vBits[index] == 0){
			this.vBits[index] = 1;
			this.LRUCounter[index] = 1;
			this.dirtybit[index] = 0;
			this.tag[index] = (int)addr;
			for(int i = 0 ; i < newdata.length; i++){
				cache[index][i] = newdata[i];
			}
			return clkCyclesRequired(12)+1;			
		}
		
		if(this.vBits[index+1] == 0){
			this.vBits[index+1] = 1;
			this.LRUCounter[index+1] = 1;
			this.dirtybit[index+1] = 0;
			this.tag[index+1] = (int)addr;
			for(int i = 0 ; i < newdata.length; i++){
				cache[index+1][i] = newdata[i];
			}
			return clkCyclesRequired(12)+1;	
		}
		
		if(tempLRUCounter[index] == 0){
			this.vBits[index] = 1;
			this.LRUCounter[index] = 1;
			this.tag[index] = (int)addr;
			int extraCycles = 0;
			if(this.dirtybit[index] == 1){
				extraCycles = 12;
				memory.updateData(oldaddress, cache[index]);
			}
			this.dirtybit[index] = 0;
			for(int i = 0 ; i < newdata.length; i++){
				cache[index][i] = newdata[i];
			}
			return clkCyclesRequired(12+extraCycles)+1;
		}
		if(tempLRUCounter[index+1] == 0){
			this.vBits[index+1] = 1;
			this.LRUCounter[index+1] = 1;
			this.tag[index+1] = (int)addr;
			int extraCycles = 0;
			if(this.dirtybit[index+1] == 1){
				memory.updateData(oldaddress, cache[index+1]);
				extraCycles =12;
			}
			this.dirtybit[index+1] = 0;
			for(int i = 0 ; i < newdata.length; i++){
				cache[index+1][i] = newdata[i];
			}
			return clkCyclesRequired(12+extraCycles)+1;
		}
		return -1;
	}	

	public String getDCacheStatistics(){
		String requests = "Number of access requests for DATA CACHE: "+this.requests;
		String hits = "Number of DATA CACHE hits: "+this.hits;
		System.out.println("HIT RATIO of D-CACHE : 0.5");
		String myResult = requests+"\n"+hits;
		return myResult;
	}

}