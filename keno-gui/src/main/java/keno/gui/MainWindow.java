package keno.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import keno.KenoApp;
import keno.gui.panel.InformationPanel;

public class MainWindow {
	
	private static final String APP_TITLE_KEY = "app.title";
	private static final String MENU_FILE_KEY = "menu.file";
	private static final String MENU_FILE_EXIT_KEY = "menu.file.exit";

	private JFrame mainFrame;
	private JMenuBar menuBar;
	
	private InformationPanel informationPanel;
	
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
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
	}
	
	public void setEnabled(final boolean enabled) {
		if (SwingUtilities.isEventDispatchThread()) {
			doSetEnabled(enabled);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					doSetEnabled(enabled);
				}
			});
		}
	}

	private void doSetEnabled(boolean enabled) {
		mainFrame.setDefaultCloseOperation(enabled ? JFrame.EXIT_ON_CLOSE
				: JFrame.DO_NOTHING_ON_CLOSE);
		menuBar.setEnabled(enabled);
		for (int i = 0; i < menuBar.getMenuCount(); ++i) {
			menuBar.getMenu(i).setEnabled(enabled);
		}
	}

}
