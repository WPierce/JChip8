import java.util.Arrays;
import java.util.Random;


public class CPU {
	
	private final Random rnd = new Random();

	private int MAX_STACK_DEPTH = 16;

	private int I; //address pointer
	private int SP; //stack pointer
	private int PC; //program counter

	int DT; //delay timer
	int ST; //sound timer

	//the memory system
	Memory memSystem;
	KeyboardManager keyboard;

	//the screen
	Display display;

	private int[] stack = new int[MAX_STACK_DEPTH]; //this is the recursion stack

	private int [] V = new int[16]; //registers vx 0 through 15

	public CPU(int entryPoint, Memory memSystem, Display display, KeyboardManager keyboard) {
		this.memSystem = memSystem;
		this.display = display;
		this.keyboard = keyboard;

		I = 0;
		SP = 0x00000000;
		PC = entryPoint;
		DT = 0;
		ST = 0;

		for (int i = 0; i < MAX_STACK_DEPTH; i++) {
			stack[i] = 0;
		}
	}//CPU
	
	public void updateDT(){
		if(DT > 0){
			DT = DT - 1;
		}
		//System.out.println("DT was " + (DT + 1) + " is now " + DT);
	}//updateDT

	int fetchOpcode(){
		int opcode = memSystem.getValue(PC);
		//System.out.println("Orig op: " + opcode);
		opcode = opcode << 8;
		//System.out.println("Op << 8: " + opcode);
		opcode += memSystem.getValue(PC+1);
		//System.out.println("End op: " + opcode);
		return opcode;
	}//fetchOpcode

	int getPC(){
		return PC;
	}

	public int getI() {
		return I;
	}

	public int getSP() {
		return SP;
	}

	public int[] getRegisters() {
		return V;
	}

	public void execute(int opcode){
		System.out.print(Integer.toHexString(PC) + "\t");
		int addr, x, k, y;

		//cout << (opcode&0xF000) << endl;
		//System.out.printf ("Opcode :%X \n", opcode);
		//System.out.printf ("Opcode slice:%X \n", (opcode&0xF000));

		switch ((opcode&0xF000)) { //want only the first byte of the opcode initially
		case 0x0000:
			switch((opcode&0x00FF)){
			case 0x00E0: //CLS
				//clears the display;
				System.out.println("CLS");
				display.clearScreen();

				PC += 2;
				break;

			case 0x00EE: //return from subroutine
				//PC = address at top of stack
				System.out.println("RET");
				//Stack pointer = stack pointer - 1
				SP--;
				PC = stack[SP];
				stack[SP] = 0x0; //added in from JChip
				PC+=2;
				break;

			default:
				System.out.printf("Unknown opcode (//00FF switch):%X \n", opcode);
				break;
			}//00FF switch
			break;

		case 0x1000: //case 0x1nnn //jump to location nnn
			addr = opcode & 0x0FFF;
			System.out.println("JMP to " + Integer.toHexString(addr));
			PC = addr;
			break;

		case 0x2000: //case 0x2nnn //call subroutine at nnn
			addr = opcode & 0x0FFF;
			System.out.println("CALL subroutine at " + addr);
			//put current PC on top of stack
			stack[SP] = PC;
			SP++;
			PC = addr;

			break;

		case 0x3000: //case 0x3xkk //skip next instruction if Vx = kk
			//vx == kk? -> Yes then PC = PC+2
			x = (opcode & 0x0F00) >> 8;
			k = (opcode & 0x00FF);
	
			System.out.println("SKEQ V" + Integer.toHexString(x) + " " + Integer.toHexString(k));
			
			if(V[x] == k){
				PC = PC + 2;
			}
			PC = PC + 2;
			break;

	case 0x4000: //case 0x4xkk //skip next instruction if Vx != kk
		//System.out.println("SNE");
		//Vx == kk? no -> PC = PC+2
		x = (opcode & 0x0F00) >> 8;
		k = (opcode & 0x00FF);
		
		System.out.println("SKNE V" + Integer.toHexString(x) + " 0x" + Integer.toHexString(k));

		if(V[x] != k){
			PC = PC + 2;
		}
		PC = PC + 2;
		break;

		case 0x5000: //case 0x5xy0 //skip next instruction if Vx = Vy
			//System.out.println("SE Vx, byte");
			//Vx == Vy? yes -> PC = PC+2
			x = (opcode & 0x0F00) >> 8;
			y = (opcode & 0x00F0) >> 4;
			
			System.out.println("SKEQV V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

			if(V[x] == V[y]){
				PC = PC + 2;
			}
			PC = PC + 2;
			break;

		case 0x6000: //case 0x6xkk //set Vx = kk
			//Vx = kk
			x = (opcode & 0x0F00) >> 8;
			k = (opcode & 0x00FF);

			System.out.println("MOV V" + Integer.toHexString(x) + " 0x" + Integer.toHexString(k));
			
			V[x] = k;
			PC += 2;
			break;

		case 0x7000: //case 0x7xkk //ADD VX, byte
			//Vx = Vx + kk
			//System.out.println("ADD Vx, byte");
			x = (opcode & 0x0F00) >> 8;
			k = (opcode & 0x00FF);
			
			System.out.println("ADD V" + Integer.toHexString(x) + " 0x" + Integer.toHexString(k));

			//System.out.println("V" + Integer.toHexString(x) + " Was " + Integer.toHexString(V[x]) + " Now " + Integer.toHexString(V[x] + k));
			int sum = V[x] + k; //mask to remove upper bits
			V[x] = sum & 0xFF; 
			
			if (sum > 0xFF){
				V[0xF] = 1;
			} else {
				V[0xF] = 0;
			}
			
			PC += 2;
			break;

		case 0x8000:
			switch((opcode&0x000F)){
			case 0x0000: //case 0x8xy0 //LD Vx, Vy
				//Vx = Vy
				//System.out.println("LD Vx, Vy");
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("MOV V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				V[x] = V[y];
				PC += 2;
				break;

			case 0x0001: // case 0x8xy1 //OR Vx, Vy
				//Vx = Vx OR Vy
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("OR V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				V[x] = (V[x] | V[y]) & 0xFF; //no need to mask just for security
				PC += 2;
				break;

			case 0x0002: //case 0x8xy2 //AND Vx, Vy
				//Vx = Vx AND Vy
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("AND V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				V[x] = (V[x] & V[y]) & 0xFF; //no need to mask just for security
				PC += 2;
				break;

			case 0x0003: //case 0x8xy3 //XOR Vx, Vy
				//Vx = Vx OR Vy
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("XOR V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				V[x] = (V[x] ^ V[y]) & 0xFF; //no need to mask just for security
				PC += 2;
				break;

			case 0x0004: //case 0x8xy4 //ADD Vx, Vy. If Vx+Vy > 255, VF=1 TODO: should it carry?
				//Vx = Vx + Vy
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("ADD V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				sum = (V[x]+V[y]) & 0xFF; //mask off top bits for unsigned
				V[x] = sum;
				
				if (sum > 0xFF){
					V[0xF] = 1;
				} else {
					V[0xF] = 0;
				}
				PC += 2;

				break;

			case 0x0005: //case 0x8xy5 //SUB
				//Vx = Vx - Vy.
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("SUB V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				if(V[x] >= V[y]){ //TODO: >= based on JChip code
					V[0xF] = 1;
				} else {
					V[0xF] = 0;
				}

				V[x] = 0xFF & (V[x] - V[y]); //mask to remove upper bits
				PC += 2;
				break;

			case 0x0006: //case 0x8xy6 //SHR
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("SHR V" + Integer.toHexString(x) + " V" + Integer.toHexString(y) + "(v" + Integer.toHexString(y) + " is ignored)");

				if((V[x] & 0x01) > 0){
					V[0xF] = 1;
				} else {
					V[0xF] = 0;
				}

				V[x] = (V[x] >> 1) & 0xFF; //probably don't need to mask but never hurts
				PC += 2;
				break;

			case 0x0007: //case 0x8xy7 //SUBN
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("SUBN V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

				if( V[x] <= V[y]){ //TODO: >= based on Jchip, used to be just >
					V[0xF] = 1;
				} else {
					V[0xF] = 0;
				}

				V[x] = (V[y] - V[x]) & 0xFF; //mask off upper  bits for unsigned
				PC += 2;

				break;

			case 0x000E: //case 0x8xyE //SHL
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				
				System.out.println("SHL V" + Integer.toHexString(x) + " V" + Integer.toHexString(y) + "(vy is ignored)");

				if((V[x] & 0x80) > 0){
					V[0xF] = 1;
				} else {
					V[0xF] = 0;
				}
				
				V[x] = ((V[x] << 1) & 0xFF); //mask to remove upper bits
				PC += 2;
				break;

			default:
				System.out.printf ("Unknown opcode (//0x8000 switch):%X \n", opcode);
				break;
			}//0x8000 switch
			break;

		case 0x9000: //case 0x9xy0 //SNE Vx, Vy
			//if Vx != Vy, skip next instr
			x = (opcode & 0x0F00) >> 8;
			y = (opcode & 0x00F0) >> 4;
			
			System.out.println("SNE V" + Integer.toHexString(x) + " V" + Integer.toHexString(y));

			if(V[x] != V[y]){
				PC = PC + 2;
			}
			PC += 2;
			break;

			case 0xA000://case 0xAnnn //Ld I, addr
				//I = nnn
				System.out.println("LD I, addr (addr=" + Integer.toHexString((opcode & 0x0FFF)) + ")");
				I = opcode & 0x0FFF;
				PC += 2;
				break;

			case 0xB000: //case 0xBnnn //JP V0, addr
				//PC = nnn + V0
				System.out.println("JMP V0 + addr (addr = " + (opcode & 0x0FFF) + ")");
				PC = (opcode & 0x0FFF) + V[0];
				//PC += 2;
				break;

			case 0xC000: //case 0xCxkk //RND Vx, kk
				//System.out.println("RND");
				x = (opcode & 0x0F00) >> 8;
				k = opcode & 0x00FF;
				
				System.out.println("RAND V" + x + " AND " + Integer.toHexString(k));
				
				V[x] = rnd.nextInt(0x100) & k; //TODO:JChip code
				
				
				//value = rand()
				//int value = (int) ((Math.round(Math.random())) * 256); //random num between 0...256
				//value = value AND kk.
				//value = value & k;
				//Vx = value
				//V[x] = value;
				PC += 2;
				break;

			case 0xD000: //case 0xDxyn //DRW Vx, Vy, nibble
				//System.out.println("DRW");
				//System.out.printf ("Opcode :%X \n", opcode);
				//Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.
				x = (opcode & 0x0F00) >> 8;
				y = (opcode & 0x00F0) >> 4;
				int n = (opcode & 0x000F); //height of the sprite
				
				V[0xF] = 0x0; //reset collision register before we start drawing

				System.out.println("DRW V" + Integer.toHexString(x) + " V" + Integer.toHexString(y) + " " + Integer.toHexString(n));
				
				//read n bytes from memory, starting at I
				for(int i=0; i<n; i++){ //loop for each byte (row) in the sprite
					int data = memSystem.getValue(I + i); //retrives the byte for the given line of pixels
					for(int j=0; j < 8; j++){ //this loop is for each bit in the byte
						//get current pixel value
						if (V[x] + j < 64 && V[y] + i < 32) { //TODO: Cowgod specifies we should loop round, but no implementation currently does this
							int curPix = display.getValue((V[x] + j), (V[y] + i));
							//get the pixel value from the byte in the data
							int newPix = data & (0x80 >> j);
							
							if (newPix > 0) { //TODO: These two ifs can go?
								newPix = 1;
							}
							if (curPix > 0) {
								curPix = 1;
							}

							if(curPix >= 1 && newPix >= 1){ //if there's a collision
			                    V[0xF] = 1;
			                }
							
							newPix = newPix ^ curPix; //Xor the value
							display.setValue((V[x] + j), (V[y] + i), newPix); //set in display
						}//if fits in display
					}//for each pixel in the current row
				}//for each row in sprite

				PC += 2;
				break;

			case 0xE000:
				switch((opcode&0x00FF)){
				//System.out.println("SKP");
				case 0x009E: //case 0xEx9E //SKP Vx
					//cout << "Instruction not yet implemented" << endl;
					x = (opcode & 0x0F00) >> 8;
					
					System.out.println("SKP if key V" + Integer.toHexString(x) + "pressed");
					
					boolean keyPressed = keyboard.getKeyPressed(V[x]);
					if(keyPressed){
						PC += 2;
					}
					PC += 2;
					break;

				case 0x00A1: //case 0xExA1 //SKNP Vx
					//cout << "Instruction not yet implemented" << endl;
					x = (opcode & 0x0F00) >> 8;
					
					System.out.println("SKP if key V" + Integer.toHexString(x) + " not pressed");
					
					keyPressed = keyboard.getKeyPressed(V[x]);
					if(!keyPressed){
						PC += 2;
					}
					PC += 2;
					break;

				default:
					System.out.printf ("Unknown opcode (0xE000 switch):%X \n", opcode);
					break;
				}//0xE000 switch
				break;

			case 0xF000:
				//cout << "Opcode with 0xF000 tag" << endl;
				switch((opcode&0x00FF)){
				case 0x0007: //case 0xFx07 //LD Vx, DT
					x = (opcode & 0x0F00) >> 8;
					System.out.println("LD V" + Integer.toHexString(x) + "DT");
					V[x] = DT & 0xFF; //mask upper bits
					System.out.println("DT is " + DT);
					PC += 2;
					break;

				case 0x000A: //case 0xFx0A //LD Vx, K
					System.out.println("LD Vx, K");
					//System.out.println("LD V" + Integer.toHexString(x) + "K");
					boolean anyKeyPressed = false;
					while (!anyKeyPressed) {
						anyKeyPressed = keyboard.isAnyKeyPressed();
					}
					
					//reset key press
					keyboard.setAnyKeyPressed(false);
					System.out.println("Instruction not yet implemented");
					PC += 2;
					break;

				case 0x0015: //case 0xFx15 //LD Dt, Vx
					x = (opcode & 0x0F00) >> 8;
					System.out.println("LD DT V" + Integer.toHexString(x));

					DT = V[x];
					System.out.println("DT set to " + DT);
					PC += 2;
					break;

				case 0x0018: //case 0xFx18 //LD ST, Vx
					x = (opcode & 0x0F00) >> 8;
					System.out.println("LD ST V" + Integer.toHexString(x));
					ST = V[x];
					PC += 2;
					break;

				case 0x001E: //case 0xFx1E //ADD I,Vx
					//System.out.println("ADD I,Vx");
					x = (opcode & 0x0F00) >> 8;
					System.out.println("ADD I V" + Integer.toHexString(x));
					I = I + V[x];
					PC += 2;
					break;

				case 0x0029: //case 0xFx29 //LD F, Vx
					//System.out.println("LD F, Vx");
					x = (opcode & 0x0F00) >> 8;
					System.out.println("I = hex sprite V" + Integer.toHexString(x));
					//I = 0x0000 + V[x]; //TODO: Massive hack here
					I = (V[x]*5); //TODO: Not sure this is correct
					System.out.printf("Value of V[%X]        %X\n", x, V[x]);
					//I = (V[((opcode&0x0F00)>>8)]*5);
					//cout << "Instruction not yet implemented" << endl;
					PC += 2;
					break;

				case 0x0033: //case 0xFx33//LD B, Vx
					//System.out.println("LD B, Vx");
					x = (opcode & 0x0F00) >> 8;
					System.out.println("LD B V" + Integer.toHexString(x));

					//memSystem.setValue(I, x/100);
					//memSystem.setValue(I+1, (x/10) % 10);
					//memSystem.setValue(I+2, (x % 100) % 10);

					memSystem.setValue(I, V[x]/100);
					memSystem.setValue(I+1, (V[x]/10) % 10);
					memSystem.setValue(I+2, (V[x] % 100) % 10);
					PC += 2;
					break;

				case 0x0055: //case 0xFx55 //LD [I], Vx
					x = (opcode & 0x0F00) >> 8;
					System.out.println("STORE [I] V" + Integer.toHexString(x));
					for(int i=0; i<=x; i++){
						memSystem.setValue(I+i, V[i]);
					}
					PC += 2;
					break;

				case 0x0065: //case 0xFx65 //LD Vx, [I]
					//System.out.println("LD Vx, [I]");
					x = (opcode & 0x0F00) >> 8;
					System.out.println("LOAD [I] V" + Integer.toHexString(x));
					for(int i=0; i<=x; i++){
						V[i] = memSystem.getValue(I+i);
					}
					PC += 2;
					break;

				default:
					System.out.printf ("Unknown opcode (0xF000 switch):%X \n", opcode);
					break;
				}//0xF000 switch
				break;

			default:
				System.out.printf ("Unknown opcode (main SW):%X \n", opcode);
				break;
		}//outside switch
	}//execute opcode

	@Override
	public String toString() {
		return "CPU [I=" + I + ", SP=" + SP + ", PC=" + PC + ", DT=" + DT
		+ ", ST=" + ST + ", V=" + Arrays.toString(V) + "]";
	}


}//CPU
