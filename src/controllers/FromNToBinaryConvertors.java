package controllers;

public class FromNToBinaryConvertors {

	public static String toBinary(int data){
		char bstr[] = new char[32];
		for(int i = 0 ; i < bstr.length; i++){
			bstr[i] = '0';
		}
		long nVal;
		if(data < 0){
			nVal = data + Integer.MIN_VALUE;
			bstr[0] = '1';
		}else{
			nVal = data;
			bstr[0] = '0';
		}
		int index = 31;
		while(nVal != 0){
			int mod = (int) (nVal%2);
			if(mod == 0){
				bstr[index] = '0';
			}else{
				bstr[index] = '1';
			}
			index--;
			nVal = nVal/2;
		}
		return new String(bstr);
	}
	
	public static int toInt(String data){
		int val = 0;
		for(int i = 1; i < data.length(); i++){
			val = val * 2 + (data.charAt(i)-'0');
		}
		if(data.charAt(0) == '1'){
			val = val + Integer.MIN_VALUE;
		}
		return val;
	}
}
