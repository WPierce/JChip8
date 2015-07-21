import java.util.Arrays;

public class Memory {
	
	//public int numKeys = 16;
	public int maxMem = 0xFFF;
	private boolean[] keyboard = new boolean[16];
	private int[] RAM = new int[maxMem];
	
	//constructor
	public Memory(){
		for (int i = 0; i < keyboard.length; i++) {
			RAM[i] = 0; //clear all memory
		}
		
		for (int i = 0; i < keyboard.length; i++) {
			keyboard[i] = false; //reset keyboard keys
		}
		
	}//constructor end
	
	public void setKeyPressed(int key, boolean value){
	    keyboard[key] = value;
	}

	public boolean getKeyPressed(int key){
	    return keyboard[key];
	}

	@Override
	public String toString() {
		String retString = "";
		for (int i = 0; i < maxMem; i++) {
			retString += Integer.toHexString(i);
			retString += "\t";
			retString += Integer.toHexString(RAM[i]);
			retString += "\n";
		}
		return retString;
		//return "Memory [RAM=" + Arrays.toString(RAM) + "]";
	}

	public int setValue(int location, int value){
	    if(location > maxMem){
	        return 0;
	    }
	    RAM[location] = value;
	    return 1;
	}

	public void setValue(int location, int[] values, int length){
	    for(int i=0; i<length; i++){
	        RAM[location+i] = values[i];
	    }
	}

	public int getValue(int location){
	    return RAM[location];
	}

}//class
