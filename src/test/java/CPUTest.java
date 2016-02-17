import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author William Pierce
 */
public class CPUTest
{
    private CPU mCPU;

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

    @Before
    public void setUp() throws Exception
    {
        mCPU = new CPU();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    //Getter/Setter tests.

    @Test (expected = IllegalArgumentException.class)
    public void testGettingRegisterLessThanZero_ThrowsException()
    {
        mCPU.getRegister(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGettingRegisterNumberHigherThanMaxRegs_ThrowsException()
    {
        mCPU.getRegister(20);
    }

    @Test (expected = IllegalArgumentException.class)
    public void settingRegisterWithValueOverByte_ThrowsException()
    {
        mCPU.setRegister(0, 256);
    }

    @Test (expected = IllegalArgumentException.class)
    public void settingRegisterNumberHigherThanMaxRegs_ThrowsException()
    {
        mCPU.setRegister(20, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSettingRegisterLessThanZero_ThrowsException()
    {
        mCPU.setRegister(-1, 1);
    }

    //Opcode tests

    @Test
    public void testJMP_0x1nnn()
    {
        int opcode = 0x1123;
        mCPU.execute(opcode);
        assertEquals(0x123, mCPU.getPC());
    }

    @Test
    public void testCALL_0x2nnn()
    {
        int opcode = 0x2100;
        int PCBeforeExec = mCPU.getPC();
        mCPU.execute(opcode);
        assertEquals(PCBeforeExec, mCPU.stackPeek());
    }

    @Test
    public void testRET_0x2nnn()
    {
        int opcode = 0x2100;
        int PCBeforeExec = mCPU.getPC();
        mCPU.execute(opcode);

        opcode = 0x00EE;
        mCPU.execute(opcode);
        assertEquals(PCBeforeExec, mCPU.getPC());
    }

    @Test
    public void testAdd_opcode8xy4()
    {
        int opcode = 0x8024;
        mCPU.setRegister(2, 2);
        mCPU.execute(opcode);
        assertEquals(2, mCPU.getRegister(0));
    }

    @Test
    public void testAddWithOverflow_opcode8xy4()
    {
        int opcode = 0x8024;
        mCPU.setRegister(0, 1);
        mCPU.setRegister(2, 255);
        mCPU.execute(opcode);
        assertEquals(0, mCPU.getRegister(0));
        assertEquals(1, mCPU.getRegister(0xF));
    }
}