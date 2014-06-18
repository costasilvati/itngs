/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itngs_mvc.model.bo;

import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

/**
 * @author juliana
 */
public class ChartBO extends Frame {

    private ArrayList<Double> array = new ArrayList<>();
    private final String title;
    private final int type;

    /**
     * Initialize the object
     *
     * @param array Values found in statiscs files
     * @param type chart model --- 0 to XY line chart --- 1 to bar chart
     * @param title name of Chart
     */
    public ChartBO(ArrayList<Double> array, int type, String title) {
        this.array = array;
        this.type = type;
        this.title = title;
    }

    public ArrayList<Double> getArray() {
        return array;
    }

    public void setArray(ArrayList<Double> array) {
        this.array = array;
    }

    public void showChart(String nameX, String nameY) {

        CategoryDataset dataset = null;
        XYSeriesCollection xyDataset = null;
        JFreeChart chart;
        JFrame chartwindow = new JFrame(title);
        if (type == 0) {//Line Chart

            xyDataset = createDatasetXY();
            chart = createChart(title, nameX, nameY, dataset, xyDataset, null);
            XYPlot plotXY = chart.getXYPlot();
            plotXY.setBackgroundPaint(new Color(240, 248, 255));//WHITE);
            plotXY.setDomainGridlinesVisible(true);
            plotXY.setRangeGridlinePaint(new Color(128, 128, 128));//black);

            //plotXY.setRenderer(renderer);
        } else if (type == 1) {
            dataset = createDataset();
            chart = createChart(title, nameX, nameY, dataset, xyDataset, null);
            RefineryUtilities.positionFrameOnScreen(chartwindow, 50, 25);
            chart.setBackgroundPaint(WHITE);
        } else {//Pie chart
            DefaultPieDataset pie = new DefaultPieDataset();

            pie.setValue(nameX, array.get(0));
            pie.setValue(nameY, array.get(1));
            RefineryUtilities.centerFrameOnScreen(chartwindow);
            chart = createChart(title, nameX, nameY, dataset, xyDataset, pie);
            chart.setBackgroundPaint(WHITE);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(600, 370));
        chartPanel.setVisible(true);

        chartwindow.setContentPane(chartPanel);
        chartwindow.pack();

        chartwindow.setVisible(true);
    }

    /**
     * Create a dataset to bar chart
     *
     * @return
     */
    private CategoryDataset createDataset() {
        String[] category;
        String[] series;
        Double[] values;

        int limit;
        if (title.contains("Quality")) {
            limit = 40;
        } else {
            limit = 90;
        }

        category = makeColumnKey(limit);
        series = makeRowKey(limit);
        values = makeValues(limit);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int incr = 10;
        int j = 0;
        int count = incr;

        for (int i = 0; i < limit; i++) {
            dataset.addValue(values[i], series[i], category[j]);
            if (i >= count) {
                j++;
                count = count + incr;
            }
        }
        return dataset;

    }

    /**
     * Create a dataset to XY line chart
     *
     * @param title
     * @return
     */
    private XYSeriesCollection createDatasetXY() {
        int[] series;
        Double[] values;

        int limit;
        if (title.contains("Quality")) {
            limit = 40;
        } else {
            limit = 90;
        }

        series = makeRowKeysXY(limit);
        values = makeValues(limit);
        XYSeries seriesxy = new XYSeries("Nº of sequences");

        for (int i = 0; i < limit; i++) {
            seriesxy.add(series[i], values[i]);

        }
        XYSeriesCollection datasetXY = new XYSeriesCollection();
        datasetXY.addSeries(seriesxy);

        return datasetXY;

    }

    /**
     * Assemble the keys columns values
     *
     * @param limit maximum value of chart
     * @return array with keys
     */
    public String[] makeColumnKey(int limit) {
        int incr = 10;

        String[] category = new String[limit / incr];

        int j = 0;
        for (int i = 0; i < limit / incr; i++) {
            category[i] = j + "";
            j = j + incr;
        }
        return category;
    }

    /**
     * Assemble the keys lines values
     *
     * @param limit maximum value of chart
     * @return array with keys
     */
    public String[] makeRowKey(int limit) {
        String series[] = new String[limit];
        for (int i = 0; i < limit; i++) {
            series[i] = i + "";
        }

        return series;
    }

    /**
     * Assemble the keys lines values to Line Charts
     *
     * @param limit maximum value of chart
     * @return array with keys
     */
    public int[] makeRowKeysXY(int limit) {
        int series[] = new int[limit];
        for (int i = 0; i < limit; i++) {
            series[i] = i;
        }

        return series;
    }

    /**
     * Initialize all positions of array with 0,0 and count equals values
     *
     * @param limit maximum value of chart
     * @return array with keys
     */
    public Double[] makeValues(int limit) {
        Double vals[] = new Double[limit];

        for (int i = 0; i < limit; i++) {
            vals[i] = 0.0;
        }

        for (int j = 0; j < array.size(); j++) {

            for (int i = 0; i < limit; i++) {

                if (array.get(j).intValue() == i) {
                    vals[i] = vals[i] + 1.0;
                }

            }

        }
        return vals;
    }

    /**
     * Create a object chart with the data received
     *
     * @param title Name of chart
     * @param nameX specific name of x axis
     * @param nameY specific name of y axis
     * @param dataset cordinates (bar chart)
     * @param xyDataset cordinates (XY line chart)
     * @return a complete object chart
     */
    private JFreeChart createChart(String title, String nameX, String nameY,
            CategoryDataset dataset, XYSeriesCollection xyDataset, PieDataset pie) {

        JFreeChart chart = null;

        if (type == 0) {
            chart = ChartFactory.createXYLineChart(
                    title, // chart title
                    nameX, // domain axis label
                    nameY,
                    xyDataset, // data
                    PlotOrientation.VERTICAL, // orientation
                    true, // include legend
                    true, // tooltips?
                    false // URLs?
            );

        } else if (type == 1) {
            // create the chart...
            chart = ChartFactory.createBarChart3D(
                    title, // chart title
                    nameX, // domain axis label
                    nameY, // range axis label
                    dataset, // data
                    PlotOrientation.VERTICAL, // orientation
                    false, // include legend
                    true, // tooltips?
                    false // URLs?
            );

        } else {
            // create the chart...
            chart = ChartFactory.createPieChart(
                    title, // chart title
                    pie, // data
                    true, // no legend
                    true, // tooltips
                    false // no URL generation
            );

        }
        return chart;
    }
}
