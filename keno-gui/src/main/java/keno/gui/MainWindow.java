package keno.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import keno.util.download.FileDownloader;

import org.apache.log4j.Logger;

public class MainWindow {
	
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);
	
	private Properties properties;
	
	private static final String CSV_URL_PROP = "keno.csv";
	private static final String WORK_DIR_PROP = "keno.workdir";
	private static final String LOCAL_FILE_PROP = "keno.localfile";
	
	public MainWindow(String propertyFile) {
		initProperties(propertyFile);
		initGui();
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

	private void initGui() {
		final JFrame mainFrame = new JFrame("Keno Application");
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(100, 100);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("Fájl");
		menubar.add(fileMenu);
		JMenuItem refreshMenuItem = new JMenuItem("Frissítés");
		fileMenu.add(refreshMenuItem);

		refreshMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final JDialog progressWindow = new JDialog(mainFrame, true);
						progressWindow.setTitle("Letöltés");
						progressWindow.setSize(200, 100);
						progressWindow.setLocation(100, 100);
						progressWindow.setResizable(false);
						progressWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						progressWindow.setLayout(new FlowLayout());
						
						final JLabel statusLabel = new JLabel("Letöltés...");
						progressWindow.add(statusLabel);
						JButton okButton = new JButton("OK");
						okButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								progressWindow.setVisible(false);
							}
						});
						progressWindow.add(okButton);
						
						try {
							new SwingWorker<Boolean, Void>() {
								@Override
								protected Boolean doInBackground() throws Exception {
									return new FileDownloader(properties.getProperty(CSV_URL_PROP),
											properties.getProperty(WORK_DIR_PROP)
											+ File.separator
											+ properties.getProperty(LOCAL_FILE_PROP))
									.download();
								}
								@Override
								protected void done() {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											statusLabel.setText("Kész.");
										}
									});
								}
							}.execute();
							progressWindow.setVisible(true);
						} catch (Exception ex) {
							LOGGER.error("File download interrupted.", ex);
						}
					}
				});
			}
		});
		
		mainFrame.setJMenuBar(menubar);
		mainFrame.setVisible(true);
	}

}
