package keno;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.SwingUtilities;

import keno.gui.MainWindow;
import keno.service.LotteryService;
import keno.service.file.FileLotteryService;

import org.apache.log4j.Logger;

public enum KenoApp {
	
	INSTANCE("/keno.properties");
	
	private static final Logger LOGGER = Logger.getLogger(KenoApp.class);
	
	private static final String SOURCE_URL_PROP = "keno.sourceurl";
	private static final String WORK_DIR_PROP = "keno.workdir";
	private static final String DATA_FILE_PROP = "keno.datafile";
	
	private static final String DEFAULT_SOURCE_URL = "http://www.szerencsejatek.hu/xls/keno.csv";
	private static final String DEFAULT_WORK_DIR = ".";
	private static final String DEFAULT_DATA_FILE = "keno.csv";
	
	private Properties properties;
	
	private LotteryService lotteryService;
	
	private KenoApp(String propertyFile) {
		initProperties(propertyFile);
	}

	private void initProperties(String propertyFile) {
		InputStream propInputSream = null;
		try {
			propInputSream = getClass().getResourceAsStream(propertyFile);
			this.properties = new Properties();
			this.properties.load(propInputSream);
		} catch (IOException e) {
			LOGGER.error("Error loading properties.", e);
			throw new IllegalArgumentException(propertyFile, e);
		} finally {
			try {
				if (propInputSream != null) {
					propInputSream.close();
				}
			} catch (IOException e) {
				
			}
		}
	}
	
	public String getSourceUrl() {
		return properties.getProperty(SOURCE_URL_PROP, DEFAULT_SOURCE_URL);
	}
	
	public String getWorkDir() {
		return properties.getProperty(WORK_DIR_PROP, DEFAULT_WORK_DIR);
	}
	
	public String getDataFile() {
		return properties.getProperty(DATA_FILE_PROP, DEFAULT_DATA_FILE);
	}
	
	public String getLotteryFile() {
		return getWorkDir() + File.separator + getDataFile();
	}
	
	public LotteryService getLotteryService() {
		if (lotteryService == null) {
			lotteryService = new FileLotteryService(getLotteryFile());
		}
		
		return lotteryService;
	}
	
	public static void main(String[] args) {
		LOGGER.info("Application started.");
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}

}

