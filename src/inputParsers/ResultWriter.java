package inputParsers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
	
	private static ResultWriter rWriter = null;
	
	public static ResultWriter getInstance() {

		if(rWriter== null){
			rWriter = new ResultWriter();
		}
		return rWriter;
	}
	
	public void writeToFile(String filepath , String data){
		
		File fp = new File(filepath);
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(fp));
		    writer.write(data.trim());
		} catch (IOException e) {
			System.out.println("Exception in writing output "+e.getLocalizedMessage());
		} finally {
		    if (writer != null){
		    	try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		   
		}
	}

}
