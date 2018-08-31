package units;

import java.util.HashMap;

public class FPRegisters {

	HashMap<String, Float> fpRegisters;
	
	public FPRegisters(){
		fpRegisters = new HashMap<String, Float>();
	}
	
	public void writeTo(String register, Float value){
		fpRegisters.put(register, value);
	}
	
	public Float readFrom(String register){
		if(fpRegisters.containsKey(register)){
			return fpRegisters.get(register);
		}
		return null;
	}	
}
