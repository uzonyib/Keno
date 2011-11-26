package keno.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import keno.KenoApp;
import keno.gui.panel.InformationPanel;

public class MainWindow {
	
	private static final String APP_TITLE_KEY = "app.title";
	private static final String MENU_FILE_KEY = "menu.file";
	private static final String MENU_FILE_EXIT_KEY = "menu.file.exit";

	private JFrame mainFrame;
	
	private InformationPanel informationPanel;
	
	public MainWindow() {
		init();
	}

	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		
		mainFrame = new JFrame(bundle.getString(APP_TITLE_KEY));
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(100, 100);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setJMenuBar(createMenu(bundle));
		
		informationPanel = new InformationPanel();
		mainFrame.add(informationPanel);
		
		mainFrame.setVisible(true);
	}
	
	private JMenuBar createMenu(ResourceBundle bundle) {
		JMenuItem exitMenuItem = new JMenuItem(bundle.getString(MENU_FILE_EXIT_KEY));
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		JMenu fileMenu = new JMenu(bundle.getString(MENU_FILE_KEY));
		fileMenu.add(exitMenuItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		return menuBar;
	}

}
