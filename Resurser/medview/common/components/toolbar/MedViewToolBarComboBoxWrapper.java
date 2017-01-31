package medview.common.components.toolbar;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MedViewToolBarComboBoxWrapper extends JPanel implements GUIConstants
{
	private JComboBox wrappedComboBox = null;
	
	private Dimension panelDimension = null;
	
	private Dimension comboDimension = null;
	
	public MedViewToolBarComboBoxWrapper(JComboBox comboBox)
	{		
		this.wrappedComboBox = comboBox;
		
		// layout wrapper panel
		
		setLayout(new GridBagLayout());	
		
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		GUIUtilities.gridBagAdd(this, wrappedComboBox, 0, 0, 1, 1, 0, 0, CENT, new Insets(1,2,1,2), NONE);
		
		// set up sizes
		
		comboDimension = wrappedComboBox.getPreferredSize();
		
		panelDimension = new Dimension(comboDimension.width + 4, comboDimension.height + 2);
		
		configureUI();
	}
	
	private void configureUI()
	{
		wrappedComboBox.setPreferredSize(comboDimension);

		wrappedComboBox.setMinimumSize(comboDimension);

		wrappedComboBox.setMaximumSize(comboDimension);

		setPreferredSize(panelDimension); 

		setMinimumSize(panelDimension);

		setMaximumSize(panelDimension);		
	}
}
