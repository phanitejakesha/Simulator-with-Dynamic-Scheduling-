package controllers;

public class ConfigManager {
//For setting and getting the number of functional units used or left for use	
	private int FPADDER_Units = 0;
	private int FPADDER_Cycles = 0;

	private int FPMULTIPLIER_Units = 0;
	private int FPMULTIPLIER_Cycles = 0;
	
	private int FPDIVIDER_Units = 0;
	private int FPDIVIDER_Cycles = 0;
	
	private int ICACHE_NUM_BLOCKS = 0;
	private int ICACHE_BLOCK_SIZE = 0;
	
	private static ConfigManager cManager = null;
	
	public static ConfigManager getInstance() {
		if(cManager== null){
			cManager = new ConfigManager();
		}
		return cManager;

	}
	
	public int getFPADDER_Units() {
		return FPADDER_Units;
	}
	public void setFPADDER_Units(int fPADDER_Units) {
		FPADDER_Units = fPADDER_Units;
	}
	public int getFPADDER_Cycles() {
		return FPADDER_Cycles;
	}
	public void setFPADDER_Cycles(int fPADDER_Cycles) {
		FPADDER_Cycles = fPADDER_Cycles;
	}
	public int getFPMULTIPLIER_Units() {
		return FPMULTIPLIER_Units;
	}
	public void setFPMULTIPLIER_Units(int fPMULTIPLIER_Units) {
		FPMULTIPLIER_Units = fPMULTIPLIER_Units;
	}
	public int getFPMULTIPLIER_Cycles() {
		return FPMULTIPLIER_Cycles;
	}
	public void setFPMULTIPLIER_Cycles(int fPMULTIPLIER_Cycles) {
		FPMULTIPLIER_Cycles = fPMULTIPLIER_Cycles;
	}
	public int getFPDIVIDER_Units() {
		return FPDIVIDER_Units;
	}
	public void setFPDIVIDER_Units(int fPDIVIDER_Units) {
		FPDIVIDER_Units = fPDIVIDER_Units;
	}
	public int getFPDIVIDER_Cycles() {
		return FPDIVIDER_Cycles;
	}
	public void setFPDIVIDER_Cycles(int fPDIVIDER_Cycles) {
		FPDIVIDER_Cycles = fPDIVIDER_Cycles;
	}
	public int getICACHE_NUM_BLOCKS() {
		return ICACHE_NUM_BLOCKS;
	}
	public void setICACHE_NUM_BLOCKS(int iCACHE_NUM_BLOCKS) {
		ICACHE_NUM_BLOCKS = iCACHE_NUM_BLOCKS;
	}
	public int getICACHE_BLOCK_SIZE() {
		return ICACHE_BLOCK_SIZE;
	}
	public void setICACHE_BLOCK_SIZE(int iCACHE_BLOCK_SIZE) {
		ICACHE_BLOCK_SIZE = iCACHE_BLOCK_SIZE;
	}
	
	
}
