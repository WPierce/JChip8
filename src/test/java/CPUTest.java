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
    public void testRET_0x00EE()
    {
        //Jump
        int opcode = 0x2100;
        int PCBeforeExec = mCPU.getPC();
        mCPU.execute(opcode);

        //Return
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

    @Test
     public void testSE_opcode3xkk_whenValuesAreEqual()
{
    //Should skip one instruction
    mCPU.setRegister(0, 0x20);

    int opcode = 0x3020;
    mCPU.execute(opcode);
    assertEquals(4, mCPU.getPC());
}

    @Test
    public void testSE_opcode3xkk_whenValuesAreNotEqual()
    {
        //Should skip one instruction
        mCPU.setRegister(0, 0x20);

        int opcode = 0x3021;
        mCPU.execute(opcode);
        assertEquals(2, mCPU.getPC());
    }

    @Test
    public void testSNE_opcode4xkk_whenValuesAreEqual()
    {
        //Should skip one instruction
        mCPU.setRegister(0, 0x20);

        int opcode = 0x4020;
        mCPU.execute(opcode);
        assertEquals(2, mCPU.getPC());
    }

    @Test
    public void testSNE_opcode4xkk_whenValuesAreNotEqual()
    {
        //Should skip one instruction
        mCPU.setRegister(0, 0x20);

        int opcode = 0x4021;
        mCPU.execute(opcode);
        assertEquals(4, mCPU.getPC());
    }

    @Test
    public void testSE_Regs_opcode5xy0_whenValuesAreEqual()
    {
        //Should skip one instruction
        mCPU.setRegister(0, 0x20);
        mCPU.setRegister(1, 0x20);

        int opcode = 0x5010;
        mCPU.execute(opcode);
        assertEquals(4, mCPU.getPC());
    }

    @Test
    public void testSE_Regs_opcode5xy0_whenValuesAreNotEqual()
    {
        //Should skip one instruction
        mCPU.setRegister(0, 0x20);
        mCPU.setRegister(1, 0x21);

        int opcode = 0x5010;
        mCPU.execute(opcode);
        assertEquals(2, mCPU.getPC());
    }

    @Test
    public void test_LD_VX_opcode6xkk()
    {
        int opcode = 0x6009;
        mCPU.execute(opcode);
        assertEquals(0x09, mCPU.getRegister(0));
    }

    @Test
    public void test_ADD_VX_byte_opcode7xkk_initiallyEmptyReg()
    {
        int opcode = 0x7009;
        mCPU.execute(opcode);
        assertEquals(0x09, mCPU.getRegister(0));
    }

    @Test
    public void test_ADD_VX_byte_opcode7xkk_initiallyNonEmptyReg()
    {
        mCPU.setRegister(0, 0x02);
        int opcode = 0x7007;
        mCPU.execute(opcode);
        assertEquals(0x09, mCPU.getRegister(0));
    }

    @Test
    public void test_ADD_VX_byte_opcode7xkk_WithOverflow()
    {
        mCPU.setRegister(0, 0xFE);
        int opcode = 0x7003;
        mCPU.execute(opcode);
        assertEquals(0x01, mCPU.getRegister(0));
    }

    @Test
    public void test_LD_VX_VY_opcode8xy0()
    {
        mCPU.setRegister(0, 0x02);
        mCPU.setRegister(1, 0x03);
        int opcode = 0x8010;
        mCPU.execute(opcode);
        assertEquals(0x03, mCPU.getRegister(0));
    }

    @Test
    public void test_OR_VX_VY_opcode8xy1()
    {
        mCPU.setRegister(0, 0x13);
        mCPU.setRegister(1, 0x04);
        int opcode = 0x8011;
        mCPU.execute(opcode);
        assertEquals(0x17, mCPU.getRegister(0));
    }

    @Test
    public void test_AND_VX_VY_opcode8xy2()
    {
        mCPU.setRegister(0, 0x15);
        mCPU.setRegister(1, 0x16);
        int opcode = 0x8012;
        mCPU.execute(opcode);
        assertEquals(0x14, mCPU.getRegister(0));
    }

}