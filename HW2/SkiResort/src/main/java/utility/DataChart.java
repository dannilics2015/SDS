package utility;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DataChart {

    private String application_title;
    private String xAxisLable;
    private String yAxisLable;
    private int imageHeight;
    private int imageWidth;
    private String outputFileName;
    private Map<Long, Double> data;

    public DataChart(String application_title, String xAxisLable, String yAxisLable, int imageHeight, int imageWidth, String outputFileName, Map<Long, Double> data) {
        this.application_title = application_title;
        this.xAxisLable = xAxisLable;
        this.yAxisLable = yAxisLable;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.outputFileName = outputFileName;
        this.data = data;
    }

    public void createChart() throws IOException {
        JFreeChart lineChartObject = ChartFactory.createXYLineChart(application_title,
                xAxisLable, yAxisLable, createDataset(data),
                PlotOrientation.VERTICAL, true, true, false);
        File lineChart = new File( outputFileName );
        ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, imageWidth ,imageHeight);
    }

    private XYSeriesCollection createDataset(Map<Long, Double> latencies) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xySeries = new XYSeries("latency");
        latencies.forEach((Long key, Double value) ->
                xySeries.add(key, value));
        dataset.addSeries(xySeries);
        return dataset;
    }
}
