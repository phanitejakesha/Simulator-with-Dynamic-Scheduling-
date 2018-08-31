package controllers;

public class ClkCycle {

	private long clock_cycle;
	//Defining the clock cycle for the given insturction
	public ClkCycle(){
		this.clock_cycle = 1;
	}
	//For incrementing the cycle if the instruction is in the next stage of pipeline
	public void tick(){
		this.clock_cycle++;
	}
	//To return the clock cycle
	public long getCycleCount(){
		return this.clock_cycle;
	}
}
