package minimed.gui;


import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import minimed.MinimedModel;
import minimed.help.Category;
import minimed.help.Help;
import minimed.help.Question;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A view for the help sceen. 
 * 
 * @author Jens Hultberg
 */
public class HelpView extends MinimedView {
	private MinimedModel curModel;
	private TabFolder tabfolder;
	private Contents contents;
	private Index index;
	private Article article;
	private Help h;
	private Menu menu;
	
	/**
	 * Creates a HelpView object.
	 *
	 * @param pShell the shell used by the application.
	 * @param pModel the model used.
	 * @param pController the controller to be associated with the view.
	 */
	public HelpView(Shell pShell, MinimedModel pModel, HelpController pController) {
		super(pShell, pModel, pController);
		curModel = pModel;
		this.setLayout(new GridLayout(1, false));
		
		buildMenu();
		
		try {
			h = new Help(pModel);
			buildTabFolder();
		} catch(Exception e) {
			MessageBox error = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
			error.setMessage(pModel.i18n("The path to help.xml is invalid!"));
			error.open();
			((HelpController)controller).returnToStartView();
		}
	}
	
	/**
	 * Disposes the view. 
	 */
	public void dispose(){
		super.dispose();
		if(menu != null)
			menu.dispose();
	}
	
	/**
	 * Builds the menu.
	 */
	private void buildMenu(){
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText(curModel.i18n("Menu"));
		file.setMenu(fileMenu);
		
		MenuItem close = new MenuItem(fileMenu, SWT.PUSH);
		close.setText(curModel.i18n("Close"));
		close.addSelectionListener((HelpController)controller);
	}
	
	/**
	 * Builds the tab folder with "contents", "index" and "article"
	 */
	private void buildTabFolder() {
		tabfolder = new TabFolder(this, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tabfolder.setLayoutData(gd);
		
		/* Builds the tabs in the tabfolder. */
		contents = new Contents();
		index = new Index();
		article = new Article();
		
		tabfolder.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_BOTH);
		contents.setLayoutData(gd);
		gd = new GridData(GridData.FILL_BOTH);
		index.setLayoutData(gd);
		gd = new GridData(GridData.FILL_BOTH);
		article.setLayoutData(gd);
	}
	
	/**
	 * Sets the article to be displayed. Also selects the article tab. 
	 * 
	 * @param pQuestion the question to construct the article from.
	 */
	protected void setArticle(Question pQuestion) {
		article.setArticle(pQuestion);
		tabfolder.setSelection(2); 
	}
	
	/**
	 * Selects the first item in the index which matches the string s.
	 * 
	 * @param pTitle the string to search for.
	 */
	protected void selectTableItem(String pTitle) {
		index.setSelection(pTitle);
	}

	/**
	 * Represents a contents tab. 
	 */
	private class Contents extends Composite {
		private Tree tree;
			
		/**
		 * Creates a new instance of this class.
		 */
		protected Contents() {
			super(tabfolder, SWT.NONE);
		 	this.setLayout(new FillLayout(SWT.VERTICAL));

		 	/* Creates a Tree and a TreeItem for each category. */
			tree = new Tree(this, SWT.SINGLE);
			Category[] cat = h.getCategories();
			for (int i = 0; i < cat.length; i++) {
				/* Creates a category. */
				TreeItem categoryItem = new TreeItem(tree, SWT.NONE);
				categoryItem.setText(cat[i].getTitle());
				
				/* Builds a tree map with all questions using their titles as keys. */
				TreeMap treeMap = new TreeMap(new IgnoreCaseComparator());
				Question[] questions = cat[i].getQuestions();
				for (int j = 0; j < questions.length; j++) {
					treeMap.put(questions[j].getTitle(), questions[j]);
				}
				
				/* 
				 * Creates a TreeItem for each question with the current 
				 * category as parent.
				 */
				Iterator iterator = treeMap.values().iterator();
				while (iterator.hasNext()) {
					Question question = (Question)(iterator.next());
					TreeItem questionItem = new TreeItem(categoryItem, SWT.NONE);
					questionItem.setText(question.getTitle());
					questionItem.setData(question);
				}
			}
			tree.addSelectionListener((HelpController)controller);
			
			/* Creates a TabItem filled with an instance of this class. */
			TabItem item = new TabItem(tabfolder, SWT.NONE);
			item.setText(curModel.i18n("Contents"));
			item.setControl(this);
		}
	}	
	
	/**
	 * Represents an index tab.
	 */
	private class Index extends Composite {
		private Text input;
		private Table table;
		private TableItem[] tableItems;
		private TreeMap treeMap;
		
		/**
		 * Creates a new instance of this class.
		 */
		protected Index() {
			super(tabfolder, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			this.setLayout(gridLayout);

			/* 
			 * Creates a field where the user may enter a 
			 * string to search for in the index. 
			 */
			input = new Text(this, (SWT.BORDER | SWT.SINGLE));
			input.addModifyListener((HelpController)controller);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			input.setLayoutData(data);
			
			/* Builds a tree map with all questions using their titles as keys. */
			treeMap = new TreeMap(new IgnoreCaseComparator());
			Question[] questions = h.getQuestions();
			for (int i = 0; i < questions.length; i++) {
				treeMap.put(questions[i].getTitle(), questions[i]);
			}
			
			/* Creates a table with all questions, using the tree map. */
			table = new Table(this, SWT.SINGLE);
			tableItems = new TableItem[questions.length];
			Iterator iterator = treeMap.values().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				tableItems[i] = new TableItem(table, SWT.NONE);
				Question question = (Question)(iterator.next());
				tableItems[i].setText(question.getTitle());
				tableItems[i].setData(question);
				i++;
			}
						
			data = new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
			table.setLayoutData(data);
			table.addSelectionListener((HelpController)controller);
			
			/* Creates a TabItem filled with an instance of this class. */
			TabItem item = new TabItem(tabfolder, SWT.NONE);
			item.setText(curModel.i18n("Index"));
			item.setControl(this);
		}
		
		/**
		 * Selects the first item in the index which matches the string pattern.
		 * 
		 * @param pPattern the pattern to search for. 
		 */
		protected void setSelection(String pPattern) {
			if (pPattern != "") {
				Iterator iterator = treeMap.keySet().iterator();
				int selection = 0;
				int bestSoFar = 0;

				/*
				 * All strings are compared with the pattern, and table
				 * item corresponding to the best match is set. 
				 */
				while (iterator.hasNext()) {
					String s = (String)iterator.next();
					/* 
					 * If the string matches the pattern, the number of 
					 * matching characters is calculated and if it exceeds
					 * the best number achieved so far, the corresponding
					 * table item is set. 
					 */
					if (s.toLowerCase().startsWith(pPattern.toLowerCase())) {
						int match = match(pPattern, s);
						if (match > bestSoFar) {
							bestSoFar = match;
							table.setSelection(selection);						
						}
					}
					selection++;
				}
			} else {
				table.setSelection(0);
			}
		}
		
		/**
		 * Compares a pattern with a string and returns the 
		 * number of character in a row, beginning at with the 
		 * first one, that matches.  
		 * 
		 * @param pPattern the pattern to compare with.
		 * @param pString the string to compare.
		 * @return the number of characters that match.
		 */
		private int match(String pPattern, String pString) {
			Collator c = Collator.getInstance(); 
			c.setStrength(Collator.PRIMARY);
			int match = 0;
			
			/*
			 * Compares the pattern and the string, character for character,
			 * returns the number of characters in a row (beginning at the first 
			 * character) that matches. 
			 */
			for (int i = 0; i <= pString.length(); i++) {
				if (i <= pPattern.length()) {
					String sub1 = pPattern.substring(0, i);
					String sub2 = pString.substring(0, i);
					if (c.compare(sub1, sub2) == 0) {
						match = i;
			      	} 
				} 
			}
			return match;  
		}
	}
	
	/**
	 * Represents an article tab.
	 */
	private class Article extends Composite {
		private Text title;
		private Text body;
				
		/**
		 * Creates a new instance of this class.
		 */
		protected Article() {
			super(tabfolder, SWT.NONE);
			GridLayout gridLayout = new GridLayout(1, false);
			this.setLayout(gridLayout);
						
			/*
			 * Both the text for the article and the title is given
			 * in plain text format. The use of HTML formatting is 
			 * unfortunately not possible at the moment due to 
			 * restrictions on the use of some custom widgets on 
			 * a Pocket PC system.
			 * 
			 * The title is given a bold font.
			 */
			title = new Text(this, (SWT.SINGLE | SWT.READ_ONLY));
			title.setText(curModel.i18n("No Article to Display"));
			title.setFont(getStyledFont(this, SWT.BOLD));
			title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			body = new Text(this, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
			body.setLayoutData(new GridData((GridData.FILL_BOTH)));
			
			/* Creates a TabItem filled with an instance of this class. */
			TabItem item = new TabItem(tabfolder, SWT.NONE);
			item.setText(curModel.i18n("Article"));
			item.setControl(this);
		}
		
		/**
		 * Sets a new article.
		 * 
		 * @param pQuestion a question to form the article from. 
		 */
		protected void setArticle(Question pQuestion) {
			/* Sets the title and text of the article. */
			title.setText(pQuestion.getTitle());
			body.setText(pQuestion.getText());
		}
		
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
	
	/**
	 * A comparator for comparing two strings, ignoring their case.
	 * 
	 * @author Joni Paananen
	 */
	private class IgnoreCaseComparator implements Comparator{
		public int compare(Object o1, Object o2){
			return ((String)o1).compareToIgnoreCase((String)o2);
		}
	}
}