package medview.common.dialogs;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import medview.datahandling.*;

import misc.foundation.*;

public class MedViewSplashWindow extends JWindow
{
	public void show()
	{
		super.show();

		toFront();
	}

	public ProgressNotifiable getProgressNotifiable()
	{
		return progressNotifiable;
	}

	private void layoutWindow()
	{
		int cS = componentSeparation;

		int pL = padding.left; int pR = padding.right;

		int pT = padding.top; int pB = padding.bottom;

		Dimension sLPS = splashLabel.getPreferredSize();

		Dimension lLPS = loadingLabel.getPreferredSize();

		Dimension dTAPS = developerTextArea.getPreferredSize();

		int pW = 1 + pL + sLPS.width + pR + 1; int pH = -1;

		if (style == IMAGE_LOAD_DEVELOPER_STYLE)
		{
			pH = 1 + pT + sLPS.height + cS + lLPS.height + cS + dTAPS.height + pB + 1;
		}
		else
		{
			pH = 1 + pT + sLPS.height + cS + dTAPS.height + pB + 1;
		}

		developerTextArea.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		setSize(new Dimension(pW, pH));

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		Border b1 = BorderFactory.createLineBorder(Color.black,1);

		Border b2 = BorderFactory.createEmptyBorder(pT,pL,pB,pR);

		Border b = BorderFactory.createCompoundBorder(b1,b2);

		getContentPane().setBackground(splashBackground);

		((JComponent)getContentPane()).setBorder(b);

		getContentPane().setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,0,cS,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		getContentPane().add(splashLabel, gbc);

		if (style == IMAGE_LOAD_DEVELOPER_STYLE)
		{
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets = new Insets(0,0,cS,0);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			getContentPane().add(loadingLabel, gbc);

			gbc.gridy = 2;	// developer text area grid y
		}
		else
		{
			gbc.gridy = 1;	// developer text area grid y
		}

		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.weighty = 100;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
        developerTextArea.setLineWrap(true);
        developerTextArea.setFont(developerTextArea.getFont().deriveFont(Font.PLAIN, 9));

        getContentPane().add(developerTextArea, gbc);
	}

	public MedViewSplashWindow(Image im, String devLS,
		String initLoadLS, Color splashBG, int style)
	{
		// members

		this.style = style;
		
		this.developerLS = devLS;

		this.initialLoadLS = initLoadLS;

		this.splashBackground = splashBG;


		// other members

		componentSeparation = 3;

		padding = new Insets(5,5,5,5);

		developerText = mVDH.getLanguageString(developerLS);

		initialLoadText = mVDH.getLanguageString(initialLoadLS);

		loadingLabel = new JLabel(initialLoadText);

		loadingLabel.setOpaque(false);

		splashLabel = new JLabel(new ImageIcon(im));

		developerTextArea = new JTextArea(developerText);

		developerTextArea.setFont(new JLabel().getFont());

		developerTextArea.setEditable(false);

		developerTextArea.setOpaque(false);

		// window layout

		layoutWindow();

		setLocationRelativeTo(null);	// center window on screen

		// create the progress notifiable

		progressNotifiable = new SplashProgressNotifiable();
	}

	private int style;

	private String developerText;

	private String initialLoadText;

	private String developerLS;

	private String initialLoadLS;

	private Color splashBackground;

	private ProgressNotifiable progressNotifiable;


	private Insets padding;

	private int componentSeparation;


	private JLabel splashLabel;

	private JLabel loadingLabel;

	private JTextArea developerTextArea;


	private static final MedViewDataHandler mVDH = MedViewDataHandler.instance();


	public static final int IMAGE_DEVELOPER_STYLE = 1;

	public static final int IMAGE_LOAD_DEVELOPER_STYLE = 2;



	private class SplashProgressNotifiable extends DefaultProgressNotifiable
	{
		public void setCurrent(int c)
		{
			super.setCurrent(c);

			updateLoadingLabel();
		}

		public void setTotal(int t)
		{
			super.setTotal(t);

			updateLoadingLabel();
		}

		public void setDescription(String desc)
		{
			super.setDescription(desc);

			updateLoadingLabel();
		}

		private void updateLoadingLabel()
		{
			String desc = getDescription();

			int current = getCurrent(); int total = getTotal();

			loadingLabel.setText(desc + " (" + current + "/" + total + ")");
		}
	}
}
