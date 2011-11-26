package keno.gui.panel;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import keno.KenoApp;
import keno.model.Draw;
import keno.service.LotteryService;

public class DrawsPanel extends JPanel {

	private static class DrawTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 1L;
		
		private String[] columnNames;
		private List<Draw> draws;
		private DateFormat dateFormat;

		public DrawTableModel(String[] columnNames, List<Draw> draws, DateFormat dateFormat) {
			this.columnNames = columnNames;
			this.draws = draws;
			this.dateFormat = dateFormat;
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
			return column == 0 ? dateFormat.format(draws.get(row).getDate())
					: draws.get(row).getNumbers()[column - 1];
		}
		
		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}
		
	}

	private static final long serialVersionUID = 1L;
	
	private static final String DRAWS_DATE_KEY = "draws.date";
	private static final String DATE_FORMAT_KEY = "app.dateformat";
	
	public DrawsPanel() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		
		KenoApp app = KenoApp.getInstance();
		LotteryService service = app.getLotteryService();
		ResourceBundle bundle = app.getResourceBundle();
		
		String[] columnNames = new String[21];
		columnNames[0] = bundle.getString(DRAWS_DATE_KEY);
		for (int i = 1; i < 21; ++i) {
			columnNames[i] = "" + i;
		}
		DrawTableModel tableModel = new DrawTableModel(columnNames,
				service.listMostRecentDraws(10),
				new SimpleDateFormat(bundle.getString(DATE_FORMAT_KEY)));
		
		final JTable table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setMinWidth(80);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

}
