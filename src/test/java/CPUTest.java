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

    @Test (expected = IllegalArgumentException.class)
    public void testGettingRegisterLessThanZeroThrowsException()
    {
        mCPU.getRegister(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGettingRegisterNumberHigherThanMaxRegsThrowsException()
    {
        mCPU.getRegister(20);
    }

    @Test
    public void settingRegisterWithValueOverByte_ThrowsException()
    {

    }

    @Test
    public void testAdd_opcode8xy4()
    {
        int opcode = 0x8024;
        mCPU.setRegister(2, 2);
        mCPU.execute(opcode);
        assertEquals(2, mCPU.getRegisters()[0]);
    }

    @Test
    public void testAddWithOverflow_opcode8xy4()
    {
        int opcode = 0x8024;
        mCPU.setRegister(0, 1);
        mCPU.setRegister(2, 255);
        mCPU.execute(opcode);
        assertEquals(0, mCPU.getRegisters()[0]);
        assertEquals(1, mCPU.getRegisters()[0xF]);
    }
}