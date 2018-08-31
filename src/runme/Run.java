package runme;

import java.io.IOException;

import controllers.Constants;
import inputParsers.ConfigParser;
import inputParsers.InstructionParser;
import inputParsers.MemoryParser;

public class Run {

	private static String TAG = "ConfigParser";

	public static void main(String[] args) throws Exception {
		if(args.length != 4){
			System.out.println("Number of parameters should be 4");
			System.out.println("Usage: simulator inst.txt data.txt config.txt result.txt");
			throw new Exception();
		}else{
			//Creating objects for config,inst,memory parsers
			ConfigParser cparser = ConfigParser.getInstance();
			InstructionParser iParser = InstructionParser.getInstance();
			MemoryParser mParser = MemoryParser.getInstance();
			//Importing the Files
			try {
				Constants.instructionFilePath = args[0];
				Constants.memoryFilePath = args[1];
				Constants.configFilePath = args[2];
				Constants.resultFilePath = args[3];
				//				//			C:\Users\tchet\workspace\ScoreBoardWithCache\Config.txt
				//								Constants.configFilePath = "C:\\Users\\tchet\\Desktop\\files\\config_new.txt";
				//								Constants.instructionFilePath = "C:\\Users\\tchet\\workspace\\ScoreBoardWithCache\\Inst.txt";
				//								Constants.memoryFilePath = "C:\\Users\\tchet\\workspace\\ScoreBoardWithCache\\data.txt";
				//								Constants.resultFilePath = "C:\\Users\\tchet\\workspace\\ScoreBoardWithCache\\result.txt";
				//Using the created objects parsing the given files
				cparser.readConfigFile(Constants.configFilePath);
				iParser.readInstFile(Constants.instructionFilePath);
				mParser.readConfigFile(Constants.memoryFilePath);
				// Main Execution Program
				ProgramManager pm = new ProgramManager();
				pm.executeProgram();


			} catch (IOException e) {
				System.out.println(TAG+e.getMessage());
			}
		}
	}

}
