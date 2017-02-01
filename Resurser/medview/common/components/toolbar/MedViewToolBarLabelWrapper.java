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
public class MedViewToolBarLabelWrapper extends JPanel implements GUIConstants
{
	private JLabel wrappedLabel = null;
	
	private Dimension panelDimension = null;
	
	private Dimension labelDimension = null;
	
	public MedViewToolBarLabelWrapper(JLabel label)
	{		
		this.wrappedLabel = label;
		
		// layout wrapper panel
		
		setLayout(new GridBagLayout());	
		
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		GUIUtilities.gridBagAdd(this, wrappedLabel, 0, 0, 1, 1, 0, 0, CENT, new Insets(1,2,1,2), NONE);
		
		// set up sizes
		
		labelDimension = wrappedLabel.getPreferredSize();
		
		panelDimension = new Dimension(labelDimension.width + 4, labelDimension.height + 2);
		
		configureUI();
	}
	
	private void configureUI()
	{
		wrappedLabel.setPreferredSize(labelDimension);

		wrappedLabel.setMinimumSize(labelDimension);

		wrappedLabel.setMaximumSize(labelDimension);

		setPreferredSize(panelDimension); 

		setMinimumSize(panelDimension);

		setMaximumSize(panelDimension);		
	}
}
