/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package itngs_mvc.model.vo;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author juliana
 */
public class ChartVO {
        //Line Charts
    private XYSeriesCollection xyDataset;
    private XYPlot plotXY;
        
    //Bars charts
    private CategoryDataset dataset;
    
    //Titles to all charts
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private String title;
    private String nameX;
    private String nameY;
}
