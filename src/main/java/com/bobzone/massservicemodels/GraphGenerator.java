package com.bobzone.massservicemodels;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by epiobob on 2017-04-09.
 */
public class GraphGenerator {

    public void generateGraph(String fileNameToSave, String chartTitle, String xAxisTitle, String yAxisTitle, String series1, java.util.List<Double> counter, List<Double> data1) throws IOException {
        // Create Chart
        final XYChart chart = new XYChartBuilder().width(600).height(400).title(chartTitle).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();


        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        // Series
        chart.addSeries(series1, counter, data1);
//        chart.addSeries(series2, counter, data2);

        // Save it
        BitmapEncoder.saveBitmap(chart, "./" + fileNameToSave, BitmapEncoder.BitmapFormat.PNG);
    }

    public XYChart generateGraphToChart(String fileNameToSave, String chartTitle, String xAxisTitle, String yAxisTitle, String series1, java.util.List<Double> counter, List<Double> data1) throws IOException {
        // Create Chart
        final XYChart chart = new XYChartBuilder().width(600).height(400).title(chartTitle).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();


        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        // Series
        chart.addSeries(series1, counter, data1);

        return chart;
    }
}
