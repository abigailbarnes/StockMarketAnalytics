import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.Port;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import static org.junit.Assert.*;

public class PortfolioTest
{
    private Portfolio emptyPort;
    private Portfolio singlePort;
    private Portfolio doublePort;
    private Portfolio triplePort;

    @Before
    public void setUp() throws Exception
    {
        emptyPort = new Portfolio("George P. Burdell");
        singlePort = new Portfolio("Singleton");
        singlePort.addNewStock("Portfolio Test 1", "portTest1");
        doublePort = new Portfolio("Doubley");
        doublePort.addNewStock("Portfolio Test 1", "portTest1");
        doublePort.addNewStock("Portfolio Test 2", "portTest2");
        triplePort = new Portfolio("Trip");
        triplePort.addNewStock("Portfolio Test 1", "portTest1");
        triplePort.addNewStock("Portfolio Test 2", "portTest2");
        triplePort.addNewStock("Portfolio Test 3", "portTest3");
    }

    @Test
    public void testFieldNames()
    {
        Class c = Portfolio.class;
        Field[] fields = c .getDeclaredFields();
        assertTrue("You do not have the correct field names. Are they in the correct order?",
                fields[0].getName().equals("stocks") && fields[1].getName().equals("investor")
        && fields[2].getName().equals("numOfStocks"));
    }

    @Test
    public void testFieldDataTypes()
    {
        Class c = Portfolio.class;
        Field[] fields = c .getDeclaredFields();
        assertTrue(fields[0].getType().getCanonicalName().equals("StockData[]"));
        assertTrue(fields[1].getType().getCanonicalName().equals("java.lang.String"));
        assertTrue(fields[2].getType().getCanonicalName().equals("int"));
    }

    @Test
    public void testPortfolioConstructor() throws IllegalAccessException
    {
        Portfolio porty = new Portfolio("Porty McPorterson");
        Class portyClass = porty.getClass();
        Field[] fields = portyClass.getDeclaredFields();
        for (Field f :fields)
            f.setAccessible(true);
        // Ensure that the stocks array is of size 3, and only contains null elements
        StockData[] stocks = (StockData[]) (fields[0].get(porty));
        assertTrue(stocks.length == 3);
        assertNull("A new portfolio should not contain any stocks yet. Did you accidentally assign one?", stocks[0]);
        assertNull("A new portfolio should not contain any stocks yet. Did you accidentally assign one?", stocks[1]);
        assertNull("A new portfolio should not contain any stocks yet. Did you accidentally assign one?", stocks[2]);
        // Ensure that the constructor takes one parameter and that it is used to assign the investor field
        Constructor portyConstructor = portyClass.getDeclaredConstructors()[0];
        assertTrue("The Portfolio constructor should only take 1 parameter.", 1 == portyConstructor.getParameterCount());
        assertTrue("The Portfolio constructor's parameter should be of type String", portyConstructor.getParameterTypes()[0].getName().equals("java.lang.String"));
        String investorName = fields[1].get(porty).toString();
        assertEquals("The parameter for the Portfolio constructor should assign the investor field.", "Porty McPorterson", investorName);
        // Ensure that the numOfStocks field is 0
        assertTrue("numOfStocks should be 0 constructing a new Portfolio.", 0 == fields[2].getInt(porty));
    }

    @Test
    public void testGetStocks()
    {
        assertTrue("A new portfolio should contain a StockData array of size 3.", emptyPort.getStocks().length == 3);
        assertEquals("A new empty portfolio should have 0 stocks and only contain null objects", null, emptyPort.getStocks()[0]);
        assertEquals("A new empty portfolio should have 0 stocks and only contain null objects", null, emptyPort.getStocks()[1]);
        assertEquals("A new empty portfolio should have 0 stocks and only contain null objects", null, emptyPort.getStocks()[2]);
    }

    
    @Test
    public void testGetInvestor()
    {
        assertEquals("getInvestor does not correctly return the investor field.", "George P. Burdell", emptyPort.getInvestor());
        assertEquals("getInvestor does not correctly return the investor field.", "Singleton", singlePort.getInvestor());
        assertEquals("getInvestor does not correctly return the investor field.", "Doubley", doublePort.getInvestor());
        assertEquals("getInvestor does not correctly return the investor field.", "Trip", triplePort.getInvestor());
    }

    @Test
    public void testGetNumOfStocks()
    {
        assertEquals("A new empty portfolio should not have any stocks.", 0, emptyPort.getNumOfStocks());
        assertEquals("After adding a single StockData object, the numOfStocks field should be 1.", 1, singlePort.getNumOfStocks());
        assertEquals("After adding two StockData objects, the numOfStocks field should be 2.", 2, doublePort.getNumOfStocks());
        assertEquals("After adding three StockData objects, the numOfStocks field should be 3.", 3, triplePort.getNumOfStocks());
    }

    @Test
    public void testGetStartingDay(){
        try{
            assertEquals("An empty portfolio does not have a starting day.", null, emptyPort.getStartingDay());
            assertEquals("Did not correctly return the first day of the portfolio.", "9/12/17", singlePort.getStartingDay());
            assertEquals("Did not correctly return the first day of the portfolio.", "9/12/17", doublePort.getStartingDay());
            assertEquals("Did not correctly return the first day of the portfolio.", "9/12/17", triplePort.getStartingDay());
        }
        catch(NullPointerException e){
            fail("Uh oh. You didn't handle the case of a null object in stocks and got a NullPointerException. " +
                    "You can't use object dot notation on a null object. Add a conditional, homes.");
        }
    }

    @Test
    public void testAddOneStock() throws Exception
    {
        emptyPort.addNewStock("Coca-Cola", "COKE");
        assertEquals("After adding a single stock to an empty portfolio, size should be 1.", 1, emptyPort.getNumOfStocks());
        assertEquals("When adding the first stock, it should be in the first spot of stocks.", "Coca-Cola", emptyPort.getStocks()[0].getStockName());
        assertTrue("After adding a single stock to an empty portfolio, the first element should no longer be null.", emptyPort.getStocks()[0] != null);
        assertTrue("After adding a single stock to an empty portfolio, the second element should still be null.", emptyPort.getStocks()[1] == null);
        assertTrue("After adding a single stock to an empty portfolio, the third element should still be null.", emptyPort.getStocks()[2] == null);
    }

    @Test
    public void testAddTwoStocks() throws Exception
    {
        emptyPort.addNewStock("Coca-Cola", "COKE");
        emptyPort.addNewStock("Pepsi", "PEP");
        assertEquals("After adding two stocks to an empty portfolio, size should be 2.", 2, emptyPort.getNumOfStocks());
        assertTrue("After adding two stocks to an empty portfolio, the first element should no longer be null.", emptyPort.getStocks()[0] != null);
        assertTrue("After adding two stocks to an empty portfolio, the second element should no longer be null.", emptyPort.getStocks()[1] != null);
        assertTrue("After adding a single stock to an empty portfolio, the third element should still be null.", emptyPort.getStocks()[2] == null);
    }

    @Test
    public void testAddThreeStocks() throws Exception
    {
        emptyPort.addNewStock("Coca-Cola", "COKE");
        emptyPort.addNewStock("Pepsi", "PEP");
        emptyPort.addNewStock("McDonalds", "MCD");
        assertEquals("After adding three stocks to an empty portfolio, size should be 3.", 3, emptyPort.getNumOfStocks());
        assertTrue("After adding three stocks to an empty portfolio, the first element should no longer be null.", emptyPort.getStocks()[0] != null);
        assertTrue("After adding three stocks to an empty portfolio, the second element should no longer be null.", emptyPort.getStocks()[1] != null);
        assertTrue("After adding three stocks to an empty portfolio, the third element should no longer be null.", emptyPort.getStocks()[2] != null);
    }
    /*
    @Test
    public void testAddFourStocks() throws Exception
    {
        emptyPort.addNewStock("Coca-Cola", "COKE");
        emptyPort.addNewStock("Pepsi", "PEP");
        emptyPort.addNewStock("McDonalds", "MCD");
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            emptyPort.addNewStock("Amazon", "AMZN");
            assertEquals("Incorrect print message. Did you follow the format correctly?","Portfolio is at capacity. Cannot add any more stocks.\n", outContent.toString());
            System.setOut(System.out);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail("Whoops. You generated an ArrayIndexOutOfBoundsException. An investor " +
                    "shouldn't be able to add another stock when the portfolio is full (3 stocks).");
        }
        catch (Exception e) {
                fail("Whoops. You generated an exception, but not an ArrayIndexOutOfBoundsException. How'd you do that?");
        }

    }
    */
    @Test
    public void testHasSameStartingDays1()
    {
        assertTrue("An empty portfolio should return true.", emptyPort.hasSameStartingDays());
        assertTrue("A portfolio of one stock has the same starting day.", singlePort.hasSameStartingDays());
        assertTrue("Failed for a portfolio with two identical starting dates.", doublePort.hasSameStartingDays());
        assertTrue("Failed for a portfolio with three identical starting dates.", triplePort.hasSameStartingDays());
    }

    @Test
    public void testHasSameStartingDays2()
    {     
        try {
            Portfolio differentDays = new Portfolio("Lord Vodlemort");
            differentDays.addNewStock("Portfolio Test 4", "portTest4");
            differentDays.addNewStock("Portfolio Test 5", "portTest5");
            assertFalse("Failed for a two-stock portfolio with two different starting dates.", differentDays.hasSameStartingDays());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Failed due to an exception being thrown.");
        }
    }

    @Test
    public void testHasSameStartingDays3()
    {
        try {
            Portfolio kindOfDifferentDays = new Portfolio("Albus Dumbloedore #RIP");
            kindOfDifferentDays.addNewStock("Portfolio Test 1", "portTest1");
            kindOfDifferentDays.addNewStock("Portfolio Test 2", "portTest2");
            kindOfDifferentDays.addNewStock("Portfolio Test 5", "portTest5");
            assertFalse("Failed for a portfolio with some starting dates the same, and some different.", kindOfDifferentDays.hasSameStartingDays());

            kindOfDifferentDays = new Portfolio("Albus Dumbledore #RIP");
            kindOfDifferentDays.addNewStock("Portfolio Test 5", "portTest5");
            kindOfDifferentDays.addNewStock("Portfolio Test 1", "portTest1");
            kindOfDifferentDays.addNewStock("Portfolio Test 2", "portTest2");
            assertFalse("Failed for a portfolio with some starting dates the same, and some different.", kindOfDifferentDays.hasSameStartingDays());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Failed due to an exception being thrown.");
        }
    }

    @Test
    public void testGetMostExpensiveStock()
    {
        try {
            assertEquals("An empty portfolio does not have a most expensive stock.", null, emptyPort.getMostExpensiveStock());
            assertEquals("A portfolio with only one stock has only one option.", "Portfolio Test 1 : $24.81283453", singlePort.getMostExpensiveStock());
            assertEquals("Incorrect stock returned for a portfolio with two stocks.", "Portfolio Test 2 : $39.2", doublePort.getMostExpensiveStock());
            assertEquals("Incorrect stock returned for a portfolio with three stocks.", "Portfolio Test 3 : $86.0", triplePort.getMostExpensiveStock());
        }
        catch (NullPointerException e){
            fail("Uh oh. You didn't handle the case of a null object in stocks and got a NullPointerException. " +
                    "You can't use object dot notation on a null object. Add a conditional, homes.");
        }
    }

    @Test
    public void testGetCheapestStock()
    {
        try {
            assertEquals("An empty portfolio does not have a least expensive stock.", null, emptyPort.getCheapestStock());
            assertEquals("A portfolio with only one stock has only one option.", "Portfolio Test 1 : $0.892305687", singlePort.getCheapestStock());
            assertEquals("Incorrect stock returned for a portfolio with two stocks.", "Portfolio Test 2 : $0.021109971", doublePort.getCheapestStock());
            assertEquals("Incorrect stock returned for a portfolio with three stocks.", "Portfolio Test 2 : $0.021109971", triplePort.getCheapestStock());
        }
        catch (NullPointerException e){
            fail("Uh oh. You didn't handle the case of a null object in stocks and got a NullPointerException. " +
                    "You can't use object dot notation on a null object. Add a conditional, homes.");
        }
    }

    @Test
    public void testGetBestGrowingStock()
    {
        try{
            assertEquals("An empty portfolio does not have a best growing stock.", null, emptyPort.getBestGrowingStock());
            assertEquals("Incorrect growth rate for a single stock portfolio. Is the String formatted correctly?" , "Portfolio Test 1 grew by 95.07926102040814%", singlePort.getBestGrowingStock());
            assertEquals("Incorrect growth rate for a double stock portfolio. Is the String formatted correctly?" , "Portfolio Test 1 grew by 95.07926102040814%", doublePort.getBestGrowingStock());
            assertEquals("Incorrect growth rate for a triple stock portfolio. Is the String formatted correctly?" , "Portfolio Test 1 grew by 95.07926102040814%", triplePort.getBestGrowingStock());
        }
        catch (NullPointerException e){
            fail("Uh oh. You didn't handle the case of a null object in stocks and got a NullPointerException. " +
                    "You can't use object dot notation on a null object. Add a conditional, homes.");
        }
    }

    @Test
    public void testGetWorstGrowingStock()
    {
        try{
            assertEquals("An empty portfolio does not have a worst growing stock.", null, emptyPort.getBestGrowingStock());
            assertEquals("Incorrect growth rate for a single stock-portfolio. Is the String formatted correctly?" , "Portfolio Test 1 grew by 95.07926102040814%", singlePort.getWorstGrowingStock());
            assertEquals("Incorrect growth rate for a double stock-portfolio. Is the String formatted correctly?" , "Portfolio Test 2 grew by -62.788857857142865%", doublePort.getWorstGrowingStock());
            assertEquals("Incorrect growth rate for a triple stock-portfolio. Is the String formatted correctly?" , "Portfolio Test 2 grew by -62.788857857142865%", triplePort.getWorstGrowingStock());
        }
        catch (NullPointerException e){
            fail("Uh oh. You didn't handle the case of a null object in stocks and got a NullPointerException. " +
                    "You can't use object dot notation on a null object. Add a conditional, homes.");
        }
    }

    @Test
    public void testToString() throws Exception
    {
        Portfolio doubleTest = new Portfolio("Alex");
        doubleTest.addNewStock("Portfolio Test 2", "portTest2");
        doubleTest.addNewStock("Portfolio Test 3", "portTest3");
        String doubleStringKey = "Alex's Portfolio\n" +
                "Number of Stocks: 2\n" +
                "Starting Date: 9/12/17\n" +
                "Portfolio Test 3 grew by 29.411764705882355%";

        String tripleStringKey = "Trip's Portfolio\n" +
                "Number of Stocks: 3\n" +
                "Starting Date: 9/12/17\n" +
                "Portfolio Test 1 grew by 95.07926102040814%";
        assertEquals("toString does not work as specified. Did you follow the formatting appropriately?", tripleStringKey, triplePort.toString());
    }

    /**********************************************************************************
    ***********************************************************************************
    *   Below are the unit tests for the Pro Level Extensions.
    *   Remove the block comment for each Unit Test that you'd like to run.
    ***********************************************************************************
    **********************************************************************************/

    //EXTENSION TEST: addNewStock DELETE THIS LINE *************************
    public void testAddNewStockExtension1() throws Exception{
        try{
            triplePort.addNewStock("Amazon", "AMZN");
            assertTrue("Adding a fourth stock should result in numOfStocks to become 4.", triplePort.getNumOfStocks() == 4);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail("Encountered an ArrayIndexOutOfBoundsException will attempting to add a 4th stock. How will you create a larger array?");
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Unexpected Exception encountered.");
        }
    }
    
    @Test
    public void testAddNewStockExtension2() throws Exception{
        try{
            triplePort.addNewStock("Amazon", "AMZN");
            assertTrue("Adding a fourth stock should result in numOfStocks to become 4.", triplePort.getNumOfStocks() == 4);
            triplePort.addNewStock("Coca-Cola", "COKE");
            assertTrue("Adding a fifth stock should result in numofStocks to become 5.", triplePort.getNumOfStocks() == 5);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail("Encountered an ArrayIndexOutOfBoundsException will attempting to add a 4th stock. How will you create a larger array?");
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Unexpected Exception encountered.");
        }
    }

    @Test
    public void testAddNewStockExtension3() throws Exception{
        try{
            for (int i = 0; i < 100; i++)
            {
                emptyPort.addNewStock("Portfolio Test 1", "portTest1");
            }
            assertTrue("Your portfolio was not able to handle a large number of additions.", emptyPort.getNumOfStocks() == 100);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail("Encountered an ArrayIndexOutOfBoundsException will attempting to add a 4th stock. How will you create a larger array?");
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Unexpected Exception encountered.");
        }
    }
    
}
