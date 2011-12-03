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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import keno.KenoApp;
import keno.gui.MainWindow;
import keno.gui.panel.draw.DrawTablePanel;
import keno.model.Draw;
import keno.model.NumberState;
import keno.service.LotteryService;

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
	
	private DrawTablePanel drawTablePanel;

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
		refreshTable(10);
	}

	private void init() {
		setLayout(new BorderLayout());
		
		KenoApp app = KenoApp.getInstance();
		service = app.getLotteryService();
		ResourceBundle bundle = app.getResourceBundle();
		
		drawTablePanel = new DrawTablePanel();
		
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
				refreshTable(drawCount);
			}
		});
		
		ticketControlBox = Box.createHorizontalBox();
		ticketControlBox.add(filterTicket);
		ticketControlBox.add(clearTicket);

		createDrawCountSelectionPanel(bundle);
		
		drawCountFormat = bundle.getString(INFO_DRAW_COUNT_KEY);
		drawCountLabel = new JLabel(MessageFormat.format(drawCountFormat,
				drawTablePanel.getDrawCount()));
		
		controlPanel = Box.createVerticalBox();
		controlPanel.add(drawCountSelectionPanel);
		
		add(controlPanel, BorderLayout.NORTH);
		add(drawTablePanel, BorderLayout.CENTER);
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
	
	private void refreshTable(final int count) {
		setPanelEnabled(false);
		drawCount = count;
		final List<NumberState> numberStates = ticketPanel.getNumberStates();
		
		new SwingWorker<Void, Void>() {
			List<Draw> draws;
			@Override
			protected Void doInBackground() throws Exception {
				if (count <= 0) {
					draws = service.listDraws(numberStates);
				} else {
					draws = service.listMostRecentDraws(count, numberStates);
				}
				return null;
			}
			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						drawTablePanel.setDraws(draws, numberStates);
						drawCountLabel.setText(MessageFormat.format(
								drawCountFormat, drawTablePanel.getDrawCount()));
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
		drawTablePanel.setEnabled(enabled);
		mainWindow.setEnabled(enabled);
	}
	
	private void refreshCustomCountEnabled() {
		boolean enabled = radioCustom.isSelected();
		customCountSpinner.setEnabled(enabled);
		customCountButton.setEnabled(enabled);
	}

}
