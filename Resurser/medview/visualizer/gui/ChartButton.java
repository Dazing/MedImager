/*
 * ChartButton.java
 *
 * Created on July 25, 2002, 4:00 PM
 *
 * $Id: ChartButton.java,v 1.5 2004/10/12 10:01:01 erichson Exp $
 *
 * $Log: ChartButton.java,v $
 * Revision 1.5  2004/10/12 10:01:01  erichson
 * Updated to ExaminationDropTargetButton base class.
 *
 * Revision 1.4  2002/10/30 15:56:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import javax.swing.*;

import medview.visualizer.data.*;

/**
 * A JButton subclass for the chart buttons in the toolbar.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ChartButton extends ExaminationDropTargetButton {
    
    /** The ViewFactory that the ChartButton uses to create a View. */
    private ViewFactory viewFactory;
    
    /** 
     * Creates a new instance of ChartButton with only a text label.
     *
     * @param text The text of the button.
     * @param factory The ViewFactory used by the ChartButton to create a View.
     */
    public ChartButton(String text, ViewFactory factory) {
        super(text);
        viewFactory = factory;
    }
    
    /** 
     * Creates a new instance of ChartButton with only an icon.
     *
     * @param icon The icon of the button.
     * @param factory The ViewFactory used by the ChartButton to create a View.
     */    
    public ChartButton(Icon icon, ViewFactory factory) {
        super(icon);
        viewFactory = factory;
    }
    
    /** 
     * Creates a new instance of ChartButton with a text label and an icon.
     *
     * @param text The text of the button.
     * @param icon The icon of the button.
     * @param factory The ViewFactory used by the ChartButton to create a View.
     */
    public ChartButton(String text, Icon icon, ViewFactory factory) {
        super(text,icon);
        viewFactory = factory;
    }
    
    public void buttonClicked() {        
        // Default behaviour is creating an empty view
        ExaminationDataElementVector elementVector = new ExaminationDataElementVector();
        examinationsDropped(elementVector);
    }
    
    public void examinationsDropped(ExaminationDataElementVector elementVector) 
    {
        showChart(elementVector);
    }
    
    /**
     * Creates a View from an ExaminationDataElementVector and displays it.
     *
     * @param elementVector An ExaminationDataElementVector containing the examinations to create the View from.
     */
    private void showChart(ExaminationDataElementVector elementVector) {
        ExaminationDataSet dataSet = new ExaminationDataSet(elementVector.toArray());
        ApplicationFrame.getInstance().showView(viewFactory.createView(dataSet));
    }
    
}
