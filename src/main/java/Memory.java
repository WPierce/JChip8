public class Memory
{

    //public int numKeys = 16;
    private final int MAX_MEM = 0xFFF;
    private final boolean[] keyboard = new boolean[16];
    private final int[] RAM = new int[MAX_MEM];

    //constructor
    public Memory()
    {
        for (int i = 0; i < keyboard.length; i++)
        {
            RAM[i] = 0; //clear all memory
        }

        for (int i = 0; i < keyboard.length; i++)
        {
            keyboard[i] = false; //reset keyboard keys
        }

    }//constructor end

    public void setKeyPressed(int key, boolean value)
    {
        keyboard[key] = value;
    }

    public boolean getKeyPressed(int key)
    {
        return keyboard[key];
    }

    @Override
    public String toString()
    {
        StringBuilder retString = new StringBuilder();
        for (int i = 0; i < MAX_MEM; i++)
        {
            retString.append(Integer.toHexString(i));
            retString.append("\t");
            retString.append(Integer.toHexString(RAM[i]));
            retString.append("\n");
        }
        return retString.toString();
    }

    public int setValue(int location, int value)
    {
        if (location > MAX_MEM)
        {
            return 0;
        }
        RAM[location] = value;
        return 1;
    }

    public void setValue(int location, int[] values, int length)
    {
        for (int i = 0; i < length; i++)
        {
            RAM[location + i] = values[i];
        }
    }

    public int getValue(int location)
    {
        return RAM[location];
    }

}//class
