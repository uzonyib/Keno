package keno.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import keno.KenoApp;
import keno.gui.MainWindow;
import keno.gui.panel.draw.DrawTablePanel;
import keno.gui.panel.draw.StatsPanel;
import keno.model.Draw;
import keno.service.LotteryService;
import keno.util.stats.StatsUtil;

public class DrawsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String COUNT_KEY = "draws.count";
	private static final String COUNT_10_KEY = "draws.count.10";
	private static final String COUNT_100_KEY = "draws.count.100";
	private static final String COUNT_1000_KEY = "draws.count.1000";
	private static final String COUNT_ALL_KEY = "draws.count.all";
	private static final String COUNT_CUSTOM_LOAD_KEY = "draws.count.custom.load";

	private static final String TICKET_SHOW_KEY = "draws.ticket.show";
	private static final String TICKET_HIDE_KEY = "draws.ticket.hide";
	
	private static final String INFO_DRAW_COUNT_KEY = "draws.info.drawcount";
	
	private static final String NUMBERS_TAB_TITLE_KEY = "draws.numbers.tab.title";
	private static final String STATS_TAB_TITLE_KEY = "draws.stats.tab.title";
	
	private int drawCount;
	private boolean ticketVisible;
	private String drawCountFormat;
	private LotteryService service;
	
	private MainWindow mainWindow;
	
	private JTabbedPane tabPanel;
	private JPanel drawCountSelectionPanel;
	private TicketPanel ticketPanel;
	private Box controlPanel;
	private DrawTablePanel drawTablePanel;
	private StatsPanel statsPanel;

	private JRadioButton radio10;
	private JRadioButton radio100;
	private JRadioButton radio1000;
	private JRadioButton radioAll;
	private JRadioButton radioCustom;	
	private JSpinner customCountSpinner;
	private JButton customCountButton;
	private JButton ticketVisiblility;
	
	private JLabel drawCountLabel;
	
	public DrawsPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		init();
		refreshTable(10);
	}

	private void init() {
		setLayout(new BorderLayout());
		
		KenoApp app = KenoApp.getInstance();
		service = app.getLotteryService();
		ResourceBundle bundle = app.getResourceBundle();
		
		drawTablePanel = new DrawTablePanel();
		statsPanel = new StatsPanel();
		
		ticketPanel = new TicketPanel(this);
		
		createDrawCountSelectionPanel(bundle);
		
		drawCountFormat = bundle.getString(INFO_DRAW_COUNT_KEY);
		drawCountLabel = new JLabel(MessageFormat.format(drawCountFormat,
				drawTablePanel.getDrawCount()));
		
		controlPanel = Box.createVerticalBox();
		controlPanel.add(drawCountSelectionPanel);
		
		tabPanel = new JTabbedPane();
		tabPanel.add(bundle.getString(NUMBERS_TAB_TITLE_KEY), drawTablePanel);
		tabPanel.add(bundle.getString(STATS_TAB_TITLE_KEY), statsPanel);
		
		add(controlPanel, BorderLayout.NORTH);
		add(tabPanel, BorderLayout.CENTER);
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
				refreshTable(10);
			}
		});
		
		radio100 = new JRadioButton(
				bundle.getString(COUNT_100_KEY));
		radio100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refreshTable(100);
			}
		});
		
		radio1000 = new JRadioButton(
				bundle.getString(COUNT_1000_KEY));
		radio1000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refreshTable(1000);
			}
		});
		
		radioAll = new JRadioButton(
				bundle.getString(COUNT_ALL_KEY));
		radioAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				refreshCustomCountEnabled();
				refreshTable(-1);
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
				refreshTable((Integer) customCountSpinner.getValue());
			}
		});
		
		ticketVisiblility = new JButton(bundle.getString(TICKET_SHOW_KEY));
		ticketVisiblility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (ticketVisible) {
					ticketVisiblility.setText(bundle.getString(TICKET_SHOW_KEY));
					controlPanel.remove(ticketPanel);
					validate();
					repaint();
					ticketVisible = false;
				} else {
					ticketVisiblility.setText(bundle.getString(TICKET_HIDE_KEY));
					controlPanel.add(ticketPanel);
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
	
	public void refreshTable() {
		refreshTable(drawCount);
	}
	
	private void refreshTable(final int count) {
		setPanelEnabled(false);
		drawCount = count;
		final List<Byte> selectedNumbers = ticketPanel.getSelectedNumbers();
		
		new SwingWorker<Void, Void>() {
			List<Draw> draws;
			@Override
			protected Void doInBackground() throws Exception {
				List<Byte> selectedHitCounts = ticketPanel.getSelectedHitCounts();
				if (selectedHitCounts.isEmpty()) {
					selectedHitCounts = null;
				}
				if (count <= 0) {
					draws = service.listMostRecentDraws(selectedNumbers,
							selectedHitCounts);
				} else {
					draws = service.listMostRecentDraws(count, selectedNumbers,
							selectedHitCounts);
				}
				return null;
			}
			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						drawTablePanel.setDraws(draws, selectedNumbers);
						drawCountLabel.setText(MessageFormat.format(
								drawCountFormat, drawTablePanel.getDrawCount()));
						statsPanel.refreshTable(StatsUtil.getCounts(draws), selectedNumbers);
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
		drawTablePanel.setEnabled(enabled);
		mainWindow.setEnabled(enabled);
	}
	
	private void refreshCustomCountEnabled() {
		boolean enabled = radioCustom.isSelected();
		customCountSpinner.setEnabled(enabled);
		customCountButton.setEnabled(enabled);
	}

}
