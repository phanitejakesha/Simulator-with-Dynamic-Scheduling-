package inputParsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import controllers.ConfigManager;

public class ConfigParser {

	private static ConfigParser cparser= null;
	
	public static ConfigParser getInstance() {

		if(cparser== null){
			cparser = new ConfigParser();
		}
		return cparser;
	}
	
	public void readConfigFile(String filepath) throws IOException {
		
		System.out.println(filepath);
		if(filepath!= null && !filepath.isEmpty()){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(filepath));
				String line;
			    while ((line= br.readLine()) != null) {			    	
			        if(!line.trim().isEmpty()){
			        	updateConfigInfo(line);
			        }
			    }
			}catch(Exception ex){
				System.out.println("Exception in readConfigFile"+ex.getMessage());
			}finally {
				if(br!= null){
					br.close();
				}			    
			}
		}
	}

	private void updateConfigInfo(String line) {
		
		ConfigManager cManager = ConfigManager.getInstance();
		String[] linearr = line.split(",|:");
		if(linearr.length==3){
			String key = linearr[0].trim();
			String units = linearr[1].trim();
			String cycles = linearr[2].trim();
			
			switch(key.toUpperCase().trim()){
			
				case "FP ADDER":
					cManager.setFPADDER_Units(Integer.valueOf(units));
					cManager.setFPADDER_Cycles(Integer.valueOf(cycles));
					break;
				case "FP MULTIPLIER":
					cManager.setFPMULTIPLIER_Units(Integer.valueOf(units));
					cManager.setFPMULTIPLIER_Cycles(Integer.valueOf(cycles));
					break;
				case "FP DIVIDER":
					cManager.setFPDIVIDER_Units(Integer.valueOf(units));
					cManager.setFPDIVIDER_Cycles(Integer.valueOf(cycles));
					break;
				case "I-CACHE":
					cManager.setICACHE_NUM_BLOCKS(Integer.valueOf(units));
					cManager.setICACHE_BLOCK_SIZE(Integer.valueOf(cycles));
					break;
			}
		}
		
		
	}
}
