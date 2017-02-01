//
//  ArchetypeOntologyTableModel.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-22.
//
//  $Id: ArchetypeOntologyTableModel.java,v 1.4 2008/12/28 19:18:33 oloft Exp $
//

package medview.openehr.workbench.model;

import java.util.*;

import javax.swing.table.*;
import javax.swing.event.*;

import br.com.zilics.archetypes.models.am.archetype.*;

public class ArchetypeOntologyTableModel extends AbstractTableModel {
	
	private int rowCount;
	private Archetype archetype;
	private String lang;
	
	private String[] columnNames = {
		ArchetypeUtilities.CODE_KEY,
		ArchetypeUtilities.TEXT_KEY, 
	ArchetypeUtilities.DESCRIPTION_KEY};
	
	private List<String> codeList;
	
	public ArchetypeOntologyTableModel() {
		super();
		rowCount = 0;
		archetype = null;
		lang = ArchetypeUtilities.DEFAULT_LANGUAGE;
		codeList = new ArrayList();
	}
	
	public void setArchetype(Archetype a) {
		archetype = a;
		
		//System.out.println(ArchetypeUtilities.getAvailableLanguages(a));
		
		reloadData();
	}
	
	public void setLanguage(String l) {
		lang = l;
		
		reloadData();
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	/*
	 public void setValueAt(Object value, int row, int col) {
	 System.out.println("setValueAt: " + value);
	 if (document.isTermSelected()) {
	 String trm = document.getSelectedTerm();
	 String newVal = value.toString();
	 try {
	 String oldVal = loadedValues[row];
	 document.renameValue(trm, oldVal, newVal);
	 //fireTableCellUpdated(row, col);
	 reloadData();
	 }
	 catch (Exception e) {
	 e.printStackTrace();
	 }
	 }
	 
	 }
	 
	 // Returns index of val or -1 if not found
	 public int indexOf(String val) {
	 int valIndex = -1;
	 
	 for (int i=0; i<rowCount; i++) {
	 if (loadedValues[i].equals(val)) {
	 valIndex = i;
	 break;
	 }
	 }
	 return valIndex;
	 }
	 */
	public Object getValueAt(int row, int col) {
		
		String atCode = codeList.get(row);
		
		switch (col) {
			case 0:
				return atCode;
			case 1:
				return ArchetypeUtilities.getTextValue(archetype,  atCode, lang);
			case 2:
				return ArchetypeUtilities.getDescriptionValue(archetype,  atCode, lang);
				
			default:
				return "Internal error trying to get value";
		}
	}	
	/* if (document.isTermSelected()) {
	 try {
	 return loadedValues[row];
	 }
	 catch (Exception e) {
	 return "Internal error trying to get value";
	 }
	 }*/
	
	public void reloadData() {
			
		try {
			codeList = ArchetypeUtilities.getAtCodesList(archetype);
			rowCount = codeList.size();
		}
		catch (Exception e) {
			codeList = new ArrayList();
			rowCount = 0;
		}
		
		fireTableChanged(new TableModelEvent(this));
	}
}
