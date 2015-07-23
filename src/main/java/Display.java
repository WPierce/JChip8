import java.util.Arrays;

public class Display
{

    private int dispX;
    private int dispY;
    private int[][] screen;

    public Display(int dispX, int dispY)
    {
        this.dispX = dispX;
        this.dispY = dispY;
        screen = new int[dispX][dispY];

        clearScreen();
    }//constructor

    public int getDispX()
    {
        return dispX;
    }

    public void setDispX(int dispX)
    {
        this.dispX = dispX;
    }

    public int getDispY()
    {
        return dispY;
    }

    ;

    public void setDispY(int dispY)
    {
        this.dispY = dispY;
    }

    public void clearScreen()
    {
        for (int i = 0; i < dispX; i++)
        {
            for (int j = 0; j < dispY; j++)
            {
                screen[i][j] = 0;
            }
        }
    }//clearScreen

    public int getValue(int locationX, int locationY)
    {
        return screen[locationX][locationY];
    }//getValue

    public void setValue(int locationX, int locationY, int value)
    {
        screen[locationX][locationY] = value;
    }//setValue

    @Override
    public String toString()
    {
        return "Display [screen=" + Arrays.toString(screen) + "]";
    }

}//class
