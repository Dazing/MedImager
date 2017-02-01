/*
 * $Id: LoadSessionThread.java,v 1.8 2005/05/20 11:16:11 erichson Exp $
 *
 * $Log: LoadSessionThread.java,v $
 * Revision 1.8  2005/05/20 11:16:11  erichson
 * Catches SQLException for better handling of SQL server login failure
 *
 * Revision 1.7  2005/02/16 11:10:08  erichson
 * minor update since createExaminationDataElement was changed
 *
 * Revision 1.6  2004/11/22 10:31:15  erichson
 * Added identifier to "could not create element" error message
 *
 * Revision 1.5  2004/11/13 10:51:40  erichson
 * Thread naming
 *
 * Revision 1.4  2004/10/21 12:18:38  erichson
 * Added descriptions to the progress updating.
 *
 * Revision 1.3  2004/10/20 10:55:20  erichson
 * 1 comment
 *
 * Revision 1.2  2004/10/11 14:06:59  erichson
 * ExaminaitionIdentifiers are now recreated in DataSource instead of here
 *
 * Revision 1.1  2004/10/11 11:41:09  erichson
 * First check-in.
 * 
 *
 * Created on den 11 oktober 2004, 13:27
 */

package medview.visualizer.data.session;

import java.io.*;
import java.util.*;

import java.net.URI;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;

import java.text.ParseException;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;


/**
 * Thread for loading sessions.
 * (Was a part of ApplicationManager earlier)
 *
 * @author Göran Zachrisson, Nils Erichson
 */

public class LoadSessionThread extends Thread implements ProgressObject {
        private File sessionFile;
        private boolean cancelled = false;
        private boolean indeterminate = true;
        private boolean finished = false;
        int progress = 0;
        int progressMin = 0;
        int progressMax = 100;
        private String progressDescription = new String();                
        
        public LoadSessionThread(File f) {
            super("LoadSessionThread");
            sessionFile = f;
        }
        
        public void run() {
            ApplicationManager.debug("Starting loadSessionThread");
            cancelled = false;
            
            progressDescription = "Load Session: Loading properties from file";
            // load the properties from the file
            Properties properties = new Properties();
            try {
                FileInputStream iStream = new FileInputStream(sessionFile);
                properties.load(iStream);
                iStream.close();
            } catch (FileNotFoundException exc) {
                ApplicationManager.errorMessage("Could not load session: " + exc.getMessage());
                return;
            } catch (IOException exc) {
                ApplicationManager.errorMessage("Could not load session: " + exc.getMessage());
                return;
            }
            
            progressDescription = "Load Session: Loading data groups";
            // load datagroups
            int dataGroupCount = 0;
            try {
                dataGroupCount = Integer.decode(properties.getProperty("datagroup_count", "0")).intValue();
            } catch (NumberFormatException exc) {
                ApplicationManager.errorMessage("loadSession: Could not get datagroup count.");
            }
            
            DataGroup[] dataGroups = new DataGroup[dataGroupCount];
            for (int i = 0; i < dataGroupCount; i++) {
                // get the datagroup name
                String name = properties.getProperty("datagroup_" + i + "_name", DataManager.getInstance().proposeDataGroupName());
                
                // get the datagroup color
                int r = 0;
                int g = 0;
                int b = 0;
                String[] colors = parseCommaSeparatedString(properties.getProperty("datagroup_" + i + "_color", "0,0,0"));
                if (colors.length == 3) {
                    try {
                        r = Integer.decode(colors[0]).intValue();
                        g = Integer.decode(colors[1]).intValue();
                        b = Integer.decode(colors[2]).intValue();
                    } catch (NumberFormatException exc) {
                    }
                } else {
                    ApplicationManager.errorMessage("loadSession: Could not get datagroup color.");
                }
                Color color = new Color(r, g, b);
                
                // create the datagroup
                dataGroups[i] = new DataGroup(name, color);
                DataManager.getInstance().addDataGroup(dataGroups[i]);
            }
            
            ApplicationManager.getInstance().setStatusBarText("Session: Loading data sources...");
            
            // load datasources
            progressDescription = "Load Session: Loading data sources";
            int dataSourceCount = 0;
            try {
                dataSourceCount = Integer.decode(properties.getProperty("datasource_count", "0")).intValue();
            } catch (NumberFormatException exc) {
                ApplicationManager.errorMessage("Could not get datasource count.");
            }
            
            DataSource[] dataSources = new DataSource[dataSourceCount];
            for (int i = 0; i < dataSourceCount; i++) {
                // get the datasource url
                String urlString = properties.getProperty("datasource_" + i + "_url", "");
                
                try {
                    URI url = new URI(urlString);
                    dataSources[i] = DataSourceFactory.createDataSource(url);
                } catch (java.sql.SQLException sqle)
                {
                    ApplicationManager.errorMessage("loadSession: Could not restore SQL datasource: " + sqle.getMessage()); 
                } catch (InvalidDataLocationException exc) {
                    dataSources[i] = null;
                    ApplicationManager.errorMessage("loadSession: Could not create datasource (InvalidDataLocation): " + exc.getMessage());
                } catch (IOException exc) {
                    dataSources[i] = null;
                    ApplicationManager.errorMessage("loadSession: Could not create datasource (IOException): " + exc.getMessage());
                } catch (java.net.URISyntaxException URIse) {
                    dataSources[i] = null;
                    ApplicationManager.errorMessage("loadSession: Could not create datasource (URISyntaxException): " + URIse.getMessage());
                }
            }
            
            progressDescription = "Load Session: Loading data elements";
            ApplicationManager.getInstance().setStatusBarText("Session: Loading data elements...");
            
            indeterminate = false;
            
            // load elements
            HashSet loadedTerms = new HashSet();
            int elementCount = 0;
            try {
                elementCount = Integer.decode(properties.getProperty("element_count", "0")).intValue();
            } catch (NumberFormatException exc) {
                errorMessage("loadSession: Could not get element count.");
            }
            ExaminationDataElement[] elements = new ExaminationDataElement[elementCount];
            progressMax = elementCount;            
            for (int i = 0; (i < elementCount) && !cancelled; i++) {
                progress = i;
                
                // get the element's datasource
                int dataSourceIndex = 0;
                String dataSourceString = properties.getProperty("element_" + i + "_datasource", "");
                try {
                    dataSourceIndex = Integer.decode(dataSourceString).intValue();
                } catch (NumberFormatException exc) {
                    errorMessage("loadSession: Could not get element datasource.");
                }
                
                // get the element's datagroup
                int dataGroupIndex = 0;
                String dataGroupString = properties.getProperty("element_" + i + "_datagroup", "");
                try {
                    dataGroupIndex = Integer.decode(dataGroupString).intValue();
                } catch (NumberFormatException exc) {
                    errorMessage("loadSession: Could not get element datagroup.");
                }
                
                // get the element's identifier
                String identifierString = properties.getProperty("element_" + i + "_identifier", "");
                
                if (dataSources[dataSourceIndex] == null) {
                    errorMessage("loadSession: Skipped loading data element [" + identifierString + "]!");
                } else {
                    // create the element
                    try {
                        ExaminationIdentifier id = 
                            dataSources[dataSourceIndex].createExaminationIdentifierFromStringRepresentation(identifierString);
                        // System.out.println("Created id from string: " + id);
                        assert (id.getPcode() != null);
                        
                        elements[i] = dataSources[dataSourceIndex].createExaminationDataElement(
                                dataGroups[dataGroupIndex],
                                id,
                                true); // add derived terms?
                    } catch (ParseException exc) { // TODO: npassa data source type, eller lat datasourcen skapa elementet
                        elements[i] = null;
                        errorMessage("loadSession: Could not create examination dataelement - ParseException:" + exc.getMessage());
                    } catch (IOException exc) {
                        elements[i] = null;
                        errorMessage("loadSession: Could not create examination dataelement - IOException: " + exc.getMessage());
                    } catch (NoSuchExaminationException exc) {
                        elements[i] = null;
                        errorMessage("loadSession: Could not create examination dataelement - NoSuchExaminationException: " + exc.getMessage());
                    }
                }
                if (elements[i] != null) {
                    boolean elementAdded = DataManager.getInstance().addDataElement(elements[i]);
                    if (!elementAdded) {
                        elements[i].removeDataElement();
                    } else {
                        String[] terms = elements[i].getTermsWithValues();
                        for (int j = 0; j < terms.length; j++) {
                            loadedTerms.add(terms[j]);
                        }
                    }
                }
                /*
                fireProgressMade("Loading session.",
                    "Examination " + (i + 1) + " of " + elementCount + " loaded.",
                    0, i, elementCount - 1);
                 */
            }
            
            indeterminate = true;
            
            // add the loaded terms
            DataManager.getInstance().addTerms((String[])loadedTerms.toArray(new String[loadedTerms.size()]));
            DataManager.getInstance().validateViews();
            
            // end start
            
            
            
            // if the user cancelled the load session operation then exit
            if (cancelled) {
                return;
            }
            
            progressDescription = "Load Session: Loading aggregations";
            // load aggregations
            int aggregationCount = 0;
            try {
                aggregationCount = Integer.decode(properties.getProperty("aggregation_count", "0")).intValue();
            } catch (NumberFormatException exc) {
                errorMessage("loadSession: Could not get aggregation count.");
            }
            
            
            Aggregation[] aggregations = new Aggregation[aggregationCount];
            for (int i = 0; i < aggregationCount; i++) {
                setStatusBarText("Session: Loading aggregation " + (i+1) + " of " + aggregationCount);
                String filename = properties.getProperty("aggregation_" + i + "_filename", Session.NO_ACTIVE_AGGREGATION_STRING);
                if (!filename.equals(Session.NO_ACTIVE_AGGREGATION_STRING)) {
                    try {
                        aggregations[i] = new Aggregation(new File(filename));
                        DataManager.getInstance().addAggregation(aggregations[i]);
                    } catch (IOException exc) {
                        aggregations[i] = null;
                        errorMessage("loadSession: Could not find aggregation: " + exc.getMessage());
                    }
                } else {
                    aggregations[i] = null;
                }
            }
            
            progressDescription = "Load Session: Loading views";
            // load views
            int viewCount = 0;
            try {
                viewCount = Integer.decode(properties.getProperty("view_count", "0")).intValue();
            } catch (NumberFormatException exc) {
                errorMessage("loadSession: Could not get view count.");
            }
            
            for (int i = 0; i < viewCount; i++) {
                setStatusBarText("Session: Loading view " + (i + 1) + " of " + viewCount);
                String viewType = properties.getProperty("view_" + i + "_type", View.UNKNOWN_VIEW_TYPE);
                String[] elementStrings = parseCommaSeparatedString(properties.getProperty("view_" + i + "_elements", ""));
                String[] locationStrings = parseCommaSeparatedString(properties.getProperty("view_" + i + "_location", "0,0"));
                String[] sizeStrings = parseCommaSeparatedString(properties.getProperty("view_" + i + "_size", "200,200"));
                String aggregationString = properties.getProperty("view_" + i + "_aggregation", Session.NO_ACTIVE_AGGREGATION_STRING);
                Aggregation aggregation = null;
                if (!aggregationString.equals(Session.NO_ACTIVE_AGGREGATION_STRING)) {
                    try {
                        aggregation = aggregations[Integer.decode(aggregationString).intValue()];
                    } catch (NumberFormatException exc) {
                        errorMessage("loadSession: Could not get aggregation for view.");
                    }
                }
                
                // calculate the location of the viewframe
                Point location = new Point(0,0);
                if (locationStrings.length == 2) {
                    try {
                        location = new Point(
                        Integer.decode(locationStrings[0]).intValue(),
                        Integer.decode(locationStrings[1]).intValue());
                    } catch(NumberFormatException exc) {
                        errorMessage("loadSession: Could not get view position.");
                    }
                }
                
                // calculate the size of the viewframe
                Dimension size = new Dimension(200,200);
                if (sizeStrings.length == 2) {
                    try {
                        size = new Dimension(
                        Integer.decode(sizeStrings[0]).intValue(),
                        Integer.decode(sizeStrings[1]).intValue());
                    } catch(NumberFormatException exc) {
                        errorMessage("loadSession: Could not get view size.");
                    }
                }
                
                // create the dataset
                Vector elementVector = new Vector();
                for (int j = 0; j < elementStrings.length; j++) {
                    if (!elementStrings[j].equals("")) {
                        try {
                            int elementIndex = Integer.decode(elementStrings[j]).intValue();
                            if (elements[elementIndex] != null) {
                                elementVector.add(elements[elementIndex]);
                            }
                        } catch (NumberFormatException exc) {}
                    }
                }
                ExaminationDataSet dataSet = new ExaminationDataSet(
                (ExaminationDataElement[]) elementVector.toArray(new ExaminationDataElement[elementVector.size()]));
                
                // create the right viewfactory
                ViewFactory viewFactory = null;
                if (viewType.equals(View.SCATTERPLOT_VIEW_TYPE)) {
                    String xTerm = properties.getProperty("view_" + i + "_x_term", DataManager.getInstance().getDefaultTerm());
                    String yTerm = properties.getProperty("view_" + i + "_y_term", DataManager.getInstance().getDefaultTerm());
                    boolean percentValues = properties.getProperty("view_" + i + "_percent_values", "false").equals("true");
                    viewFactory = new ScatterPlotViewFactory(xTerm, yTerm, percentValues);
                } else if (viewType.equals(View.BARCHART_VIEW_TYPE)) {
                    String term = properties.getProperty("view_" + i + "_term", DataManager.getInstance().getDefaultTerm());
                    boolean horizontalBars = properties.getProperty("view_" + i + "_bar_orientation", "horizontal").equals("horizontal");
                    boolean stackedBars = properties.getProperty("view_" + i + "_stacked_bars", "false").equals("true");
                    boolean percentValues = properties.getProperty("view_" + i + "_percent_values", "false").equals("true");
                    viewFactory = new BarChartViewFactory(term, horizontalBars, stackedBars, percentValues);
                } else if (viewType.equals(View.IMAGE_VIEW_TYPE)) {
                    viewFactory = new ImageViewFactory();
                } else if (viewType.equals(View.STATISTICS_VIEW_TYPE)) {
                    String term = properties.getProperty("view_" + i + "_term", DataManager.getInstance().getDefaultTerm());
                    viewFactory = new StatisticsViewFactory(term);
                } else if (viewType.equals(View.TABLE_VIEW_TYPE)) {
                    viewFactory = new TableViewFactory();
                } else if (viewType.equals(View.SUMMARY_VIEW_TYPE)) {
                    viewFactory = new SummaryViewFactory();
                }
                
                // create and display the view
                View view = viewFactory.createView(dataSet);
                if (aggregation != null) {
                    view.setAggregation(aggregation);
                }
                ApplicationFrame.getInstance().showView(view, location, size);
            }
            ApplicationFrame.getInstance().setEnabled(true);
            ApplicationFrame.getInstance().requestFocus();
            setStatusBarText("Session loading finished.");
            finished = true;
            ApplicationManager.debug("Finished loadSessionThread");
        }

        public boolean isRunning() {
            return !finished;
        }
        
        public boolean isIndeterminate() {
            return indeterminate;
        }
        
        public boolean isCancelled() {
            return cancelled;
        }
        
        public int getProgressMin() {
            return progressMin;
        }
        
        public int getProgressMax() {
            return progressMax;
        }
        
        public int getProgress() {
            return progress;
        }
        
        public void cancel() {
            cancelled = true;
        }
        
        public String getDescription()
        {
            return progressDescription;
        }
        
 // end start() method
        
        /**
     * Takes a comma separated string and returns an array of the substrings.
     *
     * @param string A comma separated string.
     * @return An array of the substrings.
     */
    private static String[] parseCommaSeparatedString(String string) {
        Vector stringVector = new Vector();
        String currentString = new String();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ',') {
                stringVector.add(currentString);
                currentString = new String();
            } else {
                currentString += string.charAt(i);
            }
        }
        stringVector.add(currentString);
        return (String[]) stringVector.toArray(new String[stringVector.size()]);
    }    

    // help methods
    private void errorMessage(String str) {
        ApplicationManager.errorMessage(str);
    }

    private void setStatusBarText(String str) {
        ApplicationManager.getInstance().setStatusBarText(str);
    }

} // end class
    