import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StockDataVisualizer extends Application{

    private static StockData stock;
    
    public static void main(String[] args) throws Exception
    {
        stock = new StockData("Urban Outfitters", "URBN");
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception{
        prepChart(stage);
    }

    private void prepChart(Stage stage) {
        stage.setTitle("Stock Market Analytics");
        // Prepare the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days Since " + stock.getDates()[0]);
        yAxis.setLabel("Closing Price ($)");
        // Create the chart object
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        // Format properties of the chart
        lineChart.setTitle(stock.getStockName() + " Closing Prices");
        lineChart.setCreateSymbols(false);

        // Obtain the data from the StockData object
        double[] closingPrices = stock.getClosings();
        double[] movingAverages = stock.getMovingAverages();
        double[] bollingerAbove = stock.getBollingerAbove();
        double[] bollingerBelow = stock.getBollingerBelow();
        
        // Prepare a series to add the data to
        XYChart.Series closingSeries = new XYChart.Series();
        closingSeries.setName(stock.getStockName());
        for (int i = 0; i < closingPrices.length; i++)
            closingSeries.getData().add(new XYChart.Data<>(i, closingPrices[i]));
            
        // Prepare a series to add the data to
        XYChart.Series averageSeries = new XYChart.Series();
        averageSeries.setName("Moving Average");
        for (int i = 0; i < movingAverages.length; i++)
            averageSeries.getData().add(new XYChart.Data<>(i + 20, movingAverages[i]));

            
        XYChart.Series bollAboveSeries = new XYChart.Series();
        bollAboveSeries.setName("Bollinger Above");
        for (int i = 0; i < bollingerAbove.length; i++)
            bollAboveSeries.getData().add(new XYChart.Data<>(i + 20, bollingerAbove[i]));
            
        XYChart.Series bollBelowSeries = new XYChart.Series();
        bollBelowSeries.setName("Bollinger Below");
        for (int i = 0; i < bollingerBelow.length; i++)
            bollBelowSeries.getData().add(new XYChart.Data<>(i + 20, bollingerBelow[i]));
            
        // Prepare the Scene
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(closingSeries);
        lineChart.getData().add(averageSeries); 
        lineChart.getData().add(bollAboveSeries);
        lineChart.getData().add(bollBelowSeries);
        stage.setScene(scene);
        stage.show();

    }
}
