/*
 * @(#)AbstractTableTermView.java
 *
 * $Id: AbstractTableTermView.java,v 1.11 2006/04/24 14:16:43 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import medview.common.dialogs.*;

import medview.datahandling.*;

import se.chalmers.cs.medview.docgen.parsetree.*;
import se.chalmers.cs.medview.docgen.translator.*;

public abstract class AbstractTableTermView extends AbstractModelTermView implements
	MedViewLanguageConstants
{

	/**
	 * Requested method for selecting and editing
	 * the specified value. Will cause the row
	 * for the value to be scrolled into view,
	 * followed by the activation and cursor
	 * placement in the edit field of that value.
	 * Nothing will happen if the specified value
	 * does not exist in the kept translation model.
	 */
	public void selectAndEdit(Object value)
	{
		int rowIndex = getRowIndex(value);

		if (rowIndex != -1)
		{
			ListSelectionModel m = table.getSelectionModel();

			m.setSelectionInterval(rowIndex, rowIndex);

			Rectangle rect = table.getCellRect(rowIndex, 0, true);

			table.scrollRectToVisible(rect);

			//int tI = getTranslationModelColumnIndex();

			//table.editCellAt(rowIndex, tI);

			//SwingUtilities.invokeLater(new Runnable()
			//{
			//	public void run()
			//	{
			//		table.getEditorComponent().requestFocus();
			//	}
			//});
		}
	}





	public void previewChanged(TranslationModelEvent e)
	{
		int rowIndex = getRowIndex(e.getTranslation().getValue());

		int pColIndex = getPreviewModelColumnIndex();

		if (rowIndex != -1)
		{
			boolean transValue = e.getTranslation().isPreview();

			boolean tableValue = ((Boolean)tableModel.getValueAt(rowIndex, pColIndex)).booleanValue();

			if (tableValue != transValue) { tableModel.setValueAt(new Boolean(transValue), rowIndex, pColIndex); }
		}
	}

	public void translationAdded(TranslationModelEvent e) { updateTopPanel(); }

	public void translationChanged(TranslationModelEvent e) { updateTopPanel(); }

	public void translationRemoved(TranslationModelEvent e) { updateTopPanel(); }





	protected void updateTopPanel()
	{
		clearTable();

		Translation[] trans = currentModel.getTranslations();

		Arrays.sort(trans);

		Object[] rowData = new Object[3];

		for (int ctr=0; ctr<trans.length; ctr++)
		{
			rowData[0] = new Boolean(trans[ctr].isPreview());

			rowData[1] = trans[ctr].getValue();

			rowData[2] = trans[ctr].getTranslation();

			tableModel.addRow(rowData);
		}
	}

	protected void clearTable()
	{
		int rC = tableModel.getRowCount();

		for (int ctr=rC-1; ctr>=0; ctr--)
		{
			tableModel.removeRow(ctr);
		}
	}





	protected Action getAddValueAction()
	{
		if (addValueAction == null) { return new DefaultTableAddAction(); }

		return addValueAction;
	}

	protected Action getRemoveValueAction()
	{
		if (removeValueAction == null) { return new DefaultTableRemoveAction(); }

		return removeValueAction;
	}





// ------------------------------------------------------------------------------------------------
// ************************************************************************************************
// ------------------------------------------------------------------------------------------------

	protected abstract String[] getTableColumnNamesLS();

// ------------------------------------------------------------------------------------------------
// ************************************************************************************************
// ------------------------------------------------------------------------------------------------





	protected JPanel getTopPanel()
	{
		createTable();

		JPanel retPanel = new JPanel(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(table);

		retPanel.add(scrollPane, BorderLayout.CENTER);

		scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		return retPanel;
	}

	private void createTable( )
	{
		String[] cN = getTableColumnNames();

		Object[][] iD = new Object[][]
		{
			{ new Boolean(false), "< value 1 >", "< translation 1 >" },

			{ new Boolean(true), "< value 2 >", "< translation 2 >" },

			{ new Boolean(false), "< value 3 >", "< translation 3 >" }
		};

		tableModel = new DefaultTermTableModel(iD, cN);

		table = new JTable(tableModel);

		table.setRowHeight(getMaxRowHeight());

		setupTableColumnWidthsAndResizability();

		registerTableListeners();

		setupTableEditors();
	}

	private String[] getTableColumnNames()
	{
		String[] lS = getTableColumnNamesLS();

		String[] retArray = new String[lS.length];

		for (int ctr=0; ctr<lS.length; ctr++)
		{
			retArray[ctr] = mVDH.getLanguageString(lS[ctr]);
		}

		return retArray;
	}

	private void registerTableListeners( )
	{

		final ListSelectionModel listModel = table.getSelectionModel();

		listModel.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (!isDerived())
				{
					getRemoveValueAction().setEnabled(!listModel.isSelectionEmpty());
				}
			}
		});

		tableModel.addTableModelListener(new TableModelListener()
		{
			public void tableChanged(TableModelEvent e)
			{
				switch (e.getType())
				{
					case TableModelEvent.UPDATE:
					{
						int col = e.getColumn();

						int row = e.getFirstRow();

						if (col == getPreviewModelColumnIndex())
						{
							Object val = tableModel.getValueAt(row, getValueModelColumnIndex());

							Boolean temp = (Boolean) tableModel.getValueAt(row, getPreviewModelColumnIndex());

							boolean tableFlag = temp.booleanValue();

							boolean modelFlag = currentModel.isPreview(val);

							if (modelFlag != tableFlag) { currentModel.setPreviewStatus(val, tableFlag); }
						}
						else if (col == getTranslationModelColumnIndex())
						{
							Object val = tableModel.getValueAt(row, getValueModelColumnIndex());

							String trans = (String) tableModel.getValueAt(row, getTranslationModelColumnIndex());

							currentModel.setTranslation(val, trans, new Date()); // current date...
						}
					}
				}
			}
		});
	}

	private void setupTableColumnWidthsAndResizability()
	{
		TableColumnModel tCM = table.getColumnModel();

		TableColumn tCP = tCM.getColumn(getPreviewModelColumnIndex());

		TableColumn tCV = tCM.getColumn(getValueModelColumnIndex());

		TableColumn tVT = tCM.getColumn(getTranslationModelColumnIndex());

		tCP.setMinWidth(PREVIEW_COLUMN_WIDTH);

		tCP.setMaxWidth(PREVIEW_COLUMN_WIDTH);

		tCV.setMinWidth(VALUE_COLUMN_MIN_WIDTH);

		tVT.setMinWidth(TRANSLATION_COLUMN_MIN_WIDTH);

		tCV.setPreferredWidth(VALUE_COLUMN_PREF_WIDTH);

		tVT.setPreferredWidth(TRANSLATION_COLUMN_PREF_WIDTH);

		tCP.setResizable(false);
	}

	private void setupTableEditors()
	{
		TableColumnModel tCM = table.getColumnModel();

		TableColumn tC = tCM.getColumn(getTranslationModelColumnIndex());

		tC.setCellEditor(new DefaultComboCellEditor());
	}

	private int getMaxRowHeight()
	{
		int cC = table.getColumnCount();

		int max = -1; int temp = -1;

		for (int ctr=0; ctr<cC; ctr++)
		{
			TableColumn column = table.getColumnModel().getColumn(ctr);

			temp = getMaxRowHeightForColumn(column);

			max = (temp > max) ? temp : max;
		}

		return max;
	}

	private int getMaxRowHeightForColumn(TableColumn column)
	{
		int max = -1;

		int col = column.getModelIndex();

		TableCellRenderer renderer = null;

		for (int row=0; row<table.getRowCount(); row++)
		{
			renderer = table.getCellRenderer(row, col);

			Component rC = renderer.getTableCellRendererComponent(
				table, table.getValueAt(row, col), false, false, row, col);

			int rH = rC.getMaximumSize().height;

			max = (rH > max) ? rH : max;
		}

		return max;
	}





	protected int getRowIndex(Object value)
	{
		if (table == null) { return -1; }

		int valueIndex = getValueModelColumnIndex();

		Object tableValue = null;

		for (int ctr=0; ctr<tableModel.getRowCount(); ctr++)
		{
			tableValue = tableModel.getValueAt(ctr, valueIndex);

			if ((tableValue instanceof String) && (value instanceof String))
			{
				if (((String)tableValue).equalsIgnoreCase((String)value)) { return ctr; }
			}
			else
			{
				if (tableValue.equals(value)) { return ctr; }
			}
		}

		return -1;
	}

	protected int getValueModelColumnIndex() { return 1; }

	protected int getPreviewModelColumnIndex() { return 0; }

	protected int getTranslationModelColumnIndex() { return 2; }





	protected AbstractTableTermView() {}

	protected DefaultTableModel tableModel;

	private static final int PREVIEW_COLUMN_WIDTH = 75;

	private static final int VALUE_COLUMN_MIN_WIDTH = 50;

	private static final int VALUE_COLUMN_PREF_WIDTH = 150;

	private static final int TRANSLATION_COLUMN_MIN_WIDTH = 50;

	private static final int TRANSLATION_COLUMN_PREF_WIDTH = 250;

	protected JTable table;








	public class DefaultTermTableModel extends DefaultTableModel
	{
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			int pI = getPreviewModelColumnIndex();

			int tI = getTranslationModelColumnIndex();

			if (isDerived())
			{
				return (columnIndex == tI);
			}
			else
			{
				return ((columnIndex == pI) || (columnIndex == tI));
			}
		}

		public Class getColumnClass(int columnIndex)
		{
			Vector firstRowVector = (Vector) dataVector.get(0);

			return firstRowVector.elementAt(columnIndex).getClass();
		}

		public DefaultTermTableModel(Object[][] data, Object[] columnNames)
		{
			super(data, columnNames);
		}
	}





	public class DefaultComboCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		public Object getCellEditorValue() { return combo.getEditor().getItem(); }

		public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c)
		{
			combo.getEditor().setItem(v);

			return combo;
		}

		private void initCombo()
		{
			Object[] initValues = new Object[]
			{
				TranslationConstants.TRANSLATION_ITSELF,

				TranslationConstants.TRANSLATION_NOLINE
			};

			combo = new JComboBox(initValues);

			combo.setEditable(true);

			combo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) { stopCellEditing(); }
			});
		}

		public DefaultComboCellEditor() { initCombo(); }

		private JComboBox combo;
	}





	protected class DefaultTableAddAction extends AbstractTermView.DefaultAddValueAction
	{
		public void actionPerformed(ActionEvent e)
		{
			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(AbstractTableTermView.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			MedViewDialog mVD = MedViewDialogs.instance().createAddValueDialog(owner); mVD.show();

			if (!mVD.wasDismissed()) { currentModel.addValue(mVD.getObjectData(), new Date()); }
		}

		/* NOTE: the value is added to the model, which in turn notifies the view that
		 * the term in question has changed, which will lead to a table recalculation and
		 * redisplay with the added term included. The view layer is a 'shell' which listens
		 * to the model layer, and which also tells the model layer that the user wishes to
		 * change something in it. The view should not update itself in direct response to
		 * the user's wish, but rather by the model's response to the users wish, and this is
		 * exactly what is done by only updating the display when the model signals that the
		 * model has changed in some way. */
	}

	protected class DefaultTableRemoveAction extends AbstractTermView.DefaultRemoveValueAction
	{
		public void actionPerformed(ActionEvent e)
		{
			ListSelectionModel listModel = table.getSelectionModel();

			TableModel tableModel = table.getModel();

			if (listModel.isSelectionEmpty()) { return; }

			int valueCol = getValueModelColumnIndex();

			int rows = table.getRowCount();

			Vector removeVector = new Vector();

			for (int ctr=0; ctr<rows; ctr++)
			{
				if (listModel.isSelectedIndex(ctr))
				{
					removeVector.add(tableModel.getValueAt(ctr, valueCol));
				}
			}

			currentModel.removeValues(removeVector);
		}
	}
}
