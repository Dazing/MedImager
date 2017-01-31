package drymouthx.model.jfreechart;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartPanel;


/**
* A simple introduction to using JFreeChart. This demo is described in the
* JFreeChart Developer Guide.
*/

public class CreateCharts {
    /**
    * The starting point for the demo.
    *
    * @param args ignored.
    */
            public CreateCharts() {}

            public ChartPanel drawChartPie(){


                // create a dataset...
                DefaultPieDataset data = new DefaultPieDataset();
                data.setValue("Sjögrens Syndrom", 43.2);
                data.setValue("HIV", 27.9);
                data.setValue("Other", 79.5);

                // create a CreateCharts...
                JFreeChart chart = ChartFactory.createPieChart3D(
                "Diseases - induced dry mouth syptoms",
                data,
                true, // legend?
                true, // tooltips?
                false // URLs?
                );

                //Man skall tydligen skapa en ChartPanel som är en jcomponent för att "visa" i GUI
               ChartPanel cpanel = new ChartPanel(chart);
               cpanel.setPreferredSize(new java.awt.Dimension(350, 185));
               //cpanel.setVisible(true);
                return cpanel;
            }

}
