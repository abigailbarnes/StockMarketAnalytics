import javafx.application.Application;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class PortfolioVisualizer extends Application {

    static Portfolio portfolio;

    public static void visualize(Portfolio port)
    {
        portfolio = port;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Portfolio Visualizer");
        // Prepare the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Normalized Closing Price ($)");
        xAxis.setLabel("Number of Days Since " + portfolio.getStartingDay());
        // Create the chart object
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        // Format properties of the chart
        lineChart.setTitle(portfolio.getInvestor() + "'s Portfolio");
        lineChart.setCreateSymbols(false);
        Scene scene = new Scene(lineChart, 1000, 800);
        populateData(lineChart);
        stage.setScene(scene);
        stage.show();
    }

    public void populateData(LineChart<Number, Number> lineChart)
    {

        for (StockData s : portfolio.getStocks()){
            XYChart.Series series = new XYChart.Series();
            s.normalizeClosings();
            double[] normalizedClosings = s.getNormalizedClosings();
            for (int i = 0; i < normalizedClosings.length; i++){
                series.getData().add(new XYChart.Data<>(i, normalizedClosings[i]));
            }
            series.setName(s.getStockName());
            lineChart.getData().add(series);
        }

    }
}
