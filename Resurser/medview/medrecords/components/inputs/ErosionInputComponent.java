package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.Tree;
import medview.datahandling.examination.tree.TreeBranch;
import medview.datahandling.examination.tree.TreeLeaf;
import medview.datahandling.MedViewLanguageConstants;
import medview.datahandling.MedViewDataHandler;


import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**

 * Erosion input component

 * @author Andreas Argirakis

 */

public class ErosionInputComponent extends TeethInputComponent {

    private final static String EROSION_CONSTANT = "ER";
    private ArrayList<JComboBox[]> mComboBoxes = new ArrayList();
    public ErosionInputComponent()
    {
        String[] teethButtons = { "17","16", "15", "14", "13", "12", "11", "21", "22", "23", "24", "25", "26", "27",
                                  "47", "46", "45", "44", "43", "42","41", "31", "32", "33", "34", "35", "36", "37"};
        setTeethButtons(teethButtons);
        setEnabledToothButton(0,false);

        setEnabledToothButton(2,false);
        setEnabledToothButton(3,false);
        setEnabledToothButton(4,false);


        setEnabledToothButton(9,false);
        setEnabledToothButton(10,false);
        setEnabledToothButton(11,false);

        setEnabledToothButton(13,false);

        setEnabledToothButton(14,false);

        setEnabledToothButton(16,false);
        setEnabledToothButton(17,false);
        setEnabledToothButton(18,false);
        setEnabledToothButton(19,false);
        setEnabledToothButton(20,false);
        setEnabledToothButton(21,false);
        setEnabledToothButton(22,false);
        setEnabledToothButton(23,false);
        setEnabledToothButton(24,false);
        setEnabledToothButton(25,false);
        setEnabledToothButton(27,false);


        buildGUI();
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
    protected JPanel createInputPanel()
    {
        JPanel headPanel = new JPanel(new BorderLayout());
        String[] comboBoxStrings = { "", "0", "1", "2", "3", "4"};
        String[] labelStrings = { "Ocklusalt: ", "Palatinalt: ", "Buckalt: "};
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(10,0,0,0);  //top padding
        c.weightx = 0.5;

        JComboBox[] arrayComboBoxes = new JComboBox[3];
        JComboBox cmbBox;
        JLabel label;
        
        JButton b;
        b = new JButton(MedViewDataHandler.instance().getLanguageString(MedViewLanguageConstants.BUTTON_EROSION_ALL_OK));
        b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//set all to zero
                for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
                {
                    //only enabled teeth
                    if(isToothButtonEnabled(toothNr))
                    {
                        JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
                        for(int i=0;i<arrayComboBoxes.length;i++)
                        {
                            arrayComboBoxes[i].setSelectedIndex(1);
                        }
                    }
                }
                refresh();
            }
		});
        panel.add(b);
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<3;j++)
            {
                label = new JLabel(labelStrings[j]);
                cmbBox = new JComboBox(comboBoxStrings);
                cmbBox.setPreferredSize(new Dimension(150,20));
                cmbBox.setMinimumSize(new Dimension(80,20));

                c.gridx = i;
                c.gridy = j;
                if(i==0)
                {
                    c.anchor = GridBagConstraints.EAST;
                    panel.add(label,c);
                }
                else
                {
                    c.anchor = GridBagConstraints.WEST;
                    panel.add(cmbBox,c);
                }
                arrayComboBoxes[j] = cmbBox;
            }
        }
        mComboBoxes.add(arrayComboBoxes);
        JPanel bottomPanel = new JPanel();

        String infoLeft = "Överkäksincisiver:\n\n";
        infoLeft += "0 = Inga synbara förändringar, utvecklingsstrukturer kvarstår; makromorfologi intakt\n\n";
        infoLeft += "1 = Utjämnad emalj, utvecklingsstrukturer har helt eller delvis försvunnit; emaljen blank, matt, ”smält”, rundad eller platt; makromorfologi relativt intakt\n\n";
        infoLeft += "2 = Emaljyta enligt grad 1; makromorfologi tydligt förändrad, facettering eller konkaviteter i emalj; ingen dentinexponering\n\n";
        infoLeft += "3 = Emaljytan enligt grad 1 & 2; makromorfologi gravt förändrad, dentinexponering <1/3\n\n";
        infoLeft += "4 = Emaljytan enligt grad 1, 2 & 3; dentinexponering >1/3 eller pulpan synligt genom dentin\n\n";
        JTextArea lTextArea = new JTextArea(infoLeft,18,30);
        lTextArea.setEditable(false);
        lTextArea.setLineWrap(true);
        lTextArea.setWrapStyleWord(true);
        lTextArea.setOpaque(false);

        String infoRight = "\tMolarer:\n\n";
        infoRight += "\t0 = Intakt yta\n\n";
        infoRight += "\t1 = Rundad kusp\n\n";
        infoRight += "\t2 = Cupping 0-1 mm, punktform\n\n";
        infoRight += "\t3 = Cupping>1mm, ej sammansmält med annan cupping\n\n";
        infoRight += "\t4 = Samlad cupping\n\n";
        JTextArea rTextArea = new JTextArea(infoRight,18,30);
        rTextArea.setEditable(false);
        rTextArea.setLineWrap(true);
        rTextArea.setWrapStyleWord(true);
        rTextArea.setOpaque(false);

        JScrollPane textAreaLeftScrollPane = new JScrollPane(lTextArea);

		textAreaLeftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textAreaLeftScrollPane.setBorder(null);
        bottomPanel.add(textAreaLeftScrollPane);

        JScrollPane textAreaRightScrollPane = new JScrollPane(rTextArea);

		textAreaRightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textAreaRightScrollPane.setBorder(null);
        bottomPanel.add(textAreaRightScrollPane);
        bottomPanel.setLayout(new GridLayout());
        headPanel.add(panel,BorderLayout.NORTH);
        headPanel.add(bottomPanel,BorderLayout.SOUTH);

        return headPanel;
    }


    public Tree buildTree()
    {
        TreeBranch rootNode = new TreeBranch("erosionRoot");
        TreeBranch locationBranch;
        TreeLeaf leaf;

        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            //only add enabled teeth
            if(isToothButtonEnabled(toothNr))
            {

                JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
                for(int i=0;i<arrayComboBoxes.length;i++)
                {
                    locationBranch = new TreeBranch(EROSION_CONSTANT + getTeethButtons().get(toothNr) + "" + (i+1));
                    if(arrayComboBoxes[i].getSelectedIndex()>0)
                    {
                        leaf = new TreeLeaf((arrayComboBoxes[i].getSelectedIndex()-1)+"");
                        locationBranch.addChild(leaf);
                    }
                    rootNode.addChild(locationBranch);
                }
            }
        }
        return rootNode;
    }

    public void loadTree(Tree t)
    {
        String term = "mineralizationRoot";

        Tree aNode = t.getNode(term);
/*
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
    public void createTermValuesFile()
    {
        System.out.println("------- Erosion ----------");
        for(int toothNr=0;toothNr<getInputPanels().size();toothNr++)
        {
            //only add enabled teeth
            if(isToothButtonEnabled(toothNr))
            {


                JComboBox[] arrayComboBoxes = mComboBoxes.get(toothNr);
                for(int i=0;i<arrayComboBoxes.length;i++)
                {
                    System.out.println("\n$" + EROSION_CONSTANT + getTeethButtons().get(toothNr) + "" + (i+1));
                    for(int j = 1; j<6 ; j++)
                    {
                        System.out.println(arrayComboBoxes[i].getItemAt(j));
                    }
                    /*if(arrayComboBoxes[i].getSelectedIndex()>0)
                    {
                        leaf = new TreeLeaf((arrayComboBoxes[i].getSelectedIndex()-1)+"");
                        locationBranch.addChild(leaf);
                    }*/
                }
            }
        }
    }

    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        ErosionInputComponent debugComponent = new ErosionInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Erosion component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
