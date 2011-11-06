package keno.main;

import java.io.File;

import javax.swing.SwingUtilities;

import keno.view.MainWindow;

import org.apache.log4j.Logger;

public class KenoApp {
	
	private static final Logger LOGGER = Logger.getLogger(KenoApp.class);
	
	public static final String DB_FILE_URL = "http://www.szerencsejatek.hu/xls/keno.csv";
	public static final String DB_FILE_LOCATION = System.getProperty("user.home")
			+ File.separator + "keno.csv";
	
	public static void main(String[] args) {
		LOGGER.info("Application started.");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
	}

}
