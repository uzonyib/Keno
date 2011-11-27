package keno.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import keno.KenoApp;
import keno.gui.panel.DrawsPanel;
import keno.gui.panel.InformationPanel;

public class MainWindow {
	
	private static final String APP_TITLE_KEY = "app.title";
	
	private static final String MENU_FILE_KEY = "menu.file";
	private static final String MENU_FILE_EXIT_KEY = "menu.file.exit";
	
	private static final String MENU_VIEW_KEY = "menu.view";
	private static final String MENU_VIEW_INFO_KEY = "menu.view.info";
	private static final String MENU_VIEW_DRAWS_KEY = "menu.view.draws";

	private JFrame mainFrame;
	private JMenuBar menuBar;
	
	private InformationPanel informationPanel;
	private DrawsPanel drawsPanel;
	
	public MainWindow() {
		init();
	}

	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		
		createMenu(bundle);
		
		mainFrame = new JFrame(bundle.getString(APP_TITLE_KEY));
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(100, 100);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setJMenuBar(menuBar);
		
		informationPanel = new InformationPanel(this);
		drawsPanel = new DrawsPanel(this);
		
		mainFrame.add(informationPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);
	}
	
	private void createMenu(ResourceBundle bundle) {
		JMenuItem exitMenuItem = new JMenuItem(bundle.getString(MENU_FILE_EXIT_KEY));
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		JMenu fileMenu = new JMenu(bundle.getString(MENU_FILE_KEY));
		fileMenu.add(exitMenuItem);
		
		final JRadioButtonMenuItem informationMenuItem = new JRadioButtonMenuItem(bundle.getString(MENU_VIEW_INFO_KEY));
		informationMenuItem.setSelected(true);
		informationMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				switchToPanel(informationPanel);
			}
		});
		
		JMenuItem drawsMenuItem = new JRadioButtonMenuItem(bundle.getString(MENU_VIEW_DRAWS_KEY));
		drawsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				switchToPanel(drawsPanel);
			}
		});
		
		ButtonGroup viewGroup = new ButtonGroup();
		viewGroup.add(informationMenuItem);
		viewGroup.add(drawsMenuItem);
		
		JMenu viewMenu = new JMenu(bundle.getString(MENU_VIEW_KEY));
		viewMenu.add(informationMenuItem);
		viewMenu.add(drawsMenuItem);
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
	}
	
	public void setEnabled(final boolean enabled) {
		if (SwingUtilities.isEventDispatchThread()) {
			setWindowEnabled(enabled);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setWindowEnabled(enabled);
				}
			});
		}
	}

	private void setWindowEnabled(boolean enabled) {
		mainFrame.setDefaultCloseOperation(enabled ? JFrame.EXIT_ON_CLOSE
				: JFrame.DO_NOTHING_ON_CLOSE);
		menuBar.setEnabled(enabled);
		for (int i = 0; i < menuBar.getMenuCount(); ++i) {
			menuBar.getMenu(i).setEnabled(enabled);
		}
	}
	
	private void switchToPanel(JPanel panel) {
		mainFrame.remove(informationPanel);
		mainFrame.remove(drawsPanel);
		mainFrame.add(panel, BorderLayout.CENTER);
		mainFrame.validate();
		mainFrame.repaint();
	}

}
