package medview.visualizer.development.experiments.DragDrop;

import javax.swing.*;
import java.awt.*;

public class MyDropTarget extends JFrame {

    private DroppablePanel panel;

    public MyDropTarget() {
	panel = new DroppablePanel();
	getContentPane().add(panel);
	setBounds(10,10,400,400);	
    }

    public static void main(String[] args) {
	(new MyDropTarget()).setVisible(true);
    }

}
