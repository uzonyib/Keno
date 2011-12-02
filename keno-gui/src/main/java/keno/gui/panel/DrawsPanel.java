package keno.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
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
	
	private static final String DATE_FORMAT_KEY = "app.dateformat";	
	private static final String DATE_KEY = "draws.date";
	private static final String COUNT_KEY = "draws.count";
	private static final String COUNT_10_KEY = "draws.count.10";
	private static final String COUNT_100_KEY = "draws.count.100";
	private static final String COUNT_1000_KEY = "draws.count.1000";
	private static final String COUNT_ALL_KEY = "draws.count.all";
	private static final String COUNT_CUSTOM_LOAD_KEY = "draws.count.custom.load";

	private static final String TICKET_SHOW_KEY = "draws.ticket.show";
	private static final String TICKET_HIDE_KEY = "draws.ticket.hide";
	private static final String TICKET_CLEAR_KEY = "draws.ticket.clear";
	private static final String TICKET_FILTER_KEY = "draws.ticket.filter";
	
	private static final String INFO_DRAW_COUNT_KEY = "draws.info.drawcount";
	
	private int drawCount;
	private boolean ticketVisible;
	private String drawCountFormat;
	private LotteryService service;
	
	private MainWindow mainWindow;
	
	private JPanel drawCountSelectionPanel;
	private TicketPanel ticketPanel;
	private Box ticketControlBox;
	private Box controlPanel;
	
	private DrawTableModel drawTableModel;
	private JTable drawTable;	
	private JScrollPane tableScrollPane;	

	private JRadioButton radio10;
	private JRadioButton radio100;
	private JRadioButton radio1000;
	private JRadioButton radioAll;
	private JRadioButton radioCustom;	
	private JSpinner customCountSpinner;
	private JButton customCountButton;
	private JButton ticketVisiblility;
	private JButton clearTicket;
	private JButton filterTicket;
	
	private JLabel drawCountLabel;
	
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
		columnNames[0] = bundle.getString(DATE_KEY);
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
		
		tableScrollPane = new JScrollPane(drawTable);
		
		ticketPanel = new TicketPanel();
		
		clearTicket = new JButton(bundle.getString(TICKET_CLEAR_KEY));
		clearTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ticketPanel.clear();
			}
		});
		
		filterTicket = new JButton(bundle.getString(TICKET_FILTER_KEY));
		filterTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refrashTable(drawCount);
			}
		});
		
		ticketControlBox = Box.createHorizontalBox();
		ticketControlBox.add(filterTicket);
		ticketControlBox.add(clearTicket);

		createDrawCountSelectionPanel(bundle);
		
		drawCountFormat = bundle.getString(INFO_DRAW_COUNT_KEY);
		drawCountLabel = new JLabel(MessageFormat.format(drawCountFormat,
				drawTable.getModel().getRowCount()));
		
		controlPanel = Box.createVerticalBox();
		controlPanel.add(drawCountSelectionPanel);
		
		add(controlPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(drawCountLabel, BorderLayout.SOUTH);
	}
	
	private void createDrawCountSelectionPanel(final ResourceBundle bundle) {
		radio10 = new JRadioButton(
				bundle.getString(COUNT_10_KEY));
		radio10.setSelected(true);
		radio10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(10);
			}
		});
		
		radio100 = new JRadioButton(
				bundle.getString(COUNT_100_KEY));
		radio100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(100);
			}
		});
		
		radio1000 = new JRadioButton(
				bundle.getString(COUNT_1000_KEY));
		radio1000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refrashTable(1000);
			}
		});
		
		radioAll = new JRadioButton(
				bundle.getString(COUNT_ALL_KEY));
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
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
			}
		});
		
		customCountSpinner = new JSpinner(
				new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 100));		
		customCountButton = new JButton(bundle.getString(COUNT_CUSTOM_LOAD_KEY));
		customCountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refrashTable((Integer) customCountSpinner.getValue());
			}
		});
		
		ticketVisiblility = new JButton(bundle.getString(TICKET_SHOW_KEY));
		ticketVisiblility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (ticketVisible) {
					ticketVisiblility.setText(bundle.getString(TICKET_SHOW_KEY));
					controlPanel.remove(ticketPanel);
					controlPanel.remove(ticketControlBox);
					validate();
					repaint();
					ticketVisible = false;
				} else {
					ticketVisiblility.setText(bundle.getString(TICKET_HIDE_KEY));
					controlPanel.removeAll();
					controlPanel.add(drawCountSelectionPanel);
					controlPanel.add(ticketPanel);
					controlPanel.add(ticketControlBox);
					ticketVisible = true;
				}
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
		box.add(new JLabel(bundle.getString(COUNT_KEY)));
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
		box.add(Box.createHorizontalStrut(10));
		box.add(ticketVisiblility);
		box.add(Box.createHorizontalGlue());
		
		drawCountSelectionPanel = new JPanel();
		drawCountSelectionPanel.add(box);
	}
	
	private void refrashTable(final int count) {
		setPanelEnabled(false);
		drawCount = count;
		
		new SwingWorker<Void, Void>() {
			List<Draw> draws;
			@Override
			protected Void doInBackground() throws Exception {
				if (count <= 0) {
					draws = service.listDraws(ticketPanel.getNumberStates());
				} else {
					draws = service.listMostRecentDraws(count, ticketPanel.getNumberStates());
				}
				return null;
			}
			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						drawTableModel.setDraws(draws);
						drawCountLabel.setText(MessageFormat.format(
								drawCountFormat, drawTable.getModel().getRowCount()));
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
		ticketVisiblility.setEnabled(enabled);
		ticketPanel.setEnabled(enabled);
		clearTicket.setEnabled(enabled);
		filterTicket.setEnabled(enabled);
		drawTable.setEnabled(enabled);
		mainWindow.setEnabled(enabled);
	}
	
	private void refreshCustomCountEnabled() {
		boolean enabled = radioCustom.isSelected();
		customCountSpinner.setEnabled(enabled);
		customCountButton.setEnabled(enabled);
	}

}
