package inputParsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import controllers.RunManager;
import instructions.ADDD;
import instructions.AND;
import instructions.ANDI;
import instructions.BEQ;
import instructions.BNE;
import instructions.DADD;
import instructions.DADDI;
import instructions.DIVD;
import instructions.DSUB;
import instructions.DSUBI;
import instructions.DataInst;
import instructions.HLT;
import instructions.J;
import instructions.LD;
import instructions.LI;
import instructions.LUI;
import instructions.LW;
import instructions.MULD;
import instructions.OR;
import instructions.ORI;
import instructions.SD;
import instructions.SUBD;
import instructions.SW;

public class InstructionParser {

	private static InstructionParser iparser= null;

	public static InstructionParser getInstance() {

		if(iparser== null){
			iparser = new InstructionParser();
		}
		return iparser;
	}

	public void readInstFile(String filepath) throws IOException {

		System.out.println(filepath);
		if(filepath!= null && !filepath.isEmpty()){
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			try {
				String line;
				while ((line= br.readLine()) != null) {			    	
					if(!line.trim().isEmpty()){
						getInfoFromInst(line);
					}
				}
			}catch(Exception ex){
				System.out.println("Exception in readInstFile"+ex.getMessage());
			}finally {
				br.close();
			}
		}
	}

	/***
	 * Creates a new object for every instruction and stores it in the map
	 * @param line
	 * @throws Exception 
	 */
	private void getInfoFromInst(String line) throws Exception {
		DataInst instruction = null;
		String[] labelNInst = line.trim().split(":");
		String label = "";
		String inst = "";
		String opcode = "";
		String destReg = "";
		String sourcereg1 ="";
		String sourcereg2 = "";
		String immediateVal = "";
		RunManager rManager = RunManager.getInstance();

		if(labelNInst.length==2){
			label = labelNInst[0].trim();
			inst = labelNInst[1].trim();
			RunManager.getInstance().addLabelToMap(label,RunManager.getInstance().getInstructionCount());
		}else{
			inst = labelNInst[0].trim();
		}

		String[] instparamArr = inst.trim().split(",|\\(|\\)|\\s+");
		List<String> instparamlist = new ArrayList<String>(Arrays.asList(instparamArr));
		instparamlist.removeAll(Collections.singleton(null));
		instparamlist.removeAll(Collections.singleton(""));

		if(instparamlist.size()>=1){

			opcode = instparamlist.get(0).trim().toUpperCase();
			switch(opcode){
			case "ADD.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new ADDD(destReg, sourcereg1, sourcereg2);
				break;
			case "SUB.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SUBD(destReg, sourcereg1, sourcereg2);
				break;
			case "MUL.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new MULD(destReg, sourcereg1, sourcereg2);
				break;
			case "DIV.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DIVD(destReg, sourcereg1, sourcereg2);
				break;
			case "DADD":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DADD(destReg, sourcereg1, sourcereg2);
				break;
			case "DSUB":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DSUB(destReg, sourcereg1, sourcereg2);
				break;
			case "AND":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new AND(destReg, sourcereg1, sourcereg2);
				break;
			case "OR":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new OR(destReg, sourcereg1, sourcereg2);
				break;
			case "DADDI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new DADDI(destReg, sourcereg1, immediateVal);
				break;
			case "DSUBI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new DSUBI(destReg, sourcereg1, immediateVal);
				break;
			case "ANDI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new ANDI(destReg, sourcereg1, immediateVal);
				break;
			case "ORI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new ORI(destReg, sourcereg1, immediateVal);
				break;
			case "LW":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg1 = instparamlist.get(3).trim().toUpperCase();
				instruction = new LW(destReg, sourcereg1, immediateVal);
				break;
			case "L.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg1 = instparamlist.get(3).trim().toUpperCase();
				instruction = new LD(destReg, sourcereg1, immediateVal);
				break;
			case "SW":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SW(sourcereg1, sourcereg2, immediateVal);
				break;
			case "S.D":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SD(sourcereg1, sourcereg2, immediateVal);
				break;
			case "LI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				instruction = new LI(destReg, immediateVal);
				break;
			case "LUI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				instruction = new LUI(destReg, immediateVal);
				break;
			case "BNE":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				sourcereg2 = instparamlist.get(2).trim().toUpperCase();
				destReg = instparamlist.get(3).trim().toUpperCase();
				instruction = new BNE(sourcereg1, sourcereg2, destReg);
				break;
			case "BEQ":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				sourcereg2 = instparamlist.get(2).trim().toUpperCase();
				destReg = instparamlist.get(3).trim().toUpperCase();
				instruction = new BEQ(sourcereg1, sourcereg2, destReg);
				break;
			case "J":
				destReg = instparamlist.get(1).trim().toUpperCase();
				instruction = new J(destReg);
				break;
			case "HLT":
				instruction = new HLT();
				break;
			default:
				System.out.println("Exception in Line : "+(rManager.getInstructionCount()+1)+" "+line);
				throw new Exception();
			}
			instruction.setInstOutptuStr(instruction.toString());
			instruction.setLabeltoOutputStr(label);
			rManager.addInstructionToMap(rManager.getInstructionCount(), line);

		}
	}
	public static  DataInst getInstructionObj(String line) {
		DataInst instruction = null;
		String[] labelNInst = line.trim().split(":");
		String label = "";
		String inst = "";
		String opcode = "";
		String destReg = "";
		String sourcereg1 ="";
		String sourcereg2 = "";
		String immediateVal = "";
		RunManager rManager = RunManager.getInstance();

		if(labelNInst.length==2){
			label = labelNInst[0].trim();
			inst = labelNInst[1].trim();
			//RunManager.getInstance().addLabelToMap(label,RunManager.getInstance().getInstructionCount());
		}else{
			inst = labelNInst[0].trim();
		}

		String[] instparamArr = inst.trim().split(",|\\(|\\)|\\s+");
		List<String> instparamlist = new ArrayList<String>(Arrays.asList(instparamArr));
		instparamlist.removeAll(Collections.singleton(null));
		instparamlist.removeAll(Collections.singleton(""));

		if(instparamlist.size()>=1){

			opcode = instparamlist.get(0).trim().toUpperCase();
			switch(opcode){
			case "ADD.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new ADDD(destReg, sourcereg1, sourcereg2);
				break;
			case "SUB.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SUBD(destReg, sourcereg1, sourcereg2);
				break;
			case "MUL.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new MULD(destReg, sourcereg1, sourcereg2);
				break;
			case "DIV.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DIVD(destReg, sourcereg1, sourcereg2);
				break;
			case "DADD":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DADD(destReg, sourcereg1, sourcereg2);
				break;
			case "DSUB":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new DSUB(destReg, sourcereg1, sourcereg2);
				break;
			case "AND":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new AND(destReg, sourcereg1, sourcereg2);
				break;
			case "OR":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new OR(destReg, sourcereg1, sourcereg2);
				break;
			case "DADDI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new DADDI(destReg, sourcereg1, immediateVal);
				break;
			case "DSUBI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new DSUBI(destReg, sourcereg1, immediateVal);
				break;
			case "ANDI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new ANDI(destReg, sourcereg1, immediateVal);
				break;
			case "ORI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				sourcereg1 = instparamlist.get(2).trim().toUpperCase();
				immediateVal = instparamlist.get(3).trim().toUpperCase();
				instruction = new ORI(destReg, sourcereg1, immediateVal);
				break;
			case "LW":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg1 = instparamlist.get(3).trim().toUpperCase();
				instruction = new LW(destReg, sourcereg1, immediateVal);
				break;
			case "L.D":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg1 = instparamlist.get(3).trim().toUpperCase();
				instruction = new LD(destReg, sourcereg1, immediateVal);
				break;
			case "SW":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SW(sourcereg1, sourcereg2, immediateVal);
				break;
			case "S.D":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				sourcereg2 = instparamlist.get(3).trim().toUpperCase();
				instruction = new SD(sourcereg1, sourcereg2, immediateVal);
				break;
			case "LI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				instruction = new LI(destReg, immediateVal);
				break;
			case "LUI":
				destReg = instparamlist.get(1).trim().toUpperCase();
				immediateVal = instparamlist.get(2).trim().toUpperCase();
				instruction = new LUI(destReg, immediateVal);
				break;
			case "BNE":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				sourcereg2 = instparamlist.get(2).trim().toUpperCase();
				destReg = instparamlist.get(3).trim().toUpperCase();
				instruction = new BNE(sourcereg1, sourcereg2, destReg);
				break;
			case "BEQ":
				sourcereg1 = instparamlist.get(1).trim().toUpperCase();
				sourcereg2 = instparamlist.get(2).trim().toUpperCase();
				destReg = instparamlist.get(3).trim().toUpperCase();
				instruction = new BEQ(sourcereg1, sourcereg2, destReg);
				break;
			case "J":
				destReg = instparamlist.get(1).trim().toUpperCase();
				instruction = new J(destReg);
				break;
			case "HLT":
				instruction = new HLT();
				break;
			}
			instruction.setInstOutptuStr(instruction.toString());
			instruction.setLabeltoOutputStr(label);
			return instruction;
			//rManager.addInstructionToMap(rManager.getInstructionCount(), line);

		}
		return null;
	}

}
