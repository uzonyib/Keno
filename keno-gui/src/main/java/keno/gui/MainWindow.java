package keno.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

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
		final KenoApp app = KenoApp.getInstance();
		final ResourceBundle bundle = app.getResourceBundle();
		
		final JFrame mainFrame = new JFrame("Keno Application");
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(100, 100);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu(bundle.getString("menu.file"));
		menubar.add(fileMenu);
		JMenuItem refreshMenuItem = new JMenuItem(bundle.getString("menu.file.refresh"));
		fileMenu.add(refreshMenuItem);

		refreshMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final JDialog progressWindow = new JDialog(mainFrame, true);
						progressWindow.setTitle(bundle.getString("info.title"));
						progressWindow.setSize(300, 100);
						progressWindow.setLocation(100, 100);
						progressWindow.setResizable(false);
						progressWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						progressWindow.setLayout(new FlowLayout());
						
						final JLabel statusLabel = new JLabel(bundle.getString("info.refresh.status.downloading"));
						progressWindow.add(statusLabel);
						JButton okButton = new JButton(bundle.getString("info.ok"));
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
									return new FileDownloader(app.getSourceUrl(),
											app.getLotteryFile()).download();
								}
								@Override
								protected void done() {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											Draw draw = app.getLotteryService().getMostRecentDraw();
											String info = draw == null ? "" : " " + bundle.getString("info.lastdraw") + ": " + new SimpleDateFormat("yyyy.mm.dd.").format(draw.getDate());
											statusLabel.setText(bundle.getString("info.refresh.status.done") + info);
											progressWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
