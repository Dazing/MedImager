/**
 * @(#) MedImager.java
 *
 *    $Id: MedImager.java,v 1.5 2010/07/01 17:40:53 oloft Exp $
 * 
 */

package medview.medimager;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

import java.awt.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;

import medview.medimager.model.*;
import medview.medimager.view.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * The MedImager application.
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedImager
{
	// LOOK-AND-FEEL SETUP

	public static void setupLookAndFeel()
	{
		//PlasticLookAndFeel.setMyCurrentTheme(new DesertBlue()); <- to fix Java 5 look-and-feel bug

		UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

		try
		{
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		}
		catch (Exception exc)
		{
			exc.printStackTrace();

			System.exit(1);
		}
	}

	public static void setupTextGeneration()
	{
		System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");

		System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");

		System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");

		System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");
	}

	public static void main( String[] args )
	{
		setupLookAndFeel();

		setupTextGeneration();

		// set language

		try
		{
			MedViewDataHandler.instance().changeLanguage("Svenska");
		}
		catch (LanguageException exc)
		{
			exc.printStackTrace();

			System.exit(1);
		}

		// create model

		MedImagerModel model = new MedImagerModel();

		// create frame attached to model

		MedImagerFrame medImagerFrame = new MedImagerFrame(model);

		medImagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// show main frame

		medImagerFrame.show();

		// create and position usability frame

		if ((args.length == 1) && args[0].equalsIgnoreCase("-layers")) // only if '-layers' is given as argument
		{
			UsabilityFrame usabilityFrame = new UsabilityFrame(model.getUsabilityModel());

			usabilityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			Dimension usabilityFrameSize = usabilityFrame.getSize();

			Dimension medImagerFrameSize = medImagerFrame.getSize();

			Point medImagerFramePosition = medImagerFrame.getLocation();

			Point usabilityFramePosition = new Point(

				medImagerFramePosition.x + medImagerFrameSize.width / 2 - usabilityFrameSize.width / 2,

				medImagerFramePosition.y - usabilityFrameSize.height);

			if (usabilityFramePosition.y >= 0)
			{
				usabilityFrame.setLocation(usabilityFramePosition);
			}
			else
			{
				usabilityFrame.setLocationRelativeTo(null); // center
			}

			usabilityFrame.show();
		}
	}

	private static class UsabilityFrame extends JFrame
	{
		public UsabilityFrame(final UsabilityModel model)
		{
			super("Layer switcher");

			// create label table dictionary

			Hashtable table = new Hashtable(UsabilityModel.UPPERMOST_LAYER + 1);

			table.put(new Integer(UsabilityModel.LAYER_SEARCH), new JLabel("Search"));

			table.put(new Integer(UsabilityModel.LAYER_STORE_AWAY), new JLabel("Store"));

			table.put(new Integer(UsabilityModel.LAYER_ORGANIZE), new JLabel("Organize"));

			table.put(new Integer(UsabilityModel.LAYER_EDIT), new JLabel("Edit"));

			table.put(new Integer(UsabilityModel.LAYER_SHARE), new JLabel("Share"));

			// create slider

			final JSlider slider = new JSlider(UsabilityModel.LOWERMOST_LAYER,

				UsabilityModel.UPPERMOST_LAYER, model.getFunctionalLeadLayer());

			slider.setLabelTable(table);

			slider.setPaintTicks(true);

			slider.setPaintTrack(true);

			slider.setPaintLabels(true);

			slider.setSnapToTicks(true);

			slider.setPreferredSize(new Dimension(

				slider.getPreferredSize().width * 2,

				slider.getPreferredSize().height));

			slider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					if (slider.getValue() != model.getFunctionalLeadLayer())
					{
						System.out.println("switching lead layer to " + slider.getValue());

						model.setFunctionalLeadLayer(slider.getValue());
					}
				}
			});

			// layout, add and pack

			JPanel containerPanel = new JPanel(new FlowLayout());

			containerPanel.setBorder(BorderFactory.createEmptyBorder(2,10,2,10));

			containerPanel.add(slider);

			getContentPane().add(containerPanel);

			pack();

			setResizable(false);
		}
	}
}
