import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardManager implements KeyListener {
	
	private int numKeys = 16;
	private boolean[] keys;
	private int[] keyMapping = new int[numKeys];
	
	private boolean anyKeyPressed;
	
	public void setKeyMapping(){
		//default mapping for now
		keyMapping[0] = KeyEvent.VK_7;
		keyMapping[1] = KeyEvent.VK_8;
		keyMapping[2] = KeyEvent.VK_9;
		keyMapping[3] = KeyEvent.VK_0;
		keyMapping[4] = KeyEvent.VK_Y;
		keyMapping[5] = KeyEvent.VK_U;
		keyMapping[6] = KeyEvent.VK_I;
		keyMapping[7] = KeyEvent.VK_O;
		keyMapping[8] = KeyEvent.VK_H;
		keyMapping[9] = KeyEvent.VK_J;
		keyMapping[10] = KeyEvent.VK_K;
		keyMapping[11] = KeyEvent.VK_L;
		keyMapping[12] = KeyEvent.VK_B;
		keyMapping[13] = KeyEvent.VK_N;
		keyMapping[14] = KeyEvent.VK_M;
		keyMapping[15] = KeyEvent.VK_COMMA;
		
		/*for (int i = 0; i < numKeys; i++) {
			//System.out.println("Please press the key for " + Integer.toHexString(i));
			//get user input somehow
			
			//put that user input into the key mapping table
		}*/
	}//setKeyMapping

	public KeyboardManager() {
		//set default key mapping
		setKeyMapping();
		keys = new boolean[numKeys];
		setAnyKeyPressed(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == keyMapping[0]) {
			keys[0] = true;
			setAnyKeyPressed(true);
			System.out.println("Key " + Integer.toHexString(keyMapping[0]) + " pressed.");
		} 
		
		if (e.getKeyCode() == keyMapping[1]) {
			keys[1] = true;
			setAnyKeyPressed(true);
			System.out.println("Key " + Integer.toHexString(keyMapping[1]) + " pressed.");
		}
		
		if (e.getKeyCode() == keyMapping[2]) {
			keys[2] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[3]) {
			keys[3] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[4]) {
			keys[4] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[5]) {
			keys[5] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[6]) {
			keys[6] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[7]) {
			keys[7] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[8]) {
			keys[8] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[9]) {
			keys[9] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xA]) {
			keys[0xA] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xB]) {
			keys[0xB] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xC]) {
			keys[0xC] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xD]) {
			keys[0xD] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xE]) {
			keys[0xE] = true;
			setAnyKeyPressed(true);
		}
		
		if (e.getKeyCode() == keyMapping[0xF]) {
			keys[0xF] = true;
			setAnyKeyPressed(true);
		}
	}//keyPressed

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == keyMapping[0]) {
			keys[0] = false;
			System.out.println("Key " + Integer.toHexString(keyMapping[0]) + " released.");
		} 
		
		if (e.getKeyCode() == keyMapping[1]) {
			keys[1] = false;
			System.out.println("Key " + Integer.toHexString(keyMapping[1]) + " released.");
		}
		
		if (e.getKeyCode() == keyMapping[2]) {
			keys[2] = false;
		}
		
		if (e.getKeyCode() == keyMapping[3]) {
			keys[3] = false;
		}
		
		if (e.getKeyCode() == keyMapping[4]) {
			keys[4] = false;
		}
		
		if (e.getKeyCode() == keyMapping[5]) {
			keys[5] = false;
		}
		
		if (e.getKeyCode() == keyMapping[6]) {
			keys[6] = false;
		}
		
		if (e.getKeyCode() == keyMapping[7]) {
			keys[7] = false;
		}
		
		if (e.getKeyCode() == keyMapping[8]) {
			keys[8] = false;
		}
		
		if (e.getKeyCode() == keyMapping[9]) {
			keys[9] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xA]) {
			keys[0xA] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xB]) {
			keys[0xB] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xC]) {
			keys[0xC] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xD]) {
			keys[0xD] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xE]) {
			keys[0xE] = false;
		}
		
		if (e.getKeyCode() == keyMapping[0xF]) {
			keys[0xF] = false;
		}
	}//keyReleased

	@Override
	public void keyTyped(KeyEvent e) {
		//We don't care about key typed events as it will never happen.
	}

	public boolean getKeyPressed(int key) {
		return keys[key]; //returns whether the key is pressed (true) or not.
	}

	public void setAnyKeyPressed(boolean anyKeyPressed) {
		this.anyKeyPressed = anyKeyPressed;
	}

	public boolean isAnyKeyPressed() {
		return anyKeyPressed;
	}

}//KeyboardManager
