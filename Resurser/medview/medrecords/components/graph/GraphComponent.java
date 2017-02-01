/*
 * GraphComponent.java
 *
 * Created on den 8 juni 2005, 12:22
 *
 * $Id: GraphComponent.java,v 1.4 2006/09/13 21:58:39 oloft Exp $
 *
 * $Log: GraphComponent.java,v $
 * Revision 1.4  2006/09/13 21:58:39  oloft
 * Fix so that labelös for missing values become visible
 *
 * Revision 1.3  2005/06/09 15:05:55  erichson
 * Labels are now displayed in the graph (instead of attribute names)
 *
 * Revision 1.2  2005/06/08 13:54:44  erichson
 * Changed parsing from integer to double (for vas-scales)
 *
 * Revision 1.1  2005/06/08 12:14:34  erichson
 * First check-in.
 *
 */

package medview.medrecords.components.graph;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import medview.medrecords.data.graph.*;
import medview.datahandling.examination.ExaminationValueContainer;

/* JFreeChart stuff */
import com.jrefinery.data.*;
import com.jrefinery.chart.tooltips.*;
import com.jrefinery.chart.entity.*;
import com.jrefinery.chart.*;

/**
 * Component that creates a graph from a GraphInfo and an ExaminationValueContainer.
 *
 * @author  erichson
 */
public class GraphComponent extends JPanel
{
    
    private GraphInfo graphInfo;
    /** Creates a new instance of GraphComponent */
    public GraphComponent(GraphInfo graphInfo, ExaminationValueContainer evc) 
    {
        super();
        this.graphInfo = graphInfo;
        
        setLayout(new BorderLayout());
        
        JFreeChart chart = createChart(evc);        
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
    
    private JFreeChart createChart(ExaminationValueContainer evc) 
    {
        
        // Create dataset from graphInfo and evc        
        
        String[] attributes = graphInfo.getAttributes();
        
        Vector labelsV = new Vector();
        Vector valuesV = new Vector();
        
        for (int i = 0; i < attributes.length; i++)
        {
           Double parsedDouble;
           try {
                String[] termValues = evc.getValues(attributes[i]);

                if (termValues.length > 0)
                {
                    parsedDouble = new Double(Double.parseDouble(termValues[0]));                                   
                }
                else
                {
					parsedDouble = new Double(0); // Missing terms should be visible in the graph - OT
                    //parsedDouble = null;
                }       
            } catch (Exception nfe) // NumberFormatException or NoSuchTermException
            {
                // parsedDouble = null;
				parsedDouble = new Double(0); // Missing terms should be visible in the graph with value 0, fix Numberformat-case? - OT
            }
           
           if (parsedDouble != null)
           {
			   // System.out.println(graphInfo.getAttributeLabel(attributes[i]) + " " + parsedDouble);
               labelsV.add( graphInfo.getAttributeLabel(attributes[i]));
               valuesV.add(parsedDouble);
           }
        }        
           
        // Check the list and remove all terms that we could not determine values for
        
        Vector v = new Vector();
                        
        String[] labels = new String[labelsV.size()];        
        Double[] values = new Double[labels.length];
        
        labels = (String[]) labelsV.toArray(labels);
        values = (Double[]) valuesV.toArray(values);
                        
        Double[][] dataArray = { values }; // Series array, but we only have one series
        
        String[] seriesNames = { "Series 1" };        
        
        DefaultCategoryDataset cDataSet = new DefaultCategoryDataset(seriesNames, labels, dataArray);
                
        // Create the chart        
                
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(new StandardEntityCollection());
        CategoryItemRenderer renderer = new HorizontalBarRenderer(new StandardCategoryToolTipGenerator());        
        
        Axis xAxis = createXAxis();
        Axis yAxis = createYAxis();                
        
        // horizontal: yAxis is domain
        CategoryPlot plot = new HorizontalCategoryPlot(cDataSet,
                                        (CategoryAxis) yAxis, 
                                        (ValueAxis) xAxis,
                                        renderer);
        
        JFreeChart chart = new JFreeChart(plot);
        return chart;
    }
    
    
    private boolean isHorizontal()
    { return true; }
    
    /**
     * Creates the x-axis.
     * 
     * @return The new x-axis.
     */
    private Axis createXAxis() {
        Axis xAxis;
        if (isHorizontal()) {
            xAxis = createNumberXAxis(); // horizontalnumberaxis (range)            
        } else { // vertical
            xAxis = createCategoryXAxis(); // domain (category)            
        }
        return xAxis;
    }
    
    /**
     * Creates the y-axis.
     *
     * @return The new y-axis.
     */
    private Axis createYAxis() {
        Axis yAxis;
        if (isHorizontal()) 
        {
            yAxis = createCategoryYAxis();
        } else {
            yAxis = createNumberYAxis();
        }
        return yAxis;
    }
    
    /**
     * Creates a number x-axis.
     * @return A number x-axis.
     */
    protected HorizontalNumberAxis createNumberXAxis()
    {
        HorizontalNumberAxis axis = new HorizontalNumberAxis("");
        axis.setStandardTickUnits(TickUnits.createIntegerTickUnits());
        axis.setAutoRange(graphInfo.usesFixMaxValue);
        if (graphInfo.usesFixMaxValue)
        {
            axis.setRange(0.0d, (double) graphInfo.maxValue);
        }
        return axis;
    }
    
    /**
     * Creates a number y-axis.
     * @return A number y-axis.
     */
    protected VerticalNumberAxis createNumberYAxis() 
    {
        VerticalNumberAxis axis = new VerticalNumberAxis("Amount");
        axis.setStandardTickUnits(TickUnits.createIntegerTickUnits());        
        return axis;
    }
    
    /**
     * Creates a category x-axis.     
     * @return A category x-axis.
     */
    protected HorizontalCategoryAxis createCategoryXAxis() 
    {
        HorizontalCategoryAxis axis = new HorizontalCategoryAxis();                
        axis.setVerticalCategoryLabels(true);
        return axis;
    }
    
    /**
     * Creates a category y-axis.
     *
     * @return A category y-axis.
     */
    protected VerticalCategoryAxis createCategoryYAxis() 
    {
        VerticalCategoryAxis axis = new VerticalCategoryAxis("");        
        //axis.setHorizontalCategoryLabels(true);
        return axis;
    }
    
    
    // Test method
    
    public static void main(String[] args)
    {
        try
        {            
            medview.datahandling.examination.tree.ExaminationValueTable evt = new medview.datahandling.examination.tree.ExaminationValueTable();
            
            GraphXMLParser parser = new GraphXMLParser();

            GraphSet graphSet = parser.parse(new java.io.File("/home/erichson/medview/graph.xml"));

            GraphInfo graphInfo = graphSet.getGraph(graphSet.getAvailableGraphs()[1]);
            GraphComponent graphComponent = new GraphComponent(graphInfo, evt); // Skip evc for now

            JFrame frame = new JFrame();
            frame.getContentPane().add(graphComponent);
            frame.pack();
            frame.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
