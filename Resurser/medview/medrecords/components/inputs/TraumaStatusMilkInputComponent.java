package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;
import medview.datahandling.examination.tree.TreeLeaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.*;

/**

 * Trauma Status Input component

 * @author Andreas Argirakis

 */

public class TraumaStatusMilkInputComponent extends TeethInputComponent {

    final static int NUMBER_OF_COMBOS = 14;
    final static int COMBO_HEIGHT = 20;
    private ArrayList<JComboBox[]> mComboBoxes = new ArrayList();
    private ArrayList<JTextField> mTextmm = new ArrayList();

    public TraumaStatusMilkInputComponent()
    {
        String[] teethButtons = { "53", "52", "51", "61", "62", "63",
                                  "83", "82", "81", "71", "72", "73"};
        setTeethButtons(teethButtons);
        buildGUI();
        refresh();
        //createTermValuesFile();

    }

    //Creates the panel where all the input components reside
    protected JPanel createInputPanel()
    {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String[] plusMinusStrings = { "", "-", "+"};
        String[] labelLeftStrings = { "Tandens färg: ", "Kronfraktur: ", "Displacering: ",
                                         "Mobilitet: "};

        String[][] comboBoxLeftStrings = { { "", "Normal", "Gul", "Röd", "Grå"},
                                       { "", "Nej", "Ja, utan pulpablotta", "Ja, med pulpablotta"},
                                       { "", "Nej", "Intruderad", "Extruderad", "Protruderad", "Retroderad"},
                                       { "", "0", "1", "2", "3"} };

        String[][] comboBoxRightStrings = { { "", "-", "+" },
                                       { "", "-", "+" },
                                       { "", "Nej", "Ja" },
                                       { "", "Nej", "Ja" },
                                       { "", "<1/2", ">=1/2"} };

        String[] labelRightStrings = {  "Avvikande perkussionsljud: ", "Ocklusionskontakt: ",
                                         "Röntgen dislokation: ", "Röntgen rotfraktur: ", "Röntgen rotlängd: "};


        String[] labelTreatmentStrings = { "Antibiotika: ", "Extraktion: "   };

        String[][] comboBoxTreatmentStrings = { {"", "Nej", "Ja"}, { "", "Nej", "Ja"}};

        String[] labelDiagoseStrings = {"Hårdvävnadsskada: ", "Stödjevävnadsskada: ", "Mjukdelsskada: "};

        String[][] comboBoxDiagoseStrings = { {"", "Ingen", "Infraktion", "Okomplicerad kronfraktur", "Komplicerad kronfraktur", "Okomplicerad kronrotfraktur", "Komplicerad kronrotfraktur", "Rotfraktur"},
                                       { "", "Ingen", "Concussion", "Subluxation", "Extrusion", "Laterial luxation", "Intrusionsluxation", "Exartikulation"},
                                       { "", "Ingen","Gingival abrasion", "Gingival laceration", "Gingival contusion"}};

        JComboBox cmbBox;
        JLabel labelCombo;
        JComboBox[] arrayComboBoxes = new JComboBox[NUMBER_OF_COMBOS];

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,15,0,0);
        c.weightx = 0.5;

        //create all input components to the left
        for(int i = 0; i<labelLeftStrings.length; i++)
        {
            cmbBox = new JComboBox(comboBoxLeftStrings[i]);
            cmbBox.setPreferredSize(new Dimension(60,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(60,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(60,COMBO_HEIGHT));
            labelCombo = new JLabel(labelLeftStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = 0;
            c.gridy = i;
            panel.add(labelCombo,c);

            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1;
            c.gridy = i;
            panel.add(cmbBox,c);
            arrayComboBoxes[i] = cmbBox;
        }

        //create all input components to the right
        for(int i = 0; i<labelRightStrings.length; i++)
        {
            cmbBox = new JComboBox(comboBoxRightStrings[i]);
            cmbBox.setPreferredSize(new Dimension(80,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(80,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(80,COMBO_HEIGHT));
            labelCombo = new JLabel(labelRightStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = 4;
            c.gridy = i;
            panel.add(labelCombo,c);

            c.anchor = GridBagConstraints.WEST;
            c.gridx = 5;
            c.gridy = i;
            panel.add(cmbBox,c);
            arrayComboBoxes[labelLeftStrings.length+i] = cmbBox;
        }
        int col = 0;
        int row = 5;
        //create a line separator
        JSeparator separator = new JSeparator();

        c.gridwidth = 6;
        c.gridx = col;
        c.gridy = row;
        panel.add(separator,c);

        c.gridwidth = 1;



        //create the bottom left controls (diagnose)
        for(int i = 0; i<labelDiagoseStrings.length; i++)
        {
            cmbBox = new JComboBox(comboBoxDiagoseStrings[i]);
            cmbBox.setPreferredSize(new Dimension(150,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(150,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(150,COMBO_HEIGHT));
            labelCombo = new JLabel(labelDiagoseStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = 0;
            c.gridy = i+6;
            panel.add(labelCombo,c);

            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1;
            c.gridy = i+6;
            panel.add(cmbBox,c);
            arrayComboBoxes[labelLeftStrings.length+labelRightStrings.length+i] = cmbBox;
        }

        //create the bottom right controls (treatment)
        for(int i = 0; i<labelTreatmentStrings.length; i++)
        {
            cmbBox = new JComboBox(comboBoxTreatmentStrings[i]);
            cmbBox.setPreferredSize(new Dimension(80,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(80,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(80,COMBO_HEIGHT));
            labelCombo = new JLabel(labelTreatmentStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = 4;
            c.gridy = i+6;
            panel.add(labelCombo,c);

            c.anchor = GridBagConstraints.WEST;
            c.gridx = 5;
            c.gridy = i+6;
            panel.add(cmbBox,c);
            arrayComboBoxes[labelLeftStrings.length+labelRightStrings.length+labelDiagoseStrings.length+i] = cmbBox;
        }
       // add the extra text input mm:
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 2;
        c.gridy = 2;
        labelCombo = new JLabel("mm: ");
        panel.add(labelCombo,c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 3;
        c.gridy = 2;
        JTextField textmm = new JTextField();
        textmm.setPreferredSize(new Dimension(50,COMBO_HEIGHT));
        textmm.setMaximumSize(new Dimension(50,COMBO_HEIGHT));
        textmm.setMinimumSize(new Dimension(50,COMBO_HEIGHT));
        panel.add(textmm,c);

        mComboBoxes.add(arrayComboBoxes);
        mTextmm.add(textmm);
        return panel;
    }

    //creates a tree with all the information that should be saved to file
    public Tree buildTree()
    {
        TreeBranch rootNode = new TreeBranch("traumaFlik3");
        TreeBranch locationBranch;
        TreeLeaf leaf;

        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {

            //add the comboboxes
            JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                int v=i+1;
                if(i>2)
                {
                    v=i+2;
                }
                //add the combo boxes
                locationBranch = new TreeBranch("D3" + getTeethButtons().get(toothNr) + "" + (v));
                if(arrayComboBoxes[i].getSelectedIndex()>0)
                {
                    if(v==10) // Röntgen rotlängd starts on 1 instead of 0
                    {
                        leaf = new TreeLeaf((arrayComboBoxes[i].getSelectedIndex())+"");
                    }
                    else
                    {
                        leaf = new TreeLeaf((arrayComboBoxes[i].getSelectedIndex()-1)+"");
                    }
                    locationBranch.addChild(leaf);
                }
                rootNode.addChild(locationBranch);
            }
            //add the textbox
            locationBranch = new TreeBranch("D3" + getTeethButtons().get(toothNr) + "4");
            leaf = new TreeLeaf(mTextmm.get(toothNr).getText());
            locationBranch.addChild(leaf);
            rootNode.addChild(locationBranch);
        }
        return rootNode;

    }

    //extracts the information from a tree that belongs to trauma status and
    //sets all the components (textboxes, comboboxes etc.)
    public void loadTree(Tree t)
    {
        String term = "TraumaStatusRoot";

        Tree aNode = t.getNode(term);

		if(aNode != null)
		{
            for(int i = 0; i < getNumberOfTeethButtons() ; i++)
            {
                for(int k=0;k< NUMBER_OF_COMBOS;k++)
                {
                   Tree aNode2 = aNode.getNode("TraumaStatus(tooth" + i + "," + k + ")");
                    if(aNode2 != null)
                    {
                        //should only contain one element, but...
                        for (Enumeration aLeaf = aNode2.getChildrenEnumeration(); aLeaf.hasMoreElements(); )
                        {
                            Tree   theLeaf     = (Tree) aLeaf.nextElement();
                            int valInt      = Integer.parseInt(theLeaf.getValue());
                            mComboBoxes.get(i)[k].setSelectedIndex(valInt);
                        }
                    }
                    aNode2 = aNode.getNode("TraumaStatus(tooth" + i + ",mm)");
                    if(aNode2 != null)
                    {
                        //should only contain one element, but...
                        for (Enumeration aLeaf = aNode2.getChildrenEnumeration(); aLeaf.hasMoreElements(); )
                        {
                            Tree   theLeaf     = (Tree) aLeaf.nextElement();
                            String valString      = theLeaf.getValue();
                            mTextmm.get(i).setText(valString);
                        }
                    }

                }
            }
        }
        else
        {
            //No TraumaStatusdata found
        }
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

        return (!mTextmm.get(tooth).getText().equals("") || selected>0);
    }

    //just a test method
    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        TraumaStatusMilkInputComponent debugComponent = new TraumaStatusMilkInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Mineralization component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void createTermValuesFile()
    {
        System.out.println("------- Trauma Flik 3 ----------");
        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            //add the comboboxes
            JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                int v=i+1;
                if(i>2)
                {
                    v=i+2;
                }
                //print the term
                System.out.println("\n$D3" + getTeethButtons().get(toothNr) + "" + v);
                //print all values for this term
                for(int k=1;k<arrayComboBoxes[i].getItemCount();k++)
                {
                    String s = (String)arrayComboBoxes[i].getItemAt(k);
                    System.out.println(s);
                }
            }
            System.out.println("\n$D3" + getTeethButtons().get(toothNr) + "4");
        }
    }

}
