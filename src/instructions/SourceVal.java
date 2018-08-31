package instructions;

public class SourceVal {
	
	private String Source = "";
	private int value = 0;
	
	public SourceVal(String s, int val){
		this.Source = s;
		this.value = val;
	}
	
	public String getSource() {
		return Source;
	}
	
	public void setSource(String source) {
		Source = source;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	

}
