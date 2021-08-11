import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;

public class StockDataTest
{
    private StockData stock1;
    private StockData stock2;
    private StockData stock3;

    @Before
    public void setUp() throws Exception
    {
        stock1 = new StockData("Test 1", "test1");
        stock2 = new StockData("Test 2", "test2");
        stock3 = new StockData("Test 3", "test3");
    }

    @Test
    public void testFieldNames()
    {
        Class c = StockData.class;
        Field[] fields = c .getDeclaredFields();
        assertTrue("You do not have the correct field names. Are they in the correct order?",
            fields[0].getName().equals("stockName") && fields[1].getName().equals("stockSymbol")
            && fields[2].getName().equals("openings") && fields[3].getName().equals("closings")
            && fields[4].getName().equals("dates"));
    }

    @Test
    public void testFieldDataTypes()
    {
        Class c = StockData.class;
        Field[] fields = c .getDeclaredFields();
        assertTrue(fields[2].getType().getCanonicalName().equals("double[]"));
        assertTrue(fields[3].getType().getCanonicalName().equals("double[]"));
        assertTrue(fields[4].getType().getCanonicalName().equals("java.lang.String[]"));
    }

    @Test
    public void testGetStockName()
    {
        assertEquals("Test 1", stock1.getStockName());
        assertEquals("Test 2", stock2.getStockName());
        assertEquals("Test 3", stock3.getStockName());
    }

    @Test
    public void testGetStockSymbol()
    {
        assertEquals("test1", stock1.getStockSymbol());
        assertEquals("test2", stock2.getStockSymbol());
        assertEquals("test3", stock3.getStockSymbol());
    }

    @Test
    public void testGetClosings()
    {
        double[] test1Key = {12.1, 11.1, 14.1, 15.1, 16.1, 19.3, 16.6, 16.1, 15.6, 15.1, 14.6};
        assertArrayEquals(test1Key, stock1.getClosings(), 0.0);
        double[] test2Key = {101.0, 102.7, 103.4};
        assertArrayEquals(test2Key, stock2.getClosings(), 0.0);
        double[] test3Key = {771.22998};
        assertArrayEquals(test3Key, stock3.getClosings(), 0.0);
    }

    @Test
    public void testGetOpenings()
    {
        double[] test1Key = {10, 11, 12, 13, 14, 15, 14.5, 14, 13.5, 13, 12.5};
        assertArrayEquals(test1Key, stock1.getOpenings(), 0.0);
        double[] test2Key = {100, 101.2, 103};
        assertArrayEquals(test2Key, stock2.getOpenings(), 0.0);
        double[] test3Key = {766.919983};
        assertArrayEquals(test3Key, stock3.getOpenings(), 0.0);
    }

    @Test
    public void testGetDates()
    {
        String[] test1Key = {"9/12/17","9/13/17","9/14/17","9/15/17","9/16/17","9/17/17","9/18/17","9/19/17","9/20/17","9/21/17","9/22/17"};
        assertArrayEquals(test1Key, stock1.getDates());
        String[] test2Key = {"1/1/99", "1/2/99", "1/3/99"};
        assertArrayEquals(test2Key, stock2.getDates());
        String[] test3Key = {"1977-10-19"};
        assertArrayEquals(test3Key, stock3.getDates());
    }

    @Test
    public void testGetMax()
    {
        assertEquals(19.3, stock1.getMax(), 0.0);
        assertEquals(103.4, stock2.getMax(), 0.0);
        assertEquals(771.22998, stock3.getMax(), 0.0);
    }

    @Test
    public void testGetMin()
    {
        assertEquals(11.1, stock1.getMin(), 0.0);
        assertEquals(101, stock2.getMin(), 0.0);
        assertEquals(771.22998, stock3.getMin(), 0.0);
    }

    @Test
    public void testGetPercentGrowth()
    {
        assertEquals(20.66115702, stock1.getPercentGrowth(), 0.01);
        assertEquals(2.376237624, stock2.getPercentGrowth(), 0.01);
        assertEquals(0.0, stock3.getPercentGrowth(), 0.01);
    }

    @Test
    public void testGetWorstDay()
    {
        assertEquals("9/13/17",stock1.getWorstDay());
        assertEquals("1/3/99", stock2.getWorstDay());
        assertEquals("1977-10-19", stock3.getWorstDay());
    }

    @Test
    public void testGetBestDay()
    {
        assertEquals("9/17/17",stock1.getBestDay());
        assertEquals("1/2/99", stock2.getBestDay());
        assertEquals("1977-10-19", stock3.getBestDay());

    }

    @Test
    public void testGetChangeInValue()
    {
        assertEquals(-1.0, stock1.changeInValue("9/12/17", "9/13/17"),0.001);
        assertEquals(1.7, stock2.changeInValue("1/1/99", "1/2/99"),0.001);
        assertEquals(0.0, stock3.changeInValue("1977-10-19","1977-10-19"), 0.0);

    }

    @Test
    public void testToString()
    {   String test2String = "Test 2: test2\n" +
            "Date\tOpen\tClose\n" +
            "1/1/99\t100.0\t101.0\n" +
            "1/2/99\t101.2\t102.7\n" +
            "1/3/99\t103.0\t103.4\n";
        assertEquals(test2String, stock2.toString());

        String test3String = "Test 3: test3\n" +
            "Date\tOpen\tClose\n" +
            "1977-10-19\t766.919983\t771.22998\n";
        assertEquals(test3String, stock3.toString());
    }

    /**********************************************************************************
     ***********************************************************************************
     *   Below are the unit tests for the Pro Level Extensions.
     *   Remove the block comment for each Unit Test that you'd like to run.
     ***********************************************************************************
     **********************************************************************************/


    @Test
    public void testMovingAveragesFields() throws IllegalAccessException
    {
        Class stock1Class = stock1.getClass();
        Field[] fields = stock1Class.getDeclaredFields();
        // Ensure that the fields movingAverages and period have been added.
        boolean foundMovingAverages = false, foundPeriod = false;
        for (Field field : fields)
        {
            field.setAccessible(true);
            if (field.getName().equals("movingAverages"))
                foundMovingAverages = true;
            else if(field.getName().equals("period"))
                foundPeriod = true;
        }
        if (!foundMovingAverages || !foundPeriod)
            fail("Missing correct fields with names of either movingAverages or period.");

        // Ensure that the fields are of the correct data type
        boolean correctDataTypeMovingAverages = false, correctDataTypePeriod = false, correctPeriod = false;
        for (Field field : fields)
        {
            if (field.getName().equals("movingAverages")) {
                if (field.getType().getCanonicalName().equals("double[]"))
                    correctDataTypeMovingAverages = true;
            }
            else if (field.getName().equals("period")) {
                if (field.getType().getCanonicalName().equals("int"))
                    correctDataTypePeriod = true;
                if (field.getInt(stock1) == 20)
                    correctPeriod = true;
            }
        }
        if (!correctDataTypeMovingAverages || !correctDataTypePeriod)
            fail("Incorrect data type for either movingAverages or period.");

        // Ensure that period is correctly assigned to 20
        assertTrue("Period should be assigned a value of 20 in the constructor.", correctPeriod);

    }

    @Test
    public void testSetMovingAverages() throws IllegalAccessException, NoSuchFieldException {
        Class stock1Class = stock1.getClass();
        Field[] stock1Fields = stock1Class.getDeclaredFields();
        for (Field field : stock1Fields)
            field.setAccessible(true);

        // Change the period for easy-to-write moving averages, N = 3
        stock1Fields[6].setInt(stock1, 3);
        // Ensure that the moving averages array is long enough
        stock1.setMovingAverages();
        Field movingAverages = stock1Fields[5];
        double[] studentAverages1 = (double[]) movingAverages.get(stock1);
        double[] answer1 = {12.433, 13.433, 15.1, 16.833, 17.333, 17.333, 16.1, 15.6};
        assertTrue("The moving averages array length should be smaller than the closing prices array.", answer1.length == studentAverages1.length);
        assertArrayEquals(answer1, studentAverages1, 0.01);

        // Change the period for easy-to-write moving averages, N = 5
        stock1Fields[6].setInt(stock1, 5);
        // Ensure that the moving averages array is long enough
        stock1.setMovingAverages();
        movingAverages = stock1Fields[5];
        double[] studentAverages2 = (double[]) movingAverages.get(stock1);
        double[] answer2 = {13.7, 15.14, 16.24, 16.64, 16.74, 16.54};
        assertTrue("The moving averages array length should be smaller than the closing prices array.", answer2.length == studentAverages2.length);
        assertArrayEquals(answer2, studentAverages2, 0.01);

        // Change the period for easy-to-write moving averages
        stock1Fields[6].setInt(stock1, 11);
        // Ensure that the moving averages array is long enough
        stock1.setMovingAverages();
        movingAverages = stock1Fields[5];
        double[] studentAverages3 = (double[]) movingAverages.get(stock1);
        double[] answer3 = {};
        assertTrue("The moving averages array length should be smaller than the closing prices array.", answer3.length == studentAverages3.length);
        assertArrayEquals(answer3, studentAverages3, 0.01);

        /////////////   Check Second Stock /////////////
        Class stock2Class = stock2.getClass();
        Field[] stock2Fields = stock2Class.getDeclaredFields();
        for (Field field : stock2Fields)
            field.setAccessible(true);

        // Case when N = 1
        stock2Fields[6].setInt(stock2, 1);
        stock2.setMovingAverages();
        movingAverages = stock2Fields[5];
        double[] studentAverages4 = (double[]) movingAverages.get(stock2);
        double[] answer4 = {101, 102.7};
        assertArrayEquals(answer4, studentAverages4, 0.01);

    }

    
    @Test
    public void testStandardDeviation()
    {
        double[] sample1 = {2.3, 3.4, 4.8, 7.6, 9.2, 5.3};
        double[] sample2 = {27.3, 42.6, 137.8};
        double[] sample3 = {1732, 1588, 2146, 1987, 2564};

        assertEquals(2.5788, stock1.stdDev(sample1, 0, sample1.length), 0.01);
        assertEquals(2.6299, stock1.stdDev(sample1, 1, 5), 0.01);
        assertEquals(59.8712, stock1.stdDev(sample2, 0, sample2.length), 0.01);
        assertEquals(381.1466, stock1.stdDev(sample3, 0, sample3.length), 0.01);
    }

    @Test
    public void testBollingerBandFields()
    {
        Class c = StockData.class;
        Field[] fields = c.getDeclaredFields();
        boolean foundK = false, foundBollingerAbove = false, foundBollingerBelow = false;
        boolean kDataType = false, bollingerAboveDataType = false, bollingerBelowDataType = false;

        for (Field field : fields)
        {
            switch (field.getName()) {
                case "K":
                foundK = true;
                if (field.getType().getCanonicalName().equals("int"))
                    kDataType = true;
                break;
                case "bollingerAbove":
                foundBollingerAbove = true;
                if (field.getType().getCanonicalName().equals("double[]"))
                    bollingerAboveDataType = true;
                break;
                case "bollingerBelow":
                foundBollingerBelow = true;
                if (field.getType().getCanonicalName().equals("double[]"))
                    bollingerBelowDataType = true;
                break;
            }
        }
        if (!foundK || !foundBollingerAbove || !foundBollingerBelow)
            fail("Missing fields with name of either K, bollingerAbove, or bollingerBelow.");
        assertTrue("K is the incorrect data type.", kDataType);
        assertTrue("bollingerAbove is the incorrect data type.", bollingerAboveDataType);
        assertTrue("bollingerBelow is the incorrect data type.", bollingerBelowDataType);
    }
    
    @Test
    public void testBollingerBandAbove() throws Exception
    {
        StockData bollingerAbove = new StockData("testBollinger", "testBollinger");
        double[] bollingerAboveKey = {204.6751705, 207.3465415, 209.4936893, 211.4983748, 214.249828, 218.2428189, 221.6996871, 223.5955033, 224.3500328, 225.0733397};
        bollingerAbove.setMovingAverages();
        bollingerAbove.setBollingerAbove();
        assertArrayEquals("Bollinger Band N Standard Deviations Above Moving Average is Incorrect.", bollingerAboveKey, bollingerAbove.getBollingerAbove(), 0.01);
    }
    
    @Test
    public void testBollingerBandBelow() throws Exception
    {
        StockData bollingerBelow = new StockData("testBollinger", "testBollinger");
        double[] bollingerBelowKey = {179.9508273, 179.7064552, 179.2123073, 178.7386216, 177.6561686, 176.0041781, 175.5643097, 176.1964934, 178.6229636, 181.4516571};
        bollingerBelow.setMovingAverages();
        bollingerBelow.setBollingerBelow();
        assertArrayEquals("Bollinger Band N Standard Deviations Below Moving Average is Incorrect.", bollingerBelowKey, bollingerBelow.getBollingerBelow(), 0.01);
    }
}
