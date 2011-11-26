package keno.gui;

import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import keno.KenoApp;
import keno.gui.panel.InformationPanel;

public class MainWindow {
	
	public MainWindow() {
		init();
	}

	private void init() {
		final KenoApp app = KenoApp.getInstance();
		final ResourceBundle bundle = app.getResourceBundle();
		
		final JFrame mainFrame = new JFrame(bundle.getString("app.title"));
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(100, 100);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu(bundle.getString("menu.file"));
		menubar.add(fileMenu);
		
		mainFrame.add(new InformationPanel());
		
		mainFrame.setJMenuBar(menubar);
		mainFrame.setVisible(true);
	}

}
