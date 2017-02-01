package minimed.gui.exam;


import java.beans.PropertyChangeListener;
import java.lang.Integer;
import minimed.core.datahandling.NoSuchTermException;
import minimed.core.datahandling.examination.ExaminationValueContainer;
import minimed.core.datahandling.examination.tree.Tree;
import minimed.core.datahandling.examination.tree.TreeBranch;
import minimed.core.datahandling.examination.tree.TreeLeaf;
import minimed.core.datahandling.examination.tree.ExaminationValueTable;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Represents a Mucositis input.
 * 
 * @author Jens Hultberg
 */
public class MucosInput extends ScrolledComposite implements Category {
	private Composite content;
	
	private Label description;
	private Label comment;
	private MucosScale[][] scales;
	private Infection infection;
    private boolean[] infectionType = new boolean[3];
    
    private final static String MUCOS_ROOTNODE_NAME = "Mucos";
    private final static int ULCERATION = 0;
    private final static int ERYTHEMA = 1;    
    private final static int INFECTION_TYPE_LOCAL = 0;
    private final static int INFECTION_TYPE_NON_ORAL = 1;
    private final static int INFECTION_TYPE_SYSTEMIC = 2;
    private final static String INFECTION_NOINFECTION_STRING = "No";
    private final static String INFECTION_LOCAL_STRING = "Local";
    private final static String INFECTION_NONORAL_STRING = "Non-Oral";
    private final static String INFECTION_SYSTEMIC_STRING = "Systemic";
    private final static String INFECTION_PRESENT_TEXT = "Presence of infection: ";
    private final static String[] columnNames =
    { "Location", "Ulceration/pseudomembrane", "Erythema" };
	private final static String[][] locationStrings =
    { {"Upper lip", "upperlip" },
      { "Lower lip", "lowerlip" },
      { "Right cheek", "rightcheek" },
      { "Left cheek", "leftcheek" },
      { "Right ventral and lateral tongue", "rightventral_lateral" },
      { "Left ventral and lateral tongue", "leftventral_lateral" },
      { "Floor of mouth", "floorofmoth" },
      { "Soft palate/fauces", "softpalatefauces" },
      { "Hard palate", "hardpalate" } };
        
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pParent a composite which will be the parent of the new instance.
	 * @param pStyle the style of widget to construct.
	 * @param pContainer a container with values to preset (may be null).
	 */
	public 	MucosInput(Composite pParent, int pStyle, ExaminationValueContainer pContainer) {
		super(pParent, pStyle | SWT.H_SCROLL | SWT.V_SCROLL);
		content = new Composite(this, SWT.NONE);
		
	    this.setContent(content);
	    this.setExpandHorizontal(true);
	    this.setExpandVertical(true);
	    this.setMinHeight(320);
	    this.setMinWidth(200);
	    
		/*
		 * Sets the layout for the MucosInput and constructs
		 * the layout data used by its components. 
		 */
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		content.setLayout(layout);
		
		/*
		 * Constructs two textfields, one for the description of the input
		 * and one for the comment. The style of the description is set to
		 * bold and the style of the comment is set to italic.  
		 */
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		description = new Label(content, SWT.WRAP);
		description.setFont(getStyledFont(content, SWT.BOLD));
		description.setText("Mucos");
		description.setLayoutData(gridData);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		comment = new Label(content, SWT.WRAP);
		comment.setFont(getStyledFont(content, SWT.ITALIC));
		comment.setText("Tip: Use the horizontal view of the PDA.");
		comment.setLayoutData(gridData);
				
		/* Creates the titles using the method getStyledGridLabel() in InputComposite. */
		for (int i = 0; i < columnNames.length; i++) {
			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 1;
			Label column = new Label(content, SWT.CENTER);
			column.setText(columnNames[i]);
			column.setFont(getStyledFont(pParent, SWT.BOLD));
			column.setLayoutData(gridData);
		}
		
		/* Creates a separator below the titles. */
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		Label separator = new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(gridData);
		
		/* 
		 * Creates each row, with a location and two scales, 
		 * one for Ulceration/pseduomembrane and one for Erythema. 
		 */
		scales = new MucosScale[locationStrings.length][2];
		for (int i = 0; i < locationStrings.length; i++) {
			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 1;
			gridData.minimumWidth = 150;
			Label location = new Label(content, SWT.WRAP);
			location.setText(locationStrings[i][0]);
			location.setLayoutData(gridData);
			
			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.minimumWidth = 160;
			scales[i][ULCERATION] = new MucosScale(ULCERATION, content);
			scales[i][ULCERATION].setLayoutData(gridData);

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.minimumWidth = 125;
			scales[i][ERYTHEMA] = new MucosScale(ERYTHEMA, content);
			scales[i][ERYTHEMA].setLayoutData(gridData);
		}
		
		/* Creates the checkboxes for infection. */
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		infection = new Infection(content);
		infection.setLayoutData(gridData);
		
		/* Presets values, if given. */
		if (pContainer != null) {
			setValues(pContainer);
		}
	}
	
	/**
	 * Builds and returns a tree for the mucos input. Only adds selected values,
	 * if no values are selected a node with no children is returned.
	 * 
	 * @return a mucos node.
	 */
	public Tree getCategoryTree() {
        TreeBranch rootNode = new TreeBranch(MUCOS_ROOTNODE_NAME);
        for (int i = 0; i < locationStrings.length; i++) {
            if (scales[i][ULCERATION].isSelected()) {
            	String termName = "Mucos(ulceration," + locationStrings[i][1] + ")";
                TreeBranch locationBranch = new TreeBranch(termName);
            	TreeLeaf ulcerationLeaf = new TreeLeaf(scales[i][ULCERATION].getMucosScore());
                locationBranch.addChild(ulcerationLeaf);
                rootNode.addChild(locationBranch);	
            }
            if (scales[i][ERYTHEMA].isSelected()) {      
            	String termName = "Mucos(erythema," + locationStrings[i][1] + ")";
            	TreeBranch locationBranch = new TreeBranch(termName);
            	TreeLeaf erythemaLeaf = new TreeLeaf(scales[i][ERYTHEMA].getMucosScore());
            	locationBranch.addChild(erythemaLeaf);
            	rootNode.addChild(locationBranch);
            }
        }
                
        TreeBranch infectionBranch = new TreeBranch("Mucos(infection)");
        if (infection.infectionPresent()) {
        	if (infectionType[INFECTION_TYPE_LOCAL]) {
        		TreeLeaf infectionLeaf = new TreeLeaf(INFECTION_LOCAL_STRING);
        		infectionBranch.addChild(infectionLeaf);
        	}
        	if (infectionType[INFECTION_TYPE_NON_ORAL]) {
        		TreeLeaf infectionLeaf = new TreeLeaf(INFECTION_NONORAL_STRING);
        		infectionBranch.addChild(infectionLeaf);
        	}
        	if (infectionType[INFECTION_TYPE_SYSTEMIC]) {
        		TreeLeaf infectionLeaf = new TreeLeaf(INFECTION_SYSTEMIC_STRING);
        		infectionBranch.addChild(infectionLeaf);
        	}              
        } else if (rootNode.hasChildren()) {	
        	/* Only adds this leaf if other leafs are present. */
        	TreeLeaf infectionLeaf = new TreeLeaf(INFECTION_NOINFECTION_STRING);
        	infectionBranch.addChild(infectionLeaf);
        }              
        rootNode.addChild(infectionBranch);
        
        return rootNode;
    }
	
	/**
	 * Presets the chosen values to those given.
	 * 
	 * @param pContainer the values to be set. 
	 */
	private void setValues(ExaminationValueContainer pContainer) {
		/* Iterates over all locations and sets the mucos scores. */
		for (int i = 0; i < locationStrings.length; i++) {
			/* Sets the ulceration scale. */
			try {
				String termName = "Mucos(ulceration," + locationStrings[i][1] + ")";
				String[] termValues = pContainer.getValues(termName);
				int termValue = Integer.parseInt(termValues[0]);
				scales[i][ULCERATION].setMucosScore(termValue);
			} catch (NoSuchTermException e) {
				/* Do nothing */
			}

			/* Sets the erythema scale. */
			try {
				String termName = "Mucos(erythema," + locationStrings[i][1] + ")";
				String[] termValues = pContainer.getValues(termName);
				int termValue = Integer.parseInt(termValues[0]);
				scales[i][ERYTHEMA].setMucosScore(termValue);
			} catch (NoSuchTermException e) {
				/* Do nothing. */
			}
		}
		try {
			String[] termValues = pContainer.getValues("Mucos(infection)");
			
			/* Sets the corresponding check boxes. */
			for (int i = 0; i < termValues.length; i++ ) {
				if (termValues[i].equals(INFECTION_LOCAL_STRING)) {
					infectionType[INFECTION_TYPE_LOCAL] = true;
				} else if (termValues[i].equals(INFECTION_NONORAL_STRING)) {
					infectionType[INFECTION_TYPE_NON_ORAL] = true;
				} else if (termValues[i].equals(INFECTION_SYSTEMIC_STRING)) {
					infectionType[INFECTION_TYPE_SYSTEMIC] = true;
				} else if (termValues[i].equals(INFECTION_NOINFECTION_STRING)) {
					/* No infection present. */
					break;
				}
			}
			infection.setButtons();
		} catch (NoSuchTermException e) {
			/* Do nothing. */
		}		
	}
		
	/**
	 * Disposes the exam. 
	 */
	public void dispose() {
		for (int i = 0; i < scales.length; i++) {
			scales[i][ULCERATION].dispose();
			scales[i][ERYTHEMA].dispose();
		}
	}
	
	/**
	 * Not implemented
	 * 
	 * @param pValues an array containing the values to be set. 
	 */
    public void setValues(String[] pValues) {}
	
    /**
     * Not implemented.
     * 
     * @param pChanged true if any InputModel has changed visibility, false otherwise.
     */
    public void layoutPages(boolean pChanged) {}
    
    /**
     * Not implemented.
     * 
     * @param pListener the listener to add.
     */
	public void addPropertyChangeListener(PropertyChangeListener pListener) {}
	
	/**
	 * Actually does not set the focus but is needed for the size to be 
	 * set correctly (bit of a hack).
	 */
	public void focus() {
	    this.setMinHeight(500);
	    this.setMinWidth(465);
	}
	
	/**
	 * Not implemented.
	 * 
	 * @param pIndex the index of the page to set.
	 */
	public void setPage(int pIndex) {}
	
	/**
	 * Sets the shell text to whatever the current shell text is concatenated with
	 * the title of the category. For example, if the current shell text is
	 * <code>Minimed</code>, and the category title is <code>Oral Status</code>,
	 * the new shell text is <code>Minimed - Oral Status</code>.
	 */
	public void setShellText() {
		this.getShell().setText("Mucos");
	}
	
	/**
	 * Returns the title of the category.
	 * 
	 * @return the title of the category.
	 */
	public String getTitle() {
		return "Mucos";
	}

	/**
	 * Not implemented.
	 * 
	 * return always returns false;
	 */
	public boolean pageLeft() {
		return false;
	}
	
	/**
	 * Not implemented.
	 * 
	 * @return always returns false;
	 */
	public boolean pageRight() {
		return false;
	}
	
	/**
	 * Returns the number of pages (1).
	 * 
	 * @return the number of pages.
	 */
	public int getNumPages() {
		return 1;
	}
	
	/**
	 * Returns the current page index.
	 * 
	 * @return always returns 0.
	 */
	public int getCurrentPageIndex() {
		return 0;
	}
    
    /**
     * Creates a font that fits the current OS, and has a specified style.
     * 
	 * The style value is either one of the style constants SWT.NORMAL, 
	 * SWT.BOLD, SWT.ITALIC defined in class SWT or must be built 
	 * by bitwise OR'ing together (that is, using the int "|" operator) two 
	 * or more of these SWT style constants. 
	 * 
     * @param pControl a control to get the font data from.
     * @param pStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>.
     * @return the font created with the given data.
     */
	protected Font getStyledFont(Control pControl, int pStyle) {
		FontData[] fd = pControl.getFont().getFontData();
		for(int i = 0; i < fd.length; i++) {
			fd[i].setStyle(pStyle);
		}
		Font result = new Font(pControl.getDisplay(), fd);
		return result;
	}
	
    /*
     * For testing only;
     */
    public static void main(String[] arg) {
    	/* 
    	 * Builds an ExaminationValueTable (implements ExaminationValueContainer) used
    	 * for testing the setValues(ExaminationValueContainer pContainer) method. 
    	 */
    	ExaminationValueTable t = new ExaminationValueTable();
    	String[] s = new String[1];
    	s[0] = "3";   	
    	t.addValues("Mucos(ulceration,upperlip)", s);
    	s[0] = "1";
    	t.addValues("Mucos(ulceration,lowerlip)", s);
    	s[0] = "2";
    	t.addValues("Mucos(ulceration,rightcheek)", s);
    	s[0] = "1";
    	t.addValues("Mucos(ulceration,leftcheek)", s);
    	s[0] = "0";
    	t.addValues("Mucos(ulceration,rightventral_lateral)", s);
    	t.addValues("Mucos(ulceration,leftventral_lateral)", s);
    	t.addValues("Mucos(ulceration,floorofmoth)", s);
    	s[0] = "3";
    	t.addValues("Mucos(ulceration,softpalatefauces)", s);
    	t.addValues("Mucos(ulceration,hardpalate)", s);
    	s[0] = "0";
    	t.addValues("Mucos(erythema,upperlip)", s);
    	t.addValues("Mucos(erythema,lowerlip)", s);
    	s[0] = "1";
    	t.addValues("Mucos(erythema,rightcheek)", s);
    	s[0] = "2";
    	t.addValues("Mucos(erythema,leftcheek)", s);
    	s[0] = "0";
    	t.addValues("Mucos(erythema,rightventral_lateral)", s);
    	t.addValues("Mucos(erythema,leftventral_lateral)", s);
    	t.addValues("Mucos(erythema,floorofmoth)", s);
    	s[0] = "1";
    	t.addValues("Mucos(erythema,softpalatefauces)", s);
    	t.addValues("Mucos(erythema,hardpalate)", s);
    	
    	//s[0] = "No";
    	s = new String[2];
    	s[0] = "Systemic";
    	s[1] = "Local";
        t.addValues("Mucos(infection)", s);
    	    	
    	Display display = new Display ();
    	Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
    	MucosInput m = new MucosInput(shell, SWT.NONE, null);
    	m.setLayoutData(new GridData(GridData.FILL_BOTH));
    	m.focus();
    	//shell.setSize(800,450);
    	//shell.setSize(320,240);
    	shell.layout();
    	shell.open ();
    	while (!shell.isDisposed ()) {
    		if (!display.readAndDispatch ()) display.sleep ();
    	}
    	display.dispose ();
    }
    
	/**
	 * Represents a Mucos scale with three or four buttons with radio button behaviour.  
	 */
	private class MucosScale extends Composite implements MouseListener {
		private Label[] buttons;
		private Label selected = null;
		private Color[] colors;
		
		/**
		 * Constructs a new instance of this class.
		 * 
		 * @param pType the type of MucosScale to be created (ULCERATION or ERYTHEMA).
		 * @param pParent the parent composite.
		 */
		private MucosScale(int pType, Composite pParent) {
			super(pParent, SWT.NONE);
			
			int numberOfButtons;
			if (pType == ULCERATION) {
				numberOfButtons = 4;
			} else {
				numberOfButtons = 3;
			}
			
			/* Sets the layout of the MucosScale. */
			this.setLayout(new GridLayout(numberOfButtons, true));
			
			/* 
			 * Creates an array of colors used by the buttons;
			 * the first one (green) is for the first button, 
			 * the second one (yellow) is for the second button,
			 * the third one (orange) is for the third button,
			 * the fourth one (red) is for the fourth button.
			 * The fifth one, which is set later, is the default 
			 * system color, used by non-selected buttons.
			 */
			colors = new Color[5];
			colors[0] = this.getDisplay().getSystemColor(SWT.COLOR_GREEN);
			colors[1] = this.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
			colors[2] = new Color(this.getDisplay(), 255, 128, 0);
			colors[3] = this.getDisplay().getSystemColor(SWT.COLOR_RED);

			/* Creates a GridData to be used by the buttons. */
			GridData buttonData = new GridData(GridData.FILL_HORIZONTAL);
			buttonData.horizontalSpan = 1;
			buttonData.widthHint = 55;

			/* Creates all buttons. */
			buttons = new Label[numberOfButtons];
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] =  new Label(this, SWT.CENTER | SWT.BORDER);
				buttons[i].setText(""+i);
				buttons[i].setLayoutData(buttonData);
				buttons[i].addMouseListener(this);
			}
						
			/* Saves the default system color, to be used by non-selected buttons. */
			colors[4] = buttons[0].getBackground();
		}
		
		/**
		 * Disposes the MucosScale.
		 */
		public void dispose() {
			colors[2].dispose();
		}
		
		/**
		 * Returns the mucos score (an integer between 0 and 3 or 4)
		 * represented as a String. 
		 * 
		 * @return the mucos score. 
		 */
		public String getMucosScore() {
			return selected.getText();
		}
		
		/**
		 * Returns true if a value is selected.
		 * 
		 * @return true if a value is selected, false otherwise.
		 */
		public boolean isSelected() {
			return (selected != null);
		}
				
		/**
		 * Sets the given score. 
		 * 
		 * @param pScore the score to set.
		 */
		public void setMucosScore(int pScore) {
			selected = buttons[pScore];
			buttons[pScore].setBackground(colors[pScore]);
			
			Control [] children = this.getChildren ();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				if (selected != child) {
					child.setBackground(colors[4]);
				}
			}
		}
		
		/**
		 * Not implemented. 
		 * 
		 * @param pEvent an event sent by a button.
		 */
		public void mouseUp(MouseEvent pEvent) {}
		
		/**
		 * Not implemented. 
		 * 
		 * @param pEvent an event sent by a button.
		 */
		public void mouseDoubleClick(MouseEvent pEvent) {}
		
		/**
		 * Handles MouseEvents sent by the buttons. 
		 * Implements a radio button behaviour for the buttons.
		 * 
		 * @param pEvent an event sent by a button. 
		 */
		public void mouseDown(MouseEvent pEvent) {
			/* If the standard (left) mouse button was used. */
			if (pEvent.button == 1) {
				if (selected == (Label)pEvent.getSource()) {
					selected.setBackground(colors[4]);
					selected = null;
				} else {
					selected = (Label)pEvent.getSource();
					
					/* 
					 * Sets the background color of all buttons except
					 * the selected to the default color. 
					 */
					Control [] children = this.getChildren ();
					for (int i = 0; i < children.length; i++) {
						Control child = children[i];
						if (selected != child) {
							child.setBackground(colors[4]);
						}
					}
					/* Sets the background color of the selected button to the correct one. */
					selected.setBackground(colors[Integer.parseInt(selected.getText())]);
				}
			}
		}
	}
	
	/**
	 * Represents a section for determining whether an infection is present or not. 
	 */
	private class Infection extends Composite implements SelectionListener {
		private Label presence;
		private Button buttons[];
		
		/**
		 * Constructs a new instance of this class.
		 * 
		 * @param pParent the parent composite. 
		 */
		private Infection(Composite pParent) {
			super(pParent, SWT.NONE);
			this.setLayout(new GridLayout(1, false));
			
			/* Creates a label. */
			presence = new Label(this, SWT.NONE);
			presence.setFont(getStyledFont(pParent, SWT.BOLD));
			presence.setText(INFECTION_PRESENT_TEXT + "No");
			presence.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
						
			/* Creates a group for the check buttons. */
			Group g = new Group(this, SWT.NONE);
			g.setText("Infection types");
			g.setLayout(new GridLayout(1, false));
			
			/* Creates all buttons. */
			buttons = new Button[3];
			buttons[0] = new Button(g, SWT.CHECK);
			buttons[0].setText(INFECTION_LOCAL_STRING);
			buttons[0].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
			buttons[0].addSelectionListener(this);
			buttons[1] = new Button(g, SWT.CHECK);
			buttons[1].setText(INFECTION_NONORAL_STRING);
			buttons[1].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
			buttons[1].addSelectionListener(this);
			buttons[2] = new Button(g, SWT.CHECK);
			buttons[2].setText(INFECTION_SYSTEMIC_STRING);
			buttons[2].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
			buttons[2].addSelectionListener(this);
		}
		
		/**
		 * Handles SelectionEvents sent by a button. 
		 * 
		 * @param pEvent an event sent by a button. 
		 */
		public void widgetSelected(SelectionEvent pEvent) {
			Button source = (Button)pEvent.getSource();
			if (source == buttons[0]) {
				infectionType[INFECTION_TYPE_LOCAL] = source.getSelection();
			} else if (source == buttons[1]) {
				infectionType[INFECTION_TYPE_NON_ORAL] = source.getSelection();
			} else if (source == buttons[2]) {
				infectionType[INFECTION_TYPE_SYSTEMIC] = source.getSelection();
			}
			
			/* 
			 * Sets the presence label according to whether an infection
			 * is present or not. 
			 */
			if(infectionPresent()) {
				presence.setText( INFECTION_PRESENT_TEXT + "Yes");
			} else {
				presence.setText( INFECTION_PRESENT_TEXT + "No");
			}
		}
		
		/**
		 * Sets the correct buttons, according to the values in
		 * infectionType. The method is used when preseting values. 
		 */
		public void setButtons() {
			if (infectionType[INFECTION_TYPE_LOCAL]) {
				buttons[0].setSelection(true);
			}
			if (infectionType[INFECTION_TYPE_NON_ORAL]) {
				buttons[1].setSelection(true);
			}
			if (infectionType[INFECTION_TYPE_SYSTEMIC]) {
				buttons[2].setSelection(true);
			}
			
			if(infectionPresent()) {
				presence.setText( INFECTION_PRESENT_TEXT + "Yes");
			} 
		}
		
		/**
		 * Not implemented. 
		 * 
		 * @param pEvent an event sent by a button.
		 */
		public void widgetDefaultSelected(SelectionEvent pEvent) {}
		
		/**
		 * Checks whether an infection is present. 
		 * 
		 * @return boolean true if an infection is present, false otherwise. 
		 */
		public boolean infectionPresent() {
	          boolean infectionPresent = false;
	          
	          /* Sets infectionPresent to true if any button is checked, and false otherwise. */
	          for (int i = 0; i < infectionType.length; i++) {
	              infectionPresent = (infectionPresent || infectionType[i]);
	          }
	          return infectionPresent;
		}
	}
}