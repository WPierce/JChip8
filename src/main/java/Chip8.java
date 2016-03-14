import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

public class Chip8
{

    private Memory memSys;
    private Display display;
    private KeyboardManager keyboard;

    private final Logger log = LogManager.getLogger(Chip8.class);

    static
    {
        try
        {
            Log4JConfigurator.configureFrom("src/main/resources/Log4jConfig.xml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    int[] font = {0xF0, 0x90, 0x90, 0x90, 0xF0,// 0
        0x20, 0x60, 0x20, 0x20, 0x70,// 1
        0xF0, 0x10, 0xF0, 0x80, 0xF0,// 2
        0xF0, 0x10, 0xF0, 0x10, 0xF0,// 3
        0x90, 0x90, 0xF0, 0x10, 0x10,// 4
        0xF0, 0x80, 0xF0, 0x10, 0xF0,// 5
        0xF0, 0x80, 0xF0, 0x90, 0xF0,// 6
        0xF0, 0x10, 0x20, 0x40, 0x40,// 7
        0xF0, 0x90, 0xF0, 0x90, 0xF0,// 8
        0xF0, 0x90, 0xF0, 0x10, 0xF0,// 9
        0xF0, 0x90, 0xF0, 0x90, 0x90,// A
        0xE0, 0x90, 0xE0, 0x90, 0xE0,// B
        0xF0, 0x80, 0x80, 0x80, 0xF0,// C
        0xE0, 0x90, 0x90, 0x90, 0xE0,// D
        0xF0, 0x80, 0xF0, 0x80, 0xF0,// E
        0xF0, 0x80, 0xF0, 0x80, 0x80 // F
    };

    public boolean loadRom(String filename, int startLocation)
    {
        log.info("Loading rom " + filename);
        try
        {
            FileInputStream rom = new FileInputStream(filename);
            int i = 0;
            int value = 0;
            while ((value = rom.read()) != -1)
            {//i.e still have data left
                //System.out.println("Value: " + Integer.toHexString(value));
                memSys.setValue(startLocation + i, value);
                i++;
            }//while
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error opening ROM file: " + filename);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
        return true;
    }//loadRom

    public void printDebugInfo(CPU cpu, int opcode)
    {

        //print out memory
        if ((opcode & 0xF000) == 0xF000)
        {
            if ((opcode & 0x00FF) == 0x0055)
            {
                System.out.println(memSys);
            }
        }

        if ((opcode & 0xF000) == 0xD000)
        {
            System.out.println(memSys);
        }

        System.out.println(cpu);
    }//printDebugInfo

    public void loadFonts()
    {
        for (int i = 0; i < 0x50; i++)
        {
            memSys.setValue(i, font[i]);
        }
    }//loadFonts

    public void updateScreen(RenderPanel2 panel)
    {
        panel.repaint();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Starting program");

        Chip8 chip8 = new Chip8();
        chip8.memSys = new Memory();
        chip8.display = new Display(64, 32);
        chip8.keyboard = new KeyboardManager();
        CPU cpu = new CPU(0x200, chip8.memSys, chip8.display, chip8.keyboard);

        chip8.loadFonts();

        String filename = "src/main/resources/chip8pack/SuperChip Test Programs/SC Test.ch8";
//        String filename = "/home/wpierce/ideaProjects/JChip8/src/main/resources/chip8pack/Chip-8 Games/Blinky [Hans Christian Egeberg, 1991].ch8";
        int startLocation = 0x0200;
        chip8.loadRom(filename, startLocation);

        //create JFrame
        JFrame frame = new JFrame("Render Test");
        frame.setLayout(new GridLayout());
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(660, 360));
        RenderPanel2 screen = new RenderPanel2(chip8.display);
        screen.setMinimumSize(new Dimension(640, 320));
        frame.add(screen);
        frame.addKeyListener(chip8.keyboard);

        boolean executeNextInstr = false;

        boolean done = false;

        long previousTime = System.currentTimeMillis();
        long curTime;

        while (!done)
        {

            //System.out.println(cpu);
            //System.out.println(chip8.memSys);
            //handle input here

            //start drawing here
            for (int cpuDelay = 0; cpuDelay < 1; cpuDelay++)
            {
                executeNextInstr = true;
                if (executeNextInstr == true)
                {
                    //fetch opcode
                    int opcode = cpu.fetchOpcode();
                    //System.out.println("opcode is hex " + Integer.toHexString(opcode) );
                    //chip8.printDebugInfo(cpu, opcode);
                    //cout << "Program counter: " << cpu.getPC() << endl;
                    //call cpu.execute
                    //System.out.printf ("Executing opcode :%X \n", opcode);

                    //int result = opcode & 0xF000;
                    //result = opcode&0x00FF;
                    //System.out.println(Integer.toHexString(result));
                    cpu.execute(opcode);
                    //test DT & decriment
                    //test ST & decriment
                    //test keypad
                    //test for exit condition
                    chip8.updateScreen(screen);
                    executeNextInstr = false;
                    cpu.updateDT();
                    curTime = System.currentTimeMillis();
                    if (curTime - previousTime > 0.166)
                    { //update DT
                        cpu.updateDT();
                        previousTime = curTime;
                    }
                }
            }

            waitForPeriod();

//            for (int i = 0; i < 5000000L; i++)
//            {
//                ;
            }
//        }//end main loop
        System.out.println("Exited cleanly");
    }//main

    private static void waitForPeriod() throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1/60.0)
        {
            Thread.sleep(1);
        }
    }

}//class
