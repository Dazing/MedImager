package medview.medrecords.components.inputs;

import medview.medrecords.models.MineralizationModel;
import medview.medrecords.models.ErosionModel;
import medview.datahandling.examination.tree.Tree;

import java.awt.*;
import java.util.Date;

/**

 * Trauma input component
 * this is just a wrapper class because java doesn't support multiple inheritance
 * @author Andreas Argirakis

 */

public class ErosionInput extends ValueInputComponent{


    ErosionInputComponent erosionComp;
    //creates the tabbed pane and add all the trauma input components
    public ErosionInput(ErosionModel erModel)
    {
        super(erModel);
        setLayout(new GridLayout(1,1));
        erosionComp = new ErosionInputComponent();
        add(erosionComp);
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
		return erosionComp.buildTree();
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
