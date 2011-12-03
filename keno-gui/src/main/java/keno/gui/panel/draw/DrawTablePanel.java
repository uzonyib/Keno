package keno.gui.panel.draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import keno.KenoApp;
import keno.model.Draw;
import keno.model.NumberState;

public class DrawTablePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private static class DrawTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 1L;
		
		private String[] columnNames;
		private List<Draw> draws;
		private List<NumberState> numberStates;
		private DateFormat dateFormat;

		public DrawTableModel(String[] columnNames, List<Draw> draws, DateFormat dateFormat) {
			this.columnNames = columnNames;
			this.draws = draws;
			this.dateFormat = dateFormat;
		}
		
		public void setDraws(List<Draw> draws, List<NumberState> numberStates) {
			this.numberStates = numberStates;
			this.draws = draws;
			fireTableDataChanged();
		}
		
		public NumberState getState(int row, int column) {
			if (column == 0) {
				return null;
			}
			byte number = draws.get(row).getNumbers()[column - 1];
			if (numberStates == null || numberStates.size() < number) {
				return null;
			}
			return numberStates.get(number - 1);
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return draws.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			Draw draw = draws.get(row);
			if (column == 0) {
				if (draw.getDate() != null) {
					return dateFormat.format(draw.getDate());
				} else {
					return draw.getYear() + "/" + draw.getWeek();
				}
			} else {
				return draw.getNumbers()[column - 1];
			}
		}
		
		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}
		
	}
	
	private static class DrawTableRenderer extends DefaultTableCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		private DrawTableModel model;

		public DrawTableRenderer(DrawTableModel model) {
			this.model = model;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
							row, column);
			if (model.getState(row, column) == NumberState.SELECTED) {
				renderer.setBackground(Color.GREEN);				
			} else {
				renderer.setBackground(Color.WHITE);				
			}
			return renderer;
		}
		
	}
	
	private static final String DATE_FORMAT_KEY = "app.dateformat";	
	private static final String DATE_KEY = "draws.date";
	
	private DrawTableModel tableModel;
	private JTable table;
	private JScrollPane tableScrollPane;
	
	public DrawTablePanel() {
		init();
	}

	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		
		String[] columnNames = new String[21];
		columnNames[0] = bundle.getString(DATE_KEY);
		for (int i = 1; i < 21; ++i) {
			columnNames[i] = "" + i;
		}
		tableModel = new DrawTableModel(columnNames,
				Collections.<Draw>emptyList(),
				new SimpleDateFormat(bundle.getString(DATE_FORMAT_KEY)));
		
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new DrawTableRenderer(tableModel));
		table.getColumnModel().getColumn(0).setMinWidth(80);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		tableScrollPane = new JScrollPane(table);
		
		setLayout(new BorderLayout());
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public int getDrawCount() {
		return tableModel.getRowCount();
	}
	
	public void setDraws(List<Draw> draws, List<NumberState> numberStates) {
		tableModel.setDraws(draws, numberStates);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}

}
