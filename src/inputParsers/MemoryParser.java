package inputParsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controllers.Constants;

public class MemoryParser {

	private List<String []> data;
	private long startAddress;
	private static MemoryParser mParser = null;

	public static MemoryParser getInstance(){
		if(mParser == null){
			mParser = new MemoryParser();
		}
		return mParser;
	}

	private MemoryParser(){
		this.data = new ArrayList<String[]>();
		this.startAddress = 256;
		//readConfigFile(Constants.memoryFilePath);
	}
	
	public void readConfigFile(String filepath){
		System.out.println(filepath);
		BufferedReader br = null;
		try{
			if(filepath!= null && !filepath.isEmpty()){
				br = new BufferedReader(new FileReader(filepath));
				StringBuilder sb = new StringBuilder();
				String line;
				int index = 0;
				String[] blockData  = new String[4];
				while ((line = br.readLine()) != null) {
					if((index+1)%4 == 0){
						blockData[index%4] = line;
						data.add(blockData);
						blockData = new String[4];
					}
					blockData[index%4] = line;
					index++;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(br!= null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void pushMemDataToFile(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<data.size();i++){
			for(int j=0; j<data.get(i).length;j++){
				sb.append(data.get(i)[j]);
				sb.append(System.lineSeparator());
			}
		}
		ResultWriter.getInstance().writeToFile(Constants.memoryFilePath, sb.toString());
	}
	
	public String[] getdataArr(long address){
		int index = (int)(address - startAddress)/4;
		index = index/4;
		return data.get(index);
	}

	public String[] updateNReturnData(long address, int offset, String givendata){
		int index = (int)(address - startAddress)/4;
		index = index / 4;
		String[] dataArr = this.data.get(index);
		dataArr[offset] = givendata;
		return dataArr;
	}
	
	public void updateData(long address, String[] givendata){
		int index = (int)(address-startAddress)/4;
		index = index/4;
		String newData[] = new String[4];
		for(int i =0; i < givendata.length; i++){
			newData[i] = givendata[i];
		}
		data.set(index, newData);
	}

}
