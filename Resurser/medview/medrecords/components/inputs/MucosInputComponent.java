/*
 * $Id: MucosInputComponent.java,v 1.14 2008/09/04 08:24:50 it2aran Exp $
 *
 * $Log: MucosInputComponent.java,v $
 * Revision 1.14  2008/09/04 08:24:50  it2aran
 * Some cleanup
 *
 * Revision 1.13  2008/08/12 08:00:05  it2aran
 * Some cleanup
 *
 * Revision 1.12  2008/06/12 09:21:22  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textfält om man sparar med felaktigt infyllt textfält.
 * 164: Tabbning mellan inputs scrollar hela formuläret så att den aktuella inputen alltid är synlig
 * Övrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM för att tilldela mer minne så att större bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar bättre in samt att inga nollor sparas om inget är ifyllt.
 * Drag'n'drop för bilder fungerade ej och är borttaget tills vidare
 * Text i felmeddelandet vid inmatat värde utan att trycka på enter ändrat
 *
 * Revision 1.11  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.10  2006/05/29 18:32:49  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.9  2005/04/26 12:28:30  erichson
 * Removed a leftover println
 *
 * Revision 1.8  2005/02/18 09:08:38  erichson
 * Infection saves properly now
 *
 * Revision 1.7  2005/02/16 15:55:21  erichson
 * Changed infection types from 1-choice to multiple choice.
 *
 * Revision 1.6  2005/01/25 11:05:35  Nils Erichson
 * Fixed "unknown infection input" error
 *
 * Revision 1.5  2005/01/25 10:20:54  erichson
 * Cleaned constants.
 *
 * Revision 1.4  2004/12/20 17:44:56  erichson
 * Merging second development branch. Almost there!
 *
 * Revision 1.3  2004/12/07 13:57:25  erichson
 * Mucos -> Mucositis
 *
 * Revision 1.2  2004/12/06 19:24:21  erichson
 * Merging development branch to keep MedRecords compilable
 *
 * Revision 1.1  2004/12/02 08:46:47  erichson
 * Checking in first experimental version (with jRadioButtons)
 *
 *
 * Created on December 1, 2004, 4:07 PM
 *
 *
 */

package medview.medrecords.components.inputs;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import medview.datahandling.examination.tree.*;

/**
 * Mucositis input component
 * @author Andreas Argirakis
 */
public class MucosInputComponent extends JPanel
{
	private final static String MUCOS_ROOTNODE_NAME = "Mucos";
	
	private final static int ULCERATION = 0;
	private final static int ERYTHEMA = 1;    

    private final static String[] columnNames =
	{ "Location", "Ulceration/pseudomembrane", "Erythema" };

    private final static String[][] locationStrings =
	{ {"Upper lip", "upperlip" },
		{ "Lower lip", "lowerlip" },
		{ "Right cheek", "rightcheek" },
		{ "Left cheek", "leftcheek" },
		{ "Right ventral and lateral tongue", "rightventral_lateral" },
		{ "Left ventral and lateral tongue", "leftventral_lateral" },
		{ "Floor of moth", "floorofmoth" },
		{ "Soft palate/fauces", "softpalatefauces" },
		{ "Hard palate", "hardpalate" } };
	
	private final static int LOCATION_AMOUNT = locationStrings.length;
	
	private Integer[][] mucosScoreArray;
    private JButton[] mucosButtonArray;
    
    private Color ColorBlueGrey = new Color(0,100,180);
    
	/** 
    * Creates a new instance of MucosInputComponent
    */
	public MucosInputComponent() {

		super(new BorderLayout());
		// mucos score array
        // all values are null
        mucosScoreArray = new Integer[LOCATION_AMOUNT][2];
        mucosButtonArray = new JButton[LOCATION_AMOUNT*7];

        add(createLeftPanel(), BorderLayout.WEST);
        add(createButtonPanel(), BorderLayout.CENTER);

        Font borderFont = new JLabel().getFont().deriveFont(Font.PLAIN,9);
        this.setBorder(BorderFactory.createTitledBorder(null, "Mucos", TitledBorder.LEFT,TitledBorder.TOP,borderFont,Color.black));

        refreshGUI();

    }
    
    /**
    * Creates the left panel with the titles and the header on top
    * @return a JPanel with all buttons 
    */
    private JPanel createLeftPanel()
    {   
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);  //top padding
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        
        JLabel label0 = new JLabel(columnNames[0]);
        Font font = new JLabel().getFont().deriveFont(Font.BOLD,12);
        label0.setForeground(ColorBlueGrey);
        label0.setFont(font);
        label0.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT,TitledBorder.TOP,font,Color.black));
        panel.add(label0,c);
 
        for(int i = 0; i<LOCATION_AMOUNT; i++)
        {
            c.gridx = 0;
            c.gridy = i+1;
            JLabel label = new JLabel(locationStrings[i][0]);
            label.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT,TitledBorder.TOP,font,Color.black));
            panel.add(label,c);
        }
        return panel;
    }
    
    /**
    * Creates the panel with all the buttons and the headers on top
    * @return a JPanel with all buttons 
    */
    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);  //top padding
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        
        //create the headers first
        Font font = new JLabel().getFont().deriveFont(Font.BOLD,12);
        
        c.gridwidth = 4;
        JLabel label1 = new JLabel(columnNames[1]);
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setForeground(ColorBlueGrey);
        label1.setFont(font);
        
        label1.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT,TitledBorder.TOP,font,Color.black));
        panel.add(label1,c);
        
        c.gridx = 4;
        c.gridwidth = 3;
        JLabel label2 = new JLabel(columnNames[2]);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setForeground(ColorBlueGrey);
        label2.setFont(font);
        label2.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT,TitledBorder.TOP,font,Color.black));
        panel.add(label2,c);
        
        // now create all the buttons
        int j = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        JButton button;
        for(int i = 0; i<LOCATION_AMOUNT*7; i++)
        {
            c.gridx = i%7;
            c.gridy = (i/7)+1;
            if(j==4 || (j==3 && i%7==0)) j=0;
            button = new JButton(j+"");
            int type;
            if(i%7<4)
            {
                type=0;
            }
            else
            {
                type=1;
            }
            button.addActionListener(createButtonListener(j, type, i/7));
            button.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT,TitledBorder.TOP,font,Color.black));
            panel.add(button,c);
            mucosButtonArray[i] = button;
            j++;

        }

        return panel;
    }
    
    /**
    * Creates an actionlistener for the button specified
    * @param value the value the button should have
    * @param type the type of the button. Could be 0 or 1: ULCERATION or ERYTHEMA
    * @param row the row the button should be in
    * @return an ActionListener to use with the button
    */
    private ActionListener createButtonListener(int value, int type, int row)
    {
        final int datavalue = value;
        final int datatype = type;
        final int datarow = row;
        return new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(buttonHasValue(datarow, datatype, datavalue)) //if the current button has a value set, unset it
                    {
                        mucosScoreArray[datarow][datatype]=null;
                    }
                    else
                    {
                        mucosScoreArray[datarow][datatype]=datavalue;
                    }
                    refreshGUI();
                }
            };
    }
    
    /**
    * Tests if a given button has a given value
    * @param row the row the button is in
    * @param type the type of the button. Could be 0 or 1: ULCERATION or ERYTHEMA
    * @param value the value to test
    * @return true if the button has the value provided, otherwise false
    */
    private boolean buttonHasValue(int row, int type, int value)
    {
        if(mucosScoreArray[row][type]==null)
        {
            return false;
        }
        else if(mucosScoreArray[row][type]==value)
        {
            return true;
        }
        return false;
    }
    
    /**
    * Redraws the GUI. Sets the correct color of all buttons etc.
    */
    public void refreshGUI()
    {
        //reset color
        for(int b = 0; b<LOCATION_AMOUNT*7; b++)
        {
            mucosButtonArray[b].setBackground(Color.white);
        }
        for(int i =0; i<LOCATION_AMOUNT; i++ )
        {
            for(int j =0; j<2; j++ )
            {
                if(mucosScoreArray[i][j]!=null)
                {
                    int secondCol = (j == 0) ? 0 : 4;
                    Color buttonColor;
                    switch(mucosScoreArray[i][j])
                    {
                        case 0:
                            buttonColor = Color.green;
                            break;
                        case 1:
                            buttonColor = Color.yellow;
                            break;
                        case 2:
                            buttonColor = Color.orange;
                            break;
                        case 3:
                            buttonColor = Color.red;
                            break;
                        default:
                            buttonColor = Color.black;
                            System.err.println("Programming error: buttonValue = " + mucosScoreArray[i][j]);
                            break;
                    }
                    mucosButtonArray[(i*7+secondCol)+mucosScoreArray[i][j]].setBackground(buttonColor);
                }
            }
        }
    }
    /**
    * Presets the Mucos input according to the values in the given tree.
    * @param pTree a tree containing values for the Mucos input. 
    */
	public void setValues(Tree pTree) {
		/* Iterates over all locations and sets the mucos scores. */
		for (int i = 0; i < locationStrings.length; i++) {
			/* Sets the mucos score for ulceration */
			String termName = "Mucos(ulceration," + locationStrings[i][1] + ")";
			String[] termValues = pTree.getValuesOfNodesNamed(termName);
			if (termValues.length > 0) { 
				int termValue = Integer.parseInt(termValues[0]);
				mucosScoreArray[i][ULCERATION] = termValue;
			} else {
				/* Do nothing. */
			}
			
			/* Sets the mucos score for erythema */	
			termName = "Mucos(erythema," + locationStrings[i][1] + ")";
			termValues = pTree.getValuesOfNodesNamed(termName);
			if (termValues.length > 0) { 
				int termValue = Integer.parseInt(termValues[0]);
				mucosScoreArray[i][ERYTHEMA] = termValue;
			} else {
				/* Do nothing. */
			}
		}
		refreshGUI();
	}
	
    /**
    * Creates a tree with all values in the Mucos input
    * @return A tree with all values in the Mucos input. 
    */
	public Tree buildTree() {
		TreeBranch rootNode = new TreeBranch(MUCOS_ROOTNODE_NAME);
		for (int i = 0; i < LOCATION_AMOUNT; i++) {
			String termName = "Mucos(ulceration," + locationStrings[i][1] + ")";
			TreeBranch locationBranch = new TreeBranch(termName);
            if(mucosScoreArray[i][ULCERATION]!=null)
            {
                TreeLeaf ulcerationLeaf = new TreeLeaf(mucosScoreArray[i][ULCERATION].toString());
			    locationBranch.addChild(ulcerationLeaf);
            }
            rootNode.addChild(locationBranch);
			
			termName = "Mucos(erythema," + locationStrings[i][1] + ")";
			locationBranch = new TreeBranch(termName);
            if(mucosScoreArray[i][ERYTHEMA]!=null)
            {
                TreeLeaf erythemaLeaf = new TreeLeaf(mucosScoreArray[i][ERYTHEMA].toString());
			    locationBranch.addChild(erythemaLeaf);
            }
            rootNode.addChild(locationBranch);
		}
		
		TreeBranch infectionBranch = new TreeBranch("Mucos(infection)");
 
		rootNode.addChild(infectionBranch);          
		return rootNode;
	}
	

	/**
	* This is just used for testing
    * @param args command line arguments
	*/
    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        MucosInputComponent debugComponent = new MucosInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("mucos component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
