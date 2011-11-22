package keno.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import keno.KenoApp;
import keno.model.Draw;
import keno.util.download.FileDownloader;

import org.apache.log4j.Logger;

public class MainWindow {
	
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);
	
	public MainWindow() {
		initGui();
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
						progressWindow.setSize(300, 100);
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
									return new FileDownloader(KenoApp.INSTANCE.getSourceUrl(),
											KenoApp.INSTANCE.getLotteryFile()).download();
								}
								@Override
								protected void done() {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											Draw draw = KenoApp.INSTANCE.getLotteryService().getMostRecentDraw();
											String info = draw == null ? "" : " Utolsó húzás: " + new SimpleDateFormat("yyyy.mm.dd.").format(draw.getDate());
											statusLabel.setText("Kész." + info);
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
