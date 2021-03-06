package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;
import medview.datahandling.examination.tree.TreeLeaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**

 * Trauma Next Visit input component

 * @author Andreas Argirakis

 */

public class TraumaNextVisitInputComponent extends TeethInputComponent {

    private ArrayList<JComboBox[]> mComboBoxes = new ArrayList();
    final static int NUMBER_OF_COMBOS = 17;
    final static int COMBO_HEIGHT = 20;
    public TraumaNextVisitInputComponent()
    {
        String[] teethButtons = { "13", "12", "11", "21", "22", "23",
                                  "43", "42", "41", "31", "32", "33"};
        setTeethButtons(teethButtons);
        buildGUI();
        refresh();
        //createTermValuesFile();

    }
    public boolean hasData(int tooth)
    {
        int selected=0;
        JComboBox[] combos = mComboBoxes.get(tooth);
        for(int i = 0; i < combos.length; i++)
        {
            selected += combos[i].getSelectedIndex();
        }

        return (selected>0);

    }
    //Creates the panel where all the input components reside
    protected JPanel createInputPanel()
    {
        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String[][] comboBoxStrings = {  {"","Normal", "Gul", "R�d", "Gr�"},
                                        {"","0","1","2","3"},
                                        {"","-","+"},
                                        {"","-","+"},
                                        {"","-","+"},
                                        {"","-","+"},
                                        {"","-","+"},
                                        {"","-","+"},
                                        {""},
                                        {"","-","+"},
                                        {"","Nej","Partiell","Total"},
                                        {"","Nej","Ankylos","Infl. resorbtion"},
                                        {"","Nej","Apikalt","Juxtaradikul�rt","Intern resorbtion"},
                                         };

        
        String[] labelStrings = {   "Tandens f�rg: ", "Mobilitet: ", "Perkussions�m: ", "Avvikande perkussionsljud: ",
                                    "Ocklusionskontakt: ", "Fistel: ", "Gingivit: ",
                                    "Gingiva retraktion: ", "Sens test: ", "Patologisk ficka: ",
                                    "R�ntgen - pulpakanal obliteration: ", "R�ntgen - periradikul�ra f�r�ndringar: ", "R�ntgen - rotresorption: "};

        String[] labelTreatmentStrings = { "Avl�gsnande av fixering: ", "Rotbehandling: ", "Dentin t�ckning: ", "Extraktion: "};
        String[][] comboTreatmentStrings = {    {"","Nej", "Ja"},
                                                {"","Prim�r rensning","Fortsatt rensning","Rotfyllning"},
                                                {"","Provisorisk fyllning","Kompositfyllning", "Proterisk terapi"},
                                                {"","Nej","Ja"}};

        JComboBox[] arrayComboBoxes = new JComboBox[NUMBER_OF_COMBOS];

        // for the sens test
        String numbers[] = new String[102];
        numbers[0]="";
        for(int v=0;v<101;v++)
        {
            numbers[v+1]= ""+v;
        }
        comboBoxStrings[8] = numbers;
        JComboBox cmbBox;
        JLabel labelCombo;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,15,0,0);
        c.weightx = 0.5;

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 0;

        int col = 0;
        int row = 0;
        for(int i = 0; i<labelStrings.length; i++)
        {
            cmbBox = new JComboBox(comboBoxStrings[i]);
            cmbBox.setPreferredSize(new Dimension(60,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(60,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(60,COMBO_HEIGHT));
            labelCombo = new JLabel(labelStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = col;
            c.gridy = row;
            panel.add(labelCombo,c);
            c.anchor = GridBagConstraints.WEST;
            c.gridx = col+1;
            c.gridy = row;
            panel.add(cmbBox,c);
            arrayComboBoxes[i] = cmbBox;
            row++;
            if(row==7)
            {
                row=0;
                col=2;
            }
        }

        col = 0;

        //create a line separator
        row=7;
        JSeparator separator = new JSeparator();

        c.gridwidth = 4;
        c.gridx = col;
        c.gridy = row;
        panel.add(separator,c);

        c.gridwidth = 1;
        col = 2;
        row = 8;
        for(int i = 0; i<labelTreatmentStrings.length; i++)
        {
            cmbBox = new JComboBox(comboTreatmentStrings[i]);
            cmbBox.setPreferredSize(new Dimension(110,COMBO_HEIGHT));
            cmbBox.setMaximumSize(new Dimension(110,COMBO_HEIGHT));
            cmbBox.setMinimumSize(new Dimension(110,COMBO_HEIGHT));
            labelCombo = new JLabel(labelTreatmentStrings[i]);

            c.anchor = GridBagConstraints.EAST;
            c.gridx = col;
            c.gridy = row;
            panel.add(labelCombo,c);
            c.anchor = GridBagConstraints.WEST;
            c.gridx = col+1;
            c.gridy = row;
            panel.add(cmbBox,c);
            arrayComboBoxes[i+labelStrings.length] = cmbBox;
            row++;
        }

        mComboBoxes.add(arrayComboBoxes);
        return panel;
    }

    //creates a tree with all the information that should be saved to file
    public Tree buildTree()
    {
        TreeBranch rootNode = new TreeBranch("traumaFlik2");
        TreeBranch locationBranch;
        TreeLeaf leaf;

        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                locationBranch = new TreeBranch("D2" + getTeethButtons().get(toothNr) + "" + (i+1));
                if(arrayComboBoxes[i].getSelectedIndex()>0)
                {
                    leaf = new TreeLeaf((arrayComboBoxes[i].getSelectedIndex()-1)+"");
                    locationBranch.addChild(leaf);
                }
                rootNode.addChild(locationBranch);
            }
        }
        return rootNode;
    }

    //extracts the information from a tree that belongs to trauma next visit and
    //sets all the components (textboxes, comboboxes etc.)
    public void loadTree(Tree t)
    {
        String term = "TraumaNextVisit";

        Tree aNode = t.getNode(term);

		if(aNode != null)
		{
            for(int i = 0; i < getNumberOfTeethButtons() ; i++)
            {
                for(int k=0;k<NUMBER_OF_COMBOS;k++)
                {
                   Tree aNode2 = aNode.getNode("TraumaNextVisit(tooth" + i + "," + k + ")");
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
                }
            }
        }
        else
        {
            //No traumaNextVisitdata found
        }
    }

    //just a test method
    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        TraumaNextVisitInputComponent debugComponent = new TraumaNextVisitInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Mineralization component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void createTermValuesFile()
    {
        System.out.println("------- Trauma Flik 2 ----------");
        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            //add the comboboxes
            JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
            for(int i=0;i<arrayComboBoxes.length;i++)
            {
                //print the term
                System.out.println("\n$D2" + getTeethButtons().get(toothNr) + "" + (i+1));
                //print all values for this term
                for(int k=1;k<arrayComboBoxes[i].getItemCount();k++)
                {
                    String s = (String)arrayComboBoxes[i].getItemAt(k);
                    System.out.println(s);
                }
            }
        }
    }

}
