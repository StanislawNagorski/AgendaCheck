package org.AgendaCheck.ReportToXLSX;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.text.NumberFormat;
import java.util.List;

public class ChartCreator {

    public static JFreeChart createChart(List<Double> hoursShare, List<Double> turnoverShare) {

        // Create Category plot
        CategoryPlot plot = new CategoryPlot();

        // Add the first dataset and render as bar
        CategoryItemRenderer lineRenderer = new LineAndShapeRenderer();
        DefaultCategoryDataset hoursDataset = createDataSet(hoursShare, "Zaplanowane godziny");
        plot.setDataset(0, hoursDataset);
        plot.setRenderer(0, lineRenderer);

        // Add the second dataset and render as lines
        CategoryItemRenderer baRenderer = new BarRenderer();
        DefaultCategoryDataset turnoverDataset = createDataSet(turnoverShare, "Pilotowany obrót");
        plot.setDataset(1, turnoverDataset);
        plot.setRenderer(1, baRenderer);

        // Set Axis
        plot.setDomainAxis(new CategoryAxis("Dzień"));
        NumberAxis hoursAxis = new NumberAxis("Udział dnia w miesiącu");

        NumberFormat doublePercent = NumberFormat.getPercentInstance();
        doublePercent.setMinimumFractionDigits(1);
        doublePercent.setMaximumFractionDigits(1);
        hoursAxis.setNumberFormatOverride(doublePercent);
        plot.setRangeAxis(hoursAxis);

        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("Obecny rozkład godzin i obrotu");

        return chart;
    }

    private static DefaultCategoryDataset createDataSet(List<Double> hoursShare, String label) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < hoursShare.size() - 1; i++) {
            int day = i + 1;
            String rowValue = String.valueOf(day);
            double value = hoursShare.get(i);

            dataset.addValue(value, label, (rowValue));
        }
        return dataset;

    }

}

