package medview.visualizer.development.experiments.DragDrop;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*; // Drag n drop
import java.awt.datatransfer.*; // Transferable
import javax.swing.*;
import javax.swing.event.*;



public class MyOldDragSource extends JFrame {
    
    private Image theRealImage;
    
    public MyOldDragSource() {
        super("DragSource test");
        // raden nedan testar bara funktionaliteten hos SystemFlavorMap
        System.out.println(SystemFlavorMap.encodeDataFlavor(JPEGFlavor.getInstance()));
        
        theRealImage = (new ImageIcon(getClass().getResource("/medview/visualizer/resources/icons/arrow.gif"))).getImage();
        DragLabel picLabel = new DragLabel(new ImageIcon(theRealImage));

        
            
        //picLabel.setDragSourceListener(this);
        
        this.getContentPane().add(picLabel);
        
        this.setBounds(10,10,200,200);
    }
    
    public static void main(String[] args) {
        new MyOldDragSource().setVisible(true);
    }
    
    
    
    
    public class DragLabel extends JLabel implements DragGestureListener, DragSourceListener {
        
        
        private DragSource dragSource;
        private DragGestureListener dgListener;
        private DragSourceListener dsListener;
        
        private ImageIcon theStoredIcon;
        
        public DragLabel(ImageIcon icon) {
            super(icon);
            theStoredIcon = icon;
            this.dragSource = DragSource.getDefaultDragSource();
            this.dgListener = this;
            this.dsListener = this;
            
            // component, action, listener
            this.dragSource.createDefaultDragGestureRecognizer(
            this, DnDConstants.ACTION_COPY_OR_MOVE, this.dgListener );
        }
        
        public Transferable getTransferable() {
            System.out.println("getTransferable run. theRealImage = " +theRealImage);
            return new ImageTransferable(theRealImage);
            //Image newImg = theStoredIcon.getImage();
          
            //return new ImageTransferable(newImg);
        }
        /**
        public void setDragSourceListener(DragSourceListener dsl) {
            dsListener = dsl;
        }
         */
        
        public void dragGestureRecognized(DragGestureEvent e) {
            // check to see if action is OK ...
            try {
                // Get what to transfer
                DragLabel dragLabel = (DragLabel) e.getComponent();
                Transferable transferable = dragLabel.getTransferable();
                
                //initial cursor, dragImage, dragImagePoint, transferable, dsource listener
                e.startDrag(DragSource.DefaultCopyNoDrop,(new ImageIcon(getClass().getResource("/medview/visualizer/resources/icons/arrow.gif"))).getImage(), new Point(50,50), transferable, dsListener);
                System.out.println("Nu borde en fin bild dras..");
                // or if dragSource is an instance variable:
                // dragSource.startDrag(e, DragSource.DefaultCopyNoDrop, transferable, dsListener);
            } catch( InvalidDnDOperationException idoe ) {
                System.err.println( idoe );
            }
            
        }
        
        public void dragOver(java.awt.dnd.DragSourceDragEvent dragSourceDragEvent) {
            System.out.println("MyDragSource: dragOver");
        }
        
        public void dragExit(java.awt.dnd.DragSourceEvent dragSourceEvent) {
            System.out.println("MyDragSource: dragExit");
        }
        
        public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dragSourceDragEvent) {
            System.out.println("MyDragSource: dropActionChanged");
        }
        
        
        public void dragEnter(DragSourceDragEvent e) {
            System.out.println("MyDragSource: dragEnter");
            
            DragSourceContext context = e.getDragSourceContext();
            //intersection of the users selected action, and the source and target actions
            
            int myaction = e.getDropAction();
            if( (myaction & DnDConstants.ACTION_COPY) != 0) {
                context.setCursor(DragSource.DefaultCopyDrop);
            } else {
                context.setCursor(DragSource.DefaultCopyNoDrop);
            }
        }
        
        public void dragDropEnd( DragSourceDropEvent e ) {
            System.out.println("MyDragSource: dragDropEnd");
            
            
            if ( e.getDropSuccess() == false ) {
                System.out.println("Getdropsuccess == false");
                return;
            }
            int dropAction = e.getDropAction();
            if ( dropAction == DnDConstants.ACTION_MOVE ) {
                System.out.println("dropAction == DnDConstants.ACTION_MOVE");
                // do whatever
            } else {
                System.out.println("dropAction not equal to DnDConstants.ACTION_MOVE");
            }
        }
        
        
    }
    
    
}
