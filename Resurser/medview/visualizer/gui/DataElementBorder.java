/*
 * DataElementBorder.java
 *
 * Created on den 4 december 2002, 16:23
 *
 * $Id: DataElementBorder.java,v 1.1 2002/12/05 11:29:09 erichson Exp $
 *
 * $Log: DataElementBorder.java,v $
 * Revision 1.1  2002/12/05 11:29:09  erichson
 * First check-in
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.geom.*;

import medview.datahandling.examination.*;
import medview.visualizer.data.*;

/**
 * A border for a dataElement. It is drawn as the dataGroup color when the element is not selected, and if 
 * the element is selected it is drawn as the dataGroup color surronded by a red-yellow dashed line.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class DataElementBorder extends javax.swing.border.LineBorder {
    
    private ExaminationDataElement dataElement;
    private final BasicStroke defaultStroke; 
    
    /** The dash pattern - 8 pixels drawn, 8 pixels not drawn, etc etc */
    private static final float[] dashArray = { (float) 8.0, (float) 8.0 };
    
    /** Creates a new instance of DataElementBorder */
    public DataElementBorder(ExaminationDataElement ede, int thickness) {
        super(ede.getDataGroup().getColor(), thickness);
        dataElement = ede;
        defaultStroke = new BasicStroke((float) thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    }
    
    /** 
     * Returns the color of the border.
     */
    public Color getLineColor() {
        return dataElement.getDataGroup().getColor();
    }
    
    /** Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     *
     */            
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c,g,x,y,width,height);
        Graphics2D g2d = (Graphics2D) g;
        if (dataElement.isSelected()) {                                                          
            Rectangle2D.Float borderRectangle = new Rectangle2D.Float( x, y, width, height );
            
            // First draw a red line underneath
            g2d.setColor(Color.RED);
            g2d.setStroke(defaultStroke);            
            g2d.draw(borderRectangle);
            
            // Then add yellow dashes 
            BasicStroke stroke = new BasicStroke((float) thickness,defaultStroke.getEndCap(),defaultStroke.getLineJoin(),defaultStroke.getMiterLimit(), dashArray,defaultStroke.getDashPhase());
            
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(stroke);                               
            g2d.draw(borderRectangle);
        }
    } 
}
