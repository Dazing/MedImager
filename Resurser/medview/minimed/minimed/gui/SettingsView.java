package minimed.gui;

import minimed.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import java.io.IOException;

/**
 * A view for the settings sceen 
 * 
 * @author Andreas Andersson
 */
public class SettingsView extends MinimedView {
	private GridLayout layout;
	private Menu menu;
	private Button btnTermValues;
	private Button btnTermDefs;
	private Button btnTemplate;
	private Button showMucosYes;
	private Button showMucosNo;
	private Combo language;
	private Label lblTermValues;
	private Label lblTermDefs;
	private Label lblTemplate;
	private Label lblShowMucos;
	private Label lblLanguage;
	private Text termValuesInput;
	private Text termDefsInput;
	private Text templateInput;

	private Shell curShell;
	private MinimedModel curModel;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pShell Shell to use
	 * @param pModel MinimedModel to use
	 * @param pController An instance of AboutController
	 */
	public SettingsView(Shell pShell, MinimedModel pModel, SettingsController pController) {
		super(pShell, pModel, pController);
	
		this.curShell = pShell;
		this.curModel = pModel;
		
		layout = new GridLayout(2, false);
		this.setLayout(layout);

		buildMenu();
		buildDialogs();
		populateDialogs();
	}
	
	/**
	 * Builds the form elements used on the settings screen.
	 */
	private void buildDialogs() {
		
		/* GridData objects */
		GridData termValuesData = new GridData();
		termValuesData.horizontalAlignment = GridData.FILL;
		termValuesData.horizontalSpan = 2;
		termValuesData.grabExcessHorizontalSpace = false;
		
		GridData termDefsData = new GridData();
		termDefsData.horizontalAlignment = GridData.FILL;
		termDefsData.horizontalSpan = 2;
		termDefsData.grabExcessHorizontalSpace = true;
		
		GridData templateData = new GridData();
		templateData.horizontalAlignment = GridData.FILL;
		templateData.horizontalSpan = 2;
		templateData.grabExcessHorizontalSpace = true;
		
		GridData btnSaveData = new GridData();
		btnSaveData.horizontalAlignment = GridData.FILL;
		btnSaveData.horizontalSpan = 2;
		btnSaveData.grabExcessHorizontalSpace = true;
		
		GridData inputData = new GridData();
		inputData.widthHint = 125;
		
		/* Setting for the term values file */
		lblTermValues = new Label(this, SWT.NONE);
		lblTermValues.setText(curModel.i18n("Term Values File") + ":");
		lblTermValues.setFont(getStyledFont(this, SWT.BOLD));
		lblTermValues.setLayoutData(termValuesData);
		termValuesInput = new Text(this, SWT.SINGLE | SWT.BORDER);
		termValuesInput.setLayoutData(inputData);
		btnTermValues = new Button(this, SWT.PUSH);
		btnTermValues.setText(curModel.i18n("Choose"));
		btnTermValues.addSelectionListener(new TermValuesListener());
			
        /* Setting for the term definitions file */
		lblTermDefs = new Label(this, SWT.NONE);
		lblTermDefs.setText(curModel.i18n("Term Definitions File") + ":");
		lblTermDefs.setFont(getStyledFont(this, SWT.BOLD));
		lblTermDefs.setLayoutData(termDefsData);
		termDefsInput = new Text(this, SWT.SINGLE | SWT.BORDER);
		termDefsInput.setLayoutData(inputData);
		btnTermDefs = new Button(this, SWT.PUSH);
		btnTermDefs.setText(curModel.i18n("Choose"));
		btnTermDefs.addSelectionListener(new TermDefsListener());
		
		/* Setting for the template file */
		lblTemplate = new Label(this, SWT.NONE);
		lblTemplate.setText(curModel.i18n("Template File") + ":");
		lblTemplate.setFont(getStyledFont(this, SWT.BOLD));
		lblTemplate.setLayoutData(templateData);
		templateInput = new Text(this, SWT.SINGLE | SWT.BORDER);
		templateInput.setLayoutData(inputData);
		btnTemplate = new Button(this, SWT.PUSH);
		btnTemplate.setText(curModel.i18n("Choose"));
		btnTemplate.addSelectionListener(new TemplateListener());
		new Label(this, SWT.NONE);
		
		/* Setting for Mucos */
		lblShowMucos = new Label(this, SWT.NONE);
		lblShowMucos.setText(curModel.i18n("Show Mucos Form") + ":");
		lblShowMucos.setFont(getStyledFont(this, SWT.BOLD));
		lblShowMucos.setLayoutData(templateData);
		showMucosYes = new Button(this, SWT.RADIO);
		showMucosYes.setText(curModel.i18n("Yes"));
		showMucosNo = new Button(this, SWT.RADIO);
		showMucosNo.setText(curModel.i18n("No"));
		
		/* Setting for language */
		lblLanguage = new Label(this, SWT.NONE);
		lblLanguage.setText(curModel.i18n("Choose Language") + ":");
		lblLanguage.setFont(getStyledFont(this, SWT.BOLD));
		lblLanguage.setLayoutData(templateData);
		language = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		new Label(this, SWT.NONE);
		
	}
	
	/**
	 * Populates the form components used on the settings screen.
	 */
	private void populateDialogs() {
		templateInput.setText(curModel.getFormTemplatePath());
		termDefsInput.setText(curModel.getTermDefinitionsPath());
		termValuesInput.setText(curModel.getTermValuesPath());
		
		if (curModel.getShowMucos().equals("yes")) {		
			showMucosYes.setSelection(true);
		} else {
			showMucosNo.setSelection(true);
		}
		
		String[] languages = curModel.getAllLanguages();
		for (int i = 0; i < languages.length; i++) {
			language.add(languages[i]);
		}
		language.setText(curModel.getLanguage());
	}
	
	/**
	 * Retrieves the values from the form components and 
	 * saves them to the settings.conf file, through the 
	 * PropertiesHandler.
	 */
	protected void save(){

		curModel.setFormTemplatePath(templateInput.getText());
		curModel.setTermDefinitionsPath(termDefsInput.getText());
		curModel.setTermValuesPath(termValuesInput.getText());
		
		if (showMucosYes.getSelection()) {
			curModel.setShowMucos("yes");
		} else {
			curModel.setShowMucos("no");
		}
		
		curModel.setLanguage(language.getText());
						
		try {
			curModel.saveProperties();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	/**
	 * Class for handling SelectionListener events for the Term Values
	 * form component.
	 */
	private class TermValuesListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent pEvent){
			String oldShellText = curShell.getText();
			curShell.setText("TermValues");
			
		    FileDialog fd = new FileDialog(curShell);
		    String filePath = fd.open();
		    
		    if (filePath != null) {
		    	termValuesInput.setText(filePath);
		    }

			curShell.setText(oldShellText);
		}
	}
	
	/**
	 * Class for handling SelectionListener events for the Term Definitions
	 * form component.
	 */
	private class TermDefsListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent pEvent){
			String oldShellText = curShell.getText();
			curShell.setText("TermDefinitions");
			
		    FileDialog fd = new FileDialog(curShell);
			String filePath = fd.open();

			if (filePath != null) {
				termDefsInput.setText(filePath);
			}
			
			curShell.setText(oldShellText);
		}
	}
	
	/**
	 * Class for handling SelectionListener events for the Template File
	 * form component.
	 */
	private class TemplateListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent pEvent){
			String oldShellText = curShell.getText();
			curShell.setText("Template");
			
			String[] filterExts = new String[1];
			filterExts[0] = "*.xml";
		    FileDialog fd = new FileDialog(curShell);
		    fd.setFilterExtensions(filterExts);
		    String filePath = fd.open();

			if (filePath != null) {
				templateInput.setText(filePath);
			}
			
			curShell.setText(oldShellText);
		}
	}
	
	/**
	 * Disposes the view.
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		super.dispose();
		menu.dispose();
	}
	
	/**
	 * Build the program menu.
	 */
	private void buildMenu(){
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText(curModel.i18n("Menu"));
		file.setMenu(fileMenu);

		MenuItem close = new MenuItem(fileMenu, SWT.PUSH);
		close.setText(curModel.i18n("Close Without Saving"));
		close.addSelectionListener((SettingsController)controller);

		MenuItem closeAndSave = new MenuItem(fileMenu, SWT.PUSH);
		closeAndSave.setText(curModel.i18n("Save And Close"));
		closeAndSave.addSelectionListener((SettingsController)controller);
	}
	
	/**
	 * Not implemented. 
	 * 
	 * @param pEvent an event sent by the view.
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent pEvent) {}

    /**
     * Creates a font that fits the current OS, and has a certain style.
     * 
     * @param pControl a control to get the font data from
     * @param pStyle the SWT font style to be used, i.e. <code>SWT.BOLD</code>
     * @return the font created with the given data
     */
	private Font getStyledFont(Control pControl, int pStyle) {
		FontData[] fd = pControl.getFont().getFontData();
		for(int i = 0; i < fd.length; i++) {
			fd[i].setStyle(pStyle);
		}
		Font result = new Font(pControl.getDisplay(), fd);
		return result;
	}
	
}