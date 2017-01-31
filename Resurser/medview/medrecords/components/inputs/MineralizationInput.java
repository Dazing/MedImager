package medview.medrecords.components.inputs;

import medview.medrecords.models.TraumaModel;
import medview.medrecords.models.MineralizationModel;
import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Date;

/**

 * @author Andreas Argirakis

 */

public class MineralizationInput extends ValueInputComponent{


    //creates the tabbed pane and add all the trauma input components
    MineralizationInputComponent mineralization;
    public MineralizationInput(MineralizationModel minModel)
    {
        super(minModel);
        setLayout(new GridLayout(1,1));
        mineralization = new MineralizationInputComponent();
        add(mineralization);
    }

    public String[] getValues()
	{
		return null;
	}
	public void clearContents()
	{
	}
	public void focusInput()
	{
	}
	public void verifyInput()
	{
	}
	public void putPreset(String value)
	{
    }
	public void putCustomPreset(String key, String value)
	{
    }
    public void setEditable(boolean e)
	{
	}
	public Tree getTreeRepresentation(Date date, String pCode)
	{
		return mineralization.buildTree();
	}


    //just a test method
    /*public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        TraumaInputComponent debugComponent = new TraumaInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Mineralization component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }*/
}
