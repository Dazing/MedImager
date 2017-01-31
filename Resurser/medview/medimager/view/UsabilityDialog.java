package medview.medimager.view;

import java.awt.*;

import javax.swing.*;

import misc.gui.components.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

public class UsabilityDialog extends JDialog implements GUIConstants
{
	// user level button group

	private JLabel userLevelDescriptionLabel;

	private ButtonGroup userButtonGroup;

	private JRadioButton userSimpleRadioButton;

	private JRadioButton userNormalRadioButton;

	private JRadioButton userAdvancedRadioButton;

	// layer check boxes

	private JCheckBox layerOneCheckBox;

	private JCheckBox layerTwoCheckBox;

	private JCheckBox layerThreeCheckBox;

	private JCheckBox layerFourCheckBox;

	private JCheckBox layerFiveCheckBox;

	private JCheckBox layerSixCheckBox;

	private MultiLineLabel layerOneTextLabel;

	private MultiLineLabel layerTwoTextLabel;

	private MultiLineLabel layerThreeTextLabel;

	private MultiLineLabel layerFourTextLabel;

	private MultiLineLabel layerFiveTextLabel;

	private MultiLineLabel layerSixTextLabel;

	// buttons

	private JButton okButton;

	// constructor

	public UsabilityDialog(Frame owner)
	{
		super(owner, "Användarinställningar", true);

		Container contentPane = this.getContentPane();

		contentPane.setLayout(new GridBagLayout());

		setSize(new Dimension(400,500));

		// user level

		userLevelDescriptionLabel = new JLabel("Hur bedömer du din datorvana?");

		userSimpleRadioButton = new JRadioButton("Nybörjare");

		userSimpleRadioButton.setHorizontalAlignment(SwingConstants.CENTER);

		userNormalRadioButton = new JRadioButton("Normal");

		userNormalRadioButton.setHorizontalAlignment(SwingConstants.CENTER);

		userAdvancedRadioButton = new JRadioButton("Avancerad");

		userAdvancedRadioButton.setHorizontalAlignment(SwingConstants.CENTER);

		userButtonGroup = new ButtonGroup();

		userButtonGroup.add(userSimpleRadioButton);

		userButtonGroup.add(userNormalRadioButton);

		userButtonGroup.add(userAdvancedRadioButton);

		// layer one

		layerOneCheckBox = new JCheckBox();

		layerOneCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerOneCheckBox.setVerticalAlignment(SwingConstants.TOP);

		String m1 = "Jag vill kunna söka bland bilder och se tillhörande ";

		String m2 = "patientjournal";

		layerOneTextLabel = new MultiLineLabel(m1 + m2);

		// layer two

		layerTwoCheckBox = new JCheckBox();

		layerTwoCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerTwoCheckBox.setVerticalAlignment(SwingConstants.TOP);

		m1 = "Jag vill kunna lägga undan intressanta bilder i ett fotoalbum ";

		m2 = "så att jag inte behöver söka för att se dom i framtiden";

		layerTwoTextLabel = new MultiLineLabel(m1 + m2);

		// layer three

		layerThreeCheckBox = new JCheckBox();

		layerThreeCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerThreeCheckBox.setVerticalAlignment(SwingConstants.TOP);

		m1 = "Jag vill kunna ha flera olika fotoalbum och ha möjlighet ";

		m2 = "att organisera inom dessa";

		layerThreeTextLabel = new MultiLineLabel(m1 + m2);

		// layer four

		layerFourCheckBox = new JCheckBox();

		layerFourCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerFourCheckBox.setVerticalAlignment(SwingConstants.TOP);

		m1 = "Jag vill kunna söka bland alla bilderna jag lagt till i mina ";

		m2 = "fotoalbum";

		layerFourTextLabel = new MultiLineLabel(m1 + m2);

		// layer five

		layerFiveCheckBox = new JCheckBox();

		layerFiveCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerFiveCheckBox.setVerticalAlignment(SwingConstants.TOP);

		m1 = "Jag vill kunna redigera och justera bilderna i mina fotoalbum ";

		m2 = "samt ha möjlighet att diktera till dem";

		layerFiveTextLabel = new MultiLineLabel(m1 + m2);

		// layer six

		layerSixCheckBox = new JCheckBox();

		layerSixCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		layerSixCheckBox.setVerticalAlignment(SwingConstants.TOP);

		m1 = "Jag vill kunna dela ut valda bilder ur mina fotoalbum till ";

		m2 = "kolleger inom kliniken samt till kolleger på andra kliniker";

		layerSixTextLabel = new MultiLineLabel(m1 + m2);

		// buttons

		okButton = new JButton("OK");

		// layout components

		GUIUtilities.gridBagAdd(contentPane, userLevelDescriptionLabel,	0,0, 3,1,0,0,CENT,new Insets(CGS,CGS,CCS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, userSimpleRadioButton,	0,1, 1,1,1,0,CENT,new Insets(0,CGS,CGS,CCS),NONE);

		GUIUtilities.gridBagAdd(contentPane, userNormalRadioButton,	1,1, 1,1,1,0,CENT,new Insets(0,0,CGS,CCS),NONE);

		GUIUtilities.gridBagAdd(contentPane, userAdvancedRadioButton,	2,1, 1,1,1,0,CENT,new Insets(0,0,CGS,CGS),NONE);

		GUIUtilities.gridBagAdd(contentPane, new JSeparator(),		0,2, 3,1,0,0,CENT,new Insets(0,CGS,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerOneCheckBox,		0,3, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerOneTextLabel,		1,3, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerTwoCheckBox,		0,4, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerTwoTextLabel,		1,4, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerThreeCheckBox,	0,5, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerThreeTextLabel,	1,5, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerFourCheckBox,		0,6, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerFourTextLabel,	1,6, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerFiveCheckBox,		0,7, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerFiveTextLabel,	1,7, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerSixCheckBox,		0,8, 1,1,0,1,CENT,new Insets(0,CGS,CGS,CCS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, layerSixTextLabel,		1,8, 2,1,0,0,CENT,new Insets(0,0,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, new JSeparator(),		0,9, 3,1,0,0,CENT,new Insets(0,CGS,CGS,CGS),BOTH);

		GUIUtilities.gridBagAdd(contentPane, okButton,			0,10,3,1,0,0,EAST,new Insets(0,CGS,CGS,CGS),NONE);
	}


	public static void main(String[] args)
	{
		try
		{
			(new UsabilityDialog(null)).show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
