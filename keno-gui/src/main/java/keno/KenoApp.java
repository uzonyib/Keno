package keno;

import javax.swing.SwingUtilities;

import keno.gui.MainWindow;

import org.apache.log4j.Logger;

public class KenoApp {
	
	private static final Logger LOGGER = Logger.getLogger(KenoApp.class);
	
	public static void main(String[] args) {
		LOGGER.info("Application started.");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow("/keno.properties");
			}
		});
	}

}
