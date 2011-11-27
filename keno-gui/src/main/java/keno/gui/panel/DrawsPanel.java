package keno.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import keno.KenoApp;
import keno.gui.MainWindow;
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
		
		public void setDraws(List<Draw> draws) {
			this.draws = draws;
			fireTableDataChanged();
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

	private static final long serialVersionUID = 1L;
	
	private static final String DRAWS_DATE_KEY = "draws.date";
	private static final String DRAWS_COUNT_KEY = "draws.count";
	private static final String DRAWS_COUNT_10_KEY = "draws.count.10";
	private static final String DRAWS_COUNT_100_KEY = "draws.count.100";
	private static final String DRAWS_COUNT_1000_KEY = "draws.count.1000";
	private static final String DRAWS_COUNT_ALL_KEY = "draws.count.all";
	private static final String DRAWS_COUNT_CUSTOM_LOAD_KEY = "draws.count.custom.load";
	private static final String DATE_FORMAT_KEY = "app.dateformat";	
	
	private MainWindow mainWindow;
	
	private LotteryService service;
	private DrawTableModel drawTableModel;
	private JTable drawTable;

	private JRadioButton radio10;
	private JRadioButton radio100;
	private JRadioButton radio1000;
	private JRadioButton radioAll;
	private JRadioButton radioCustom;	
	private JSpinner customCountSpinner;
	private JButton customCountButton;
	
	private int drawCount;
	
	public DrawsPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		init();
		refrashTable(10);
	}

	private void init() {
		setLayout(new BorderLayout());
		
		KenoApp app = KenoApp.getInstance();
		service = app.getLotteryService();
		ResourceBundle bundle = app.getResourceBundle();
		
		String[] columnNames = new String[21];
		columnNames[0] = bundle.getString(DRAWS_DATE_KEY);
		for (int i = 1; i < 21; ++i) {
			columnNames[i] = "" + i;
		}
		drawTableModel = new DrawTableModel(columnNames,
				Collections.<Draw>emptyList(),
				new SimpleDateFormat(bundle.getString(DATE_FORMAT_KEY)));
		
		drawTable = new JTable(drawTableModel);
		drawTable.getColumnModel().getColumn(0).setMinWidth(80);
		drawTable.getTableHeader().setReorderingAllowed(false);
		drawTable.getTableHeader().setResizingAllowed(false);
		
		Box box = Box.createVerticalBox();
		box.add(createDrawCountSelectionPanel(bundle));
		box.add(new JScrollPane(drawTable));
		
		add(box, BorderLayout.CENTER);
	}
	
	private Box createDrawCountSelectionPanel(ResourceBundle bundle) {
		radio10 = new JRadioButton(
				bundle.getString(DRAWS_COUNT_10_KEY));
		radio10.setSelected(true);
		radio10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(10);
			}
		});
		
		radio100 = new JRadioButton(
				bundle.getString(DRAWS_COUNT_100_KEY));
		radio100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(100);
			}
		});
		
		radio1000 = new JRadioButton(
				bundle.getString(DRAWS_COUNT_1000_KEY));
		radio1000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(1000);
			}
		});
		
		radioAll = new JRadioButton(
				bundle.getString(DRAWS_COUNT_ALL_KEY));
		radioAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(-1);
			}
		});
		
		radioCustom = new JRadioButton();
		radioCustom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshCustomCountEnabled();
			}
		});
		
		customCountSpinner = new JSpinner(
				new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 100));		
		customCountButton = new JButton(bundle.getString(DRAWS_COUNT_CUSTOM_LOAD_KEY));
		customCountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refrashTable((Integer) customCountSpinner.getValue());
			}
		});
		
		refreshCustomCountEnabled();
		
		ButtonGroup group = new ButtonGroup();
		group.add(radio10);
		group.add(radio100);
		group.add(radio1000);
		group.add(radioAll);
		group.add(radioCustom);
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(new JLabel(bundle.getString(DRAWS_COUNT_KEY)));
		box.add(Box.createHorizontalStrut(10));
		box.add(radio10);
		box.add(Box.createHorizontalStrut(10));
		box.add(radio100);
		box.add(Box.createHorizontalStrut(10));
		box.add(radio1000);
		box.add(Box.createHorizontalStrut(10));
		box.add(radioAll);
		box.add(Box.createHorizontalStrut(10));
		box.add(radioCustom);
		box.add(customCountSpinner);
		box.add(customCountButton);
		box.add(Box.createHorizontalGlue());
		
		return box;
	}
	
	private void refrashTable(final int count) {
		if (count == drawCount) {
			return;
		}
		
		setPanelEnabled(false);
		drawCount = count;
		
		new SwingWorker<Void, Void>() {
			List<Draw> draws;
			@Override
			protected Void doInBackground() throws Exception {
				if (count <= 0) {
					draws = service.listDraws();
				} else {
					draws = service.listMostRecentDraws(count);
				}
				return null;
			}
			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						drawTableModel.setDraws(draws);
						setPanelEnabled(true);
					}
				});
			}
		}.execute();
	}
	
	private void setPanelEnabled(boolean enabled) {
		radio10.setEnabled(enabled);
		radio100.setEnabled(enabled);
		radio1000.setEnabled(enabled);
		radioAll.setEnabled(enabled);
		radioCustom.setEnabled(enabled);
		refreshCustomCountEnabled();
		drawTable.setEnabled(enabled);
		mainWindow.setEnabled(enabled);
	}
	
	private void refreshCustomCountEnabled() {
		boolean enabled = radioCustom.isSelected();
		customCountSpinner.setEnabled(enabled);
		customCountButton.setEnabled(enabled);
	}

}
