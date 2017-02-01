package medview.visualizer.development.experiments.DragDrop;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;

public class DroppablePanel extends JPanel {
    private DropTarget dropTarget;
    private DropTargetListener dtListener;
    private int acceptableActions = DnDConstants.ACTION_COPY;
    private JLabel label;

    public DroppablePanel() {
	label = new JLabel();
	add(label);

	dtListener = new DTListener();
	
	dropTarget = new DropTarget(this,acceptableActions,dtListener,true);
    }

    private class DTListener implements DropTargetListener {
	public void dragEnter(DropTargetDragEvent e) {
	    System.out.println("DEBUG: dragEnter");
	    if(isDragOk(e) == false) {
		e.rejectDrag();
		return;
	    }
	    // UNIMPLEMENTED: visa att objektet kan släppas
	}
	
	public void dragOver(DropTargetDragEvent e) {
	    System.out.println("DEBUG: dragOver");
	    if(isDragOk(e) == false) {
		e.rejectDrag();
		return;
	    }
	    e.acceptDrag(acceptableActions);
	}

	public void dropActionChanged(DropTargetDragEvent e) {
	    System.out.println("DEBUG: dropActionChanged");
	    if(isDragOk(e) == false) {
		e.rejectDrag();
		return;
	    }
	    e.acceptDrag(acceptableActions);
	}
	
	public void dragExit(DropTargetEvent e) {
	    System.out.println("DEBUG: dragExit");
	    // UNIMPLEMENTED: visa att objektet inte kan släppas längre
	}

	public void drop(DropTargetDropEvent e) {
	    System.out.println("DEBUG: drop");
	    DataFlavor[] flavors = ImageTransferable.flavors;
	    DataFlavor chosen = null;
	    for(int i=0; i<flavors.length; i++) {
		if(e.isDataFlavorSupported(flavors[i])) {
		    chosen = flavors[i];
		    break;
		}
	    }
	    if(chosen == null) {
		e.rejectDrop();
		return;
	    }
	    int sa = e.getSourceActions();
	    if((sa & acceptableActions) == 0) {
		e.rejectDrop();
		// UNIMPLEMENTED: visa att objektet inte kan släppas längre
		return;
	    }
	    Object data = null;
	    try {
		e.acceptDrop(acceptableActions);
		data = e.getTransferable().getTransferData(chosen);
		if(data == null)
		    throw new NullPointerException();
	    }catch(Throwable t) {
		t.printStackTrace();
		e.dropComplete(false);
		// UNIMPLEMENTED: visa att objektet inte kan släppas längre
		return;
	    }
	    if(data instanceof InputStream) {
		InputStream input = (InputStream)data;
		try {
		    int size = input.available();
		    byte[] imageData = new byte[size];
		    int bytesRead = input.read(imageData);
		    if((bytesRead == -1) || (bytesRead != size)) {
			e.dropComplete(false);
			// UNIMPLEMENTED: visa att objektet inte kan släppas längre
			return;
		    }
		    label.setIcon(new ImageIcon(imageData));
		    e.dropComplete(true);
		}catch(IOException ioe) {
		    System.err.println("ERROR: Couldn't read from stream.");
		    e.dropComplete(false);
		}
	    }

	}
	
	private boolean isDragOk(DropTargetDragEvent e) {
	    DataFlavor[] flavors = ImageTransferable.flavors;
	    DataFlavor chosen = null;
	    for(int i=0; i<flavors.length; i++) {
		if(e.isDataFlavorSupported(flavors[i])) {
		    chosen = flavors[i];
		    break;
		}
	    }
	    if(chosen == null) {
		return false;
	    }
	    // the actions specified when the source created
	    // the DragGestureRecognizer
	    int sa = e.getSourceActions();
	    
	    if((sa & acceptableActions) == 0)
		return false;
	    return true;
	}
    }
}
