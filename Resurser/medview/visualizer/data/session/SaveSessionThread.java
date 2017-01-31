/*
 * $Id: SaveSessionThread.java,v 1.6 2005/10/21 09:01:16 erichson Exp $
 *
 * Created on den 11 oktober 2004, 13:17
 *
 * $Log: SaveSessionThread.java,v $
 * Revision 1.6  2005/10/21 09:01:16  erichson
 * Minor cleanup
 *
 * Revision 1.5  2004/11/16 09:20:00  erichson
 * Added extra null checking when saving aggregation
 *
 * Revision 1.4  2004/11/13 10:52:24  erichson
 * Thread naming
 *
 * Revision 1.3  2004/10/21 12:18:53  erichson
 * Added descriptions to the progress updating.
 *
 * Revision 1.2  2004/10/20 10:55:36  erichson
 * 1 comment
 *
 * Revision 1.1  2004/10/11 11:41:10  erichson
 * First check-in.
 *
 */

package medview.visualizer.data.session;

import java.io.*;
import java.util.*;

import java.awt.Color;

import medview.datahandling.aggregation.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;


/**
 * Thread for saving the session.
 * (Was a part of ApplicationManager earlier)
 * @author Göran Zachrisson, Nils Erichson
 */
public class SaveSessionThread extends Thread implements ProgressObject
{
        private File sessionFile;
        private boolean cancelled = false;
        private boolean finished = false;
        
        private String progressDescription = new String();
        
        public SaveSessionThread(File sesFile)
        {
            super("SaveSessionThread");
            sessionFile = sesFile;
        }
        
        public void start() {
            ApplicationManager.debug("Starting SaveSessionThread");
            Properties properties = new Properties();
            
            progressDescription = "Save Session: Creating datagroup mappings";
            // create datagroup mappings
            DataGroup[] dataGroups = DataManager.getInstance().getDataGroups();
            Hashtable dataGroupTable = new Hashtable();
            
            for (int i = 0; i < dataGroups.length; i++) {
                dataGroupTable.put(dataGroups[i], new Integer(i));
            }
            
            progressDescription = "Save Session: Saving data groups";
            // save datagroups
            properties.put("datagroup_count", "" + dataGroups.length);
            for (int i = 0; i < dataGroups.length; i++) {
                String key = "datagroup_" + i;
                Color color = dataGroups[i].getColor();
                
                properties.put(key + "_name", dataGroups[i].getName());
                properties.put(key + "_color", "" + color.getRed() + "," + color.getGreen() + "," + color.getBlue());
            }
            
            // prepare for element and datasource mappings
            LinkedHashSet dataSourceSet = new LinkedHashSet();
            Hashtable dataSourceTable = new Hashtable();
            Hashtable elementTable = new Hashtable();
            
            ExaminationDataElement[] elements = DataManager.getInstance().getAllElements();
            
            // create the element mapping
            for (int i = 0; i < elements.length; i++) {
                elementTable.put(elements[i], new Integer(i));
                dataSourceSet.add(elements[i].getDataSource());
            }
            
            // create the datasource mapping
            DataSource[] dataSources = (DataSource[]) dataSourceSet.toArray(new DataSource[dataSourceSet.size()]);
            for (int i = 0; i < dataSources.length; i++) {
                dataSourceTable.put(dataSources[i], new Integer(i));
            }
            
            progressDescription = "Save Session: Saving data sources";
            // save the datasources
            properties.put("datasource_count", "" + dataSources.length);
            for (int i = 0; i < dataSources.length; i++) {
                properties.put("datasource_" + i + "_url", dataSources[i].getURI().toString());
            }
            
            // save elements
            progressDescription = "Save Session: Saving elements";
            properties.put("element_count", "" + elements.length);
            for (int i = 0; i < elements.length; i++) 
            {
                DataGroup dataGroup = elements[i].getDataGroup();
                DataSource dataSource = elements[i].getDataSource();
                
                properties.put("element_" + i + "_datasource",
                    ((Integer) dataSourceTable.get(dataSource)).toString());
                properties.put("element_" + i + "_datagroup",
                    ((Integer) dataGroupTable.get(dataGroup)).toString());
                try {
                    properties.put("element_" + i + "_identifier",
                    elements[i].getExaminationIdentifier().toString());
                } catch (IOException exc) {
                    // do nothing??
                }
            }
            
            progressDescription = "Save Session: Saving aggregations";
            // create aggregation mappings
            Hashtable aggregationTable = new Hashtable();
            Aggregation[] aggregations = DataManager.getInstance().getAggregations();
            for (int i = 0; i < aggregations.length; i++) {
                aggregationTable.put(aggregations[i], new Integer(i));
            }
            
            // save aggregations
            properties.put("aggregation_count", "" + aggregations.length);
            for (int i = 0; i < aggregations.length; i++) {
                properties.put("aggregation_" + i + "_filename", aggregations[i].getMVGFile().getPath());
            }
            
            progressDescription = "Save Session: Saving views";
            // save views
            ViewFrame[] allViewFrames = ApplicationManager.getInstance().getViewFrames();
            properties.put("view_count", "" + allViewFrames.length);
            for (int i = 0; i < allViewFrames.length; i++) {
                ViewFrame viewFrame = (ViewFrame) allViewFrames[i];
                View view = viewFrame.getView();
                
                String type = View.UNKNOWN_VIEW_TYPE;
                if (view instanceof BarChartView) {
                    type = View.BARCHART_VIEW_TYPE;
                    BarChartView barChartView = (BarChartView) view;
                    properties.put("view_" + i + "_term", barChartView.getTerm());
                    if (barChartView.isHorizontal()) {
                        properties.put("view_" + i + "_bar_orientation", "horizontal");
                    } else {
                        properties.put("view_" + i + "_bar_orientation", "vertical");
                    }
                    if (barChartView.isBarsStacked()) {
                        properties.put("view_" + i + "_stacked_bars", "true");
                    } else {
                        properties.put("view_" + i + "_stacked_bars", "false");
                    }
                    if (barChartView.isPercentValuesUsed()) {
                        properties.put("view_" + i + "_percent_values", "true");
                    } else {
                        properties.put("view_" + i + "_percent_values", "false");
                    }
                } else if (view instanceof ScatterPlotView) {
                    type = View.SCATTERPLOT_VIEW_TYPE;
                    ScatterPlotView scatterPlotView = (ScatterPlotView) view;
                    properties.put("view_" + i + "_x_term", scatterPlotView.getXTerm());
                    properties.put("view_" + i + "_y_term", scatterPlotView.getYTerm());
                    if (scatterPlotView.isPercentValuesUsed()) {
                        properties.put("view_" + i + "_percent_values", "true");
                    } else {
                        properties.put("view_" + i + "_percent_values", "false");
                    }
                } else if (view instanceof ImageView) {
                    type = View.IMAGE_VIEW_TYPE;
                } else if (view instanceof StatisticsView) {
                    type = View.STATISTICS_VIEW_TYPE;
                    properties.put("view_" + i + "_term", ((StatisticsView)view).getTerm());
                } else if (view instanceof TableView) {
                    type = View.TABLE_VIEW_TYPE;
                } else if (view instanceof SummaryView) {
                    type = View.SUMMARY_VIEW_TYPE;
                }
                properties.put("view_" + i + "_type", type);
                properties.put("view_" + i + "_location",
                "" + viewFrame.getLocation().x + "," + viewFrame.getLocation().y);
                properties.put("view_" + i + "_size",
                "" + viewFrame.getSize().width + "," + viewFrame.getSize().height);
                
                // save the active aggregation
                String aggregationString = Session.NO_ACTIVE_AGGREGATION_STRING;
                
                Aggregation activeAggregation = view.getAggregation();                              
                
                if (activeAggregation != null)
                {
                    Integer aggregationIndex = (Integer) aggregationTable.get(activeAggregation);                
                    if (aggregationIndex != null) 
                    {
                        aggregationString = aggregationIndex.toString();
                    }
                }
                
                properties.put("view_" + i + "_aggregation", aggregationString);
                
                // save the elements of the view
                ExaminationDataElement[] elementsInView = view.getExaminationDataSet().getElements();
                String elementString = "";
                for (int e = 0; e < elementsInView.length; e++) {
                    if (!elementString.equals("")) {
                        elementString += "," + ((Integer)elementTable.get(elementsInView[e])).toString();
                    } else {
                        elementString += ((Integer)elementTable.get(elementsInView[e])).toString();
                    }
                }
                properties.put("view_" + i + "_elements", elementString);
            }
            
            progressDescription = "Save Session: Saving session to file";
            // store the session to a file
            try {
                FileOutputStream oStream = new FileOutputStream(sessionFile);
                properties.store(oStream, null);
                oStream.close();
            } catch (FileNotFoundException exc) {
                ApplicationManager.errorMessage("Could not save session: " + exc.getMessage());
            } catch (IOException exc) {
                ApplicationManager.errorMessage("Could not save session: " + exc.getMessage());
            }
            finished = true;
            ApplicationManager.debug("Finished saveSessionThread");
        } // end start
        
        public void cancel() {
            cancelled = true;
        }
        
        public int getProgress() {
            return 0; // Doesn't matter since it's always indeterminate
        }
        
        public int getProgressMax() {
            return 100; // Doesn't matter since it's always indeterminate
        }
        
        public int getProgressMin() {
            return 0; // Doesn't matter since it's always indeterminate
        }
        
        public boolean isCancelled() {
            return cancelled;
        }
        
        public boolean isIndeterminate() {
            return true; // Always can
        }
        
        public boolean isRunning() {
            return !finished;
        }
        
        // Progress description
        public String getDescription() {
            return progressDescription;
        }
        
    }
    
    