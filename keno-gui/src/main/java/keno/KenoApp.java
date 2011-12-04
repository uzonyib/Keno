package keno;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import keno.gui.MainWindow;
import keno.service.LotteryService;
import keno.service.file.FileLotteryService;

import org.apache.log4j.Logger;

public final class KenoApp {
	
	private Properties properties;
	private ResourceBundle resourceBundle;
	private LotteryService lotteryService;
	
	private KenoApp(Properties properties, ResourceBundle resourceBundle) {
		this.properties = properties;
		this.resourceBundle = resourceBundle;
		this.lotteryService = new FileLotteryService(getLotteryFile());
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
	
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public LotteryService getLotteryService() {
		return lotteryService;
	}
	
	private static KenoApp instance;
	
	private static final Logger LOGGER = Logger.getLogger(KenoApp.class);
	
	private static final String KENO_GUI_RB_BASE = "kenogui";
	private static final String PROPERTY_FILE = "/keno.properties";
	
	private static final String SOURCE_URL_PROP = "keno.sourceurl";
	private static final String WORK_DIR_PROP = "keno.workdir";
	private static final String DATA_FILE_PROP = "keno.datafile";
	
	private static final String DEFAULT_SOURCE_URL = "http://www.szerencsejatek.hu/xls/keno.csv";
	private static final String DEFAULT_WORK_DIR = ".";
	private static final String DEFAULT_DATA_FILE = "keno.csv";
	
	public static void main(String[] args) {
		LOGGER.info("Application started.");
		
		Properties properties = initProperties(PROPERTY_FILE);
		ResourceBundle resourceBundle = initResourceBundle(KENO_GUI_RB_BASE);
		if (properties == null || resourceBundle == null) {
			LOGGER.error("Failed to initialize resources.");
			return;
		}
		
		instance = new KenoApp(properties, resourceBundle);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
			}
		});
	}
	
	public static KenoApp getInstance() {
		return instance;
	}

	private static Properties initProperties(String propertyFile) {
		InputStream propInputSream = null;
		try {
			propInputSream = KenoApp.class.getResourceAsStream(propertyFile);
			Properties properties = new Properties();
			properties.load(propInputSream);
			return properties;
		} catch (Exception e) {
			LOGGER.error("Error loading properties.", e);
			return null;
		} finally {
			try {
				if (propInputSream != null) {
					propInputSream.close();
				}
			} catch (IOException e) {
				
			}
		}
	}
	
	private static ResourceBundle initResourceBundle(String propertyFile) {
		try {
			return ResourceBundle.getBundle(propertyFile);
		} catch (Exception e) {
			LOGGER.error("Error loading resource bundle.", e);
			return null;
		}
	}
	
}

