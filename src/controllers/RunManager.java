package controllers;

import java.util.HashMap;
import java.util.Map;

import inputParsers.InstructionParser;
import instructions.DataInst;

public class RunManager {
	
	public static RunManager rManager = null;
	
	private int InstructionCount =0;
	private Map<String,Integer> labelMap = null;
	private Map<Integer, String> instructionlist = null;
	
	public static RunManager getInstance() {
		if(rManager == null){
			rManager = new RunManager();
		}
		return rManager;
	}
	
	private RunManager() {
		this.InstructionCount = 0;
		this.instructionlist = new HashMap<>();
		this.labelMap = new HashMap<>();
	}
	
	public DataInst getInstructioninAddress(int address) {
		if(instructionlist.containsKey(address)){
			String instStr= instructionlist.get(address);
			return InstructionParser.getInstructionObj(instStr);
		}else{
			return null;
		}		
	}
	
	/***
	 * 
	 * @param lbl
	 * @return address if the label exists otherwise returns -1
	 */
	public int getAddressforLabel(String lbl){
		if(labelMap.containsKey(lbl)){
			return labelMap.get(lbl);
		}else{
			return -1;
		}
	}

	public int getInstructionCount() {
		return InstructionCount;
	}

	public void setInstructionCount(int instructionCount) {
		InstructionCount = instructionCount;
	}

	public Map<String, Integer> getLabelMap() {
		return labelMap;
	}

	public void setLabelMap(Map<String, Integer> labelMap) {
		this.labelMap = labelMap;
	}

	public Map<Integer, String> getInstructionlist() {
		return instructionlist;
	}

	public void setInstructionlist(Map<Integer, String> instructionlist) {
		this.instructionlist = instructionlist;
	}

	/***
	 * adds instruction to the map and increments the instruction count
	 * @param add
	 * @param inst
	 */
	public void addInstructionToMap(int add, String instStr){
		this.instructionlist.put(add, instStr);
		this.InstructionCount++;
		//System.out.println(getInstructionCount()+" : "+inst.toString());
	}
	
	public void addLabelToMap(String lbl, int add){
		this.labelMap.put(lbl, add);
	}
}
