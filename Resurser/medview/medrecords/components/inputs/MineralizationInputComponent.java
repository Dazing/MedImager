package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;
import medview.datahandling.examination.tree.TreeLeaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.*;

/**

 * Mineralization input component

 * @author Andreas Argirakis

 */

public class MineralizationInputComponent extends TeethInputComponent {

    private ArrayList<JCheckBox[]> mCheckBoxes = new ArrayList();
    private ArrayList<JComboBox[]> mComboBoxes = new ArrayList();
    private static String[] COMBO_DB_TERMS =    { "SB", "GB", "SO", "GO", "SL", "GL" };
    private static String[] CHECKBOX_DB_TERMS = { "KB", "FB", "KO", "FO", "KL", "FL" };

    public MineralizationInputComponent()
    {
        String[] teethButtons = { "17","16", "15", "14", "13", "12", "11", "21", "22", "23", "24", "25", "26", "27",
                                  "47", "46", "45", "44", "43", "42","41", "31", "32", "33", "34", "35", "36", "37"};
        setTeethButtons(teethButtons);
        buildGUI();
        //createTermValuesFile();

    }

    protected JPanel createInputPanel()
    {
        String[][] comboBoxStrings = { {"", "1", "2", "3"},
                                       { "", "1", "2"} };

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.weightx = 0.5;

        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        label = new JLabel("B");
        panel.add(label,c);

        c.gridx = 2;
        c.gridy = 0;
        label = new JLabel("O");
        panel.add(label,c);

        c.gridx = 3;
        c.gridy = 0;
        label = new JLabel("L");
        panel.add(label,c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 1;
        label = new JLabel("Störning: ");
        panel.add(label,c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 2;
        label = new JLabel("Grad: ");
        panel.add(label,c);

        JComboBox[] arrayComboBoxes = new JComboBox[6];
        JCheckBox[] arrayCheckBoxes = new JCheckBox[6];
        JComboBox cmbBox;

        int count=0;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<2;j++)
            {

                cmbBox = new JComboBox(comboBoxStrings[j]);
                cmbBox.setPreferredSize(new Dimension(150,20));
                cmbBox.setMinimumSize(new Dimension(80,20));
                c.anchor = GridBagConstraints.CENTER;
                c.gridx = (i)+1;
                c.gridy = j+1;
                panel.add(cmbBox,c);
                c.gridx = (i)+2;

                arrayComboBoxes[count] = cmbBox;
                count++;
            }
        }

        JCheckBox check;
        c.gridx = 0;
        c.gridy = 4;
        count = 0;
        JPanel checkPanel;
        for(int i=0;i<3;i++)
        {

            checkPanel = new JPanel(new BorderLayout());
            check = new JCheckBox("Karies");
            arrayCheckBoxes[count] = check;
            c.gridx = i+1;
            checkPanel.add(check,BorderLayout.EAST);
            count++;
            check = new JCheckBox("Fyllning ");
            checkPanel.add(check);
            panel.add(checkPanel,c);
            arrayCheckBoxes[count] = check;
            count++;
        }

        mComboBoxes.add(arrayComboBoxes);
        mCheckBoxes.add(arrayCheckBoxes);

        JLabel labelDesc;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 10;
        c.gridx = 1;
        labelDesc = new JLabel("Störning:");
        panel.add(labelDesc,c);
        c.gridy = 11;
        c.gridx = 1;
        labelDesc = new JLabel("1 = Välavgränsade opaciteter >=2 mm");
        panel.add(labelDesc,c);
        c.gridy = 12;
        c.gridx = 1;
        labelDesc = new JLabel("2 = Diffusa opaciteter, vita linjer");
        panel.add(labelDesc,c);
        c.gridy = 13;
        c.gridx = 1;
        labelDesc = new JLabel("3 = Hypoplasier");
        panel.add(labelDesc,c);
        c.gridy = 10;
        c.gridx = 3;
        labelDesc = new JLabel("Grad:");
        panel.add(labelDesc,c);
        c.gridy = 11;
        c.gridx = 3;
        labelDesc = new JLabel("1 = Hård yta utan tecken på sönderfall");
        panel.add(labelDesc,c);
        c.gridy = 12;
        c.gridx = 3;
        labelDesc = new JLabel("2 = Porös yta med eller utan sönderfall");
        panel.add(labelDesc,c);
        return panel;
    }


    public Tree buildTree()
    {
        TreeBranch rootNode = new TreeBranch("mineralizationRoot");
        TreeBranch locationBranch;
        TreeLeaf leaf;

        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {


            //add the comboboxes
            JComboBox[] arrayComboBoxes =  mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                locationBranch = new TreeBranch("M" + getTeethButtons().get(toothNr) + "" + COMBO_DB_TERMS[i]);
                if(arrayComboBoxes[i].getSelectedIndex()>0)
                {
                    leaf = new TreeLeaf(arrayComboBoxes[i].getSelectedIndex()+"");
                    locationBranch.addChild(leaf);
                }
                rootNode.addChild(locationBranch);
            }

            //add the checkboxes
            JCheckBox[] arrayCheckBoxes =  mCheckBoxes.get(toothNr);
            for(int i=0;i<arrayCheckBoxes.length;i++)
            {
                locationBranch = new TreeBranch("M" + getTeethButtons().get(toothNr) + "" + CHECKBOX_DB_TERMS[i]);
                int boolValue = arrayCheckBoxes[i].isSelected() ? 1 : 0;
                leaf = new TreeLeaf(boolValue+"");
                locationBranch.addChild(leaf);
                rootNode.addChild(locationBranch);
            }
        }
        return rootNode;
    }

    public void loadTree(Tree t)
    {
        /*String term = "mineralizationRoot";

        Tree aNode = t.getNode(term);

		if(aNode != null)
		{
            for(int i = 0; i < getNumberOfTeethButtons() ; i++)
            {
                for(int j=0;j<3;j++)
                {
                    for(int k=0;k<2;k++)
                    {
                       Tree aNode2 = aNode.getNode("Mineralization(tooh" + i + "," + j + "," + k + ")");
                        if(aNode2 != null)
                        {
                            //should only contain one element, but...
                            for (Enumeration aLeaf = aNode2.getChildrenEnumeration(); aLeaf.hasMoreElements(); )
                            {
                                Tree   theLeaf     = (Tree) aLeaf.nextElement();
                                String valString      = theLeaf.getValue();
                                textFields.get(i)[j][k].setText(valString);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            //No mineralizationdata found
        }*/
    }
    public boolean hasData(int tooth)
    {
        //System.out.println(mTextmm.get(tooth).getText());

        int selected=0;
        JComboBox[] combos = mComboBoxes.get(tooth);
        for(int i = 0; i < combos.length; i++)
        {
            selected += combos[i].getSelectedIndex();
        }

        boolean bools = false;
        JCheckBox[] checks = mCheckBoxes.get(tooth);
        for(int i = 0; i < checks.length; i++)
        {
            bools = bools || checks[i].isSelected();
        }
        return (selected>0 || bools);

    }

    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        MineralizationInputComponent debugComponent = new MineralizationInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Mineralization component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void createTermValuesFile()
    {
        System.out.println("------- Mineralisering ----------");
        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            //add the comboboxes
            JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                //print the term
                System.out.println("\n$M" + getTeethButtons().get(toothNr) + "" + COMBO_DB_TERMS[i]);
                //add combo boxes
                for(int k=1;k<arrayComboBoxes[i].getItemCount();k++)
                {
                    String s = (String)arrayComboBoxes[i].getItemAt(k);
                    System.out.println(s);
                }
            }
            //add the checkboxes
            JCheckBox[] arrayCheckBoxes =  mCheckBoxes.get(toothNr);
            for(int i=1;i<arrayCheckBoxes.length;i++)
            {
                //print the term
                System.out.println("\n$M" + getTeethButtons().get(toothNr) + "" + CHECKBOX_DB_TERMS[i]);
                System.out.println("0\n1");
            }
        }
    }
}
