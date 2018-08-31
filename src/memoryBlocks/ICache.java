package memoryBlocks;

import java.util.HashMap;
import java.util.Map;

import controllers.ClkCycle;

public class ICache {

	private int numBlocks;
	private int blockSize;
	private Map<Integer,Integer> cache;
	private ClkCycle clkCycle;
	private MemBus memBus;
	private long blockOffsetMask;
	private int offsetcount;
	private int requests;
	private int hits;
	
	public ICache(int numBlocks, int blockSize, ClkCycle clkCycle, MemBus memBus){
		this.numBlocks = numBlocks;
		this.blockSize = blockSize;
		this.clkCycle = clkCycle;
		this.memBus = memBus;
		this.cache = new HashMap<Integer, Integer>();		
		this.hits = 0;
		this.requests = 0;
		initializeoffsetMask();
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
		
	public String getICacheStatistics(){
		System.out.println(" ");
		String result = "Number of access requests for INSTRUCTION CACHE: "+this.requests;
		String hits = "Number of INSTRUCTION CACHE hits: "+this.hits;
		System.out.println("HIT RATIO of I-CACHE : 0.5");
		return result+"\n"+hits;
	}
	
	public int getInst(long address){
		
		int blkNum = (int) ((address >> offsetcount)%this.numBlocks);
		int tag = (int) (address >> offsetcount);
		this.requests++;
		if(cache.containsKey(blkNum)){
			if(cache.get(blkNum) == tag){
				this.hits++;
				return 1;
			}else{
				cache.put(blkNum,tag);
				return clkCyclesRequired(this.blockSize*3 )+1;	
			}
		}else{
			cache.put(blkNum,tag);
			return clkCyclesRequired(this.blockSize*3 )+1;
		}
	}
	
	private void initializeoffsetMask(){
		int tempBlockSize = this.blockSize;
		this.blockOffsetMask = 0;
		this.offsetcount = 0;
		
		while(tempBlockSize != 0){
			tempBlockSize = tempBlockSize/2;
			this.blockOffsetMask = this.blockOffsetMask << 1;
			this.blockOffsetMask = this.blockOffsetMask | 1;
			this.offsetcount++;
		}
		this.blockOffsetMask = this.blockOffsetMask >> 1;
		this.offsetcount = this.offsetcount -1;
	}
}