package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;
import medview.datahandling.examination.tree.TreeLeaf;
import medview.medrecords.models.AbstractInputModel;
import medview.medrecords.models.TraumaModel;
import medview.medrecords.models.PresetModel;
import medview.medrecords.data.PreferencesModel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.awt.*;
import java.awt.event.KeyEvent;

/**

 * Trauma input component

 * @author Andreas Argirakis

 */

public class TraumaInputComponent extends ValueInputComponent{

    TraumaStatusInputComponent mStatus;
    TraumaNextVisitInputComponent mNextVisit;
    TraumaStatusMilkInputComponent mStatusMilk;
    TraumaNextVisitMilkInputComponent mNextVisitMilk;
    //creates the tabbed pane and add all the trauma input components
    public TraumaInputComponent(TraumaModel traumaModel)
    {
        super(traumaModel);
        JTabbedPane tabbedPane = new JTabbedPane();
        setLayout(new GridLayout(1,1));

        mStatus = new TraumaStatusInputComponent();
        mNextVisit = new TraumaNextVisitInputComponent();
        mStatusMilk = new TraumaStatusMilkInputComponent();
        mNextVisitMilk = new TraumaNextVisitMilkInputComponent();
        tabbedPane.addTab("Akutbesök perm",mStatus);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Återbesök perm",mNextVisit);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tabbedPane.addTab("Akutbesök prim",mStatusMilk);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPane.addTab("Återbesök prim",mNextVisitMilk);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        add(tabbedPane);
    }

    //load all the input components with the data from file
    public void loadTree(Tree t)
    {
        mStatus.loadTree(t);
        mNextVisit.loadTree(t);
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
        TreeBranch rootNode = new TreeBranch("TraumaRoot");

        rootNode.addChild(mStatus.buildTree());
        rootNode.addChild(mNextVisit.buildTree());
        rootNode.addChild(mStatusMilk.buildTree());
        rootNode.addChild(mNextVisitMilk.buildTree());
        
        return rootNode;
	}


    //just a test method
    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        TraumaInputComponent debugComponent = new TraumaInputComponent(new TraumaModel());
        panel.add(debugComponent);
        JFrame frame = new JFrame("Trauma component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
