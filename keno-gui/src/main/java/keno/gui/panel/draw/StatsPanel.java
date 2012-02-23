package keno.gui.panel.draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import keno.KenoApp;

public class StatsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String HEADER_COLOR_KEY = "draws.stats.header.color";
	private static final String VALUE_COLOR_KEY = "draws.stats.value.color";
	private static final String SELECTED_COLOR_KEY = "draws.color.selected";
	
	private static class StatsTableRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		
		private Color headerColor;
		private Color valueColor;
		private Color selectedColor;
		
		private List<Byte> selectedNumbers;
		
		public StatsTableRenderer(ResourceBundle bundle) {
			this.headerColor = Color.decode(bundle.getString(HEADER_COLOR_KEY));
			this.valueColor = Color.decode(bundle.getString(VALUE_COLOR_KEY));
			this.selectedColor = Color.decode(bundle.getString(SELECTED_COLOR_KEY));
			this.selectedNumbers = Collections.emptyList();
		}
		
		public void setSelectedNumbers(List<Byte> selectedNumbers) {
			this.selectedNumbers = selectedNumbers;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			if (row % 2 == 0) {
				byte number = (byte) ((row / 2) * 20 + column + 1);
				if (selectedNumbers.contains(number)) {
					renderer.setBackground(selectedColor);
				} else {
					renderer.setBackground(headerColor);
				}
				renderer.setHorizontalAlignment(SwingConstants.CENTER);
			} else {
				renderer.setBackground(valueColor);
			}
			return renderer;
		}
		
	}
	
	private JTable table;
	private StatsTableRenderer tableRenderer;
	private JScrollPane tableScrollPane;
	
	public StatsPanel() {
		init();
	}
	
	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		
		table = new JTable(new DefaultTableModel(8, 20));
		tableRenderer = new StatsTableRenderer(bundle);
		table.setDefaultRenderer(Object.class, tableRenderer);
		table.setTableHeader(null);
		table.setCellSelectionEnabled(false);
		
		for (int i = 0; i < 4; ++i) {
			int rowIndex = 2 * i;
			for (int j = 0; j < 20; ++j) {
				table.setValueAt("" + (i * 20 + j + 1), rowIndex, j);
			}
		}
		
		tableScrollPane = new JScrollPane(table);
		
		setLayout(new BorderLayout());
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	public void refreshTable(int[] counts, List<Byte> selectedNumbers) {
		this.tableRenderer.setSelectedNumbers(selectedNumbers);
		for (int i = 0; i < 4; ++i) {
			int rowIndex = 2 * i + 1;
			for (int j = 0; j < 20; ++j) {
				table.setValueAt("" + counts[i * 20 + j], rowIndex, j);
			}
		}
		
		validate();
		repaint();
	}

}
