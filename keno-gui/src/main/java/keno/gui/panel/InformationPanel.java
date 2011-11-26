package keno.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import keno.KenoApp;
import keno.model.Draw;
import keno.service.LotteryService;
import keno.util.download.FileDownloader;

public class InformationPanel extends JPanel {

	private static final String DATE_FORMAT_KEY = "app.dateformat";
	private static final String REFRESH_KEY = "info.refresh";
	private static final String STATUS_DOWNLOADING_KEY = "info.status.downloading";
	private static final String STATUS_MISSING_KEY = "info.status.missing";
	private static final String STATUS_LAST_DRAW_KEY = "info.status.lastdraw";

	private static final long serialVersionUID = 1L;
	
	private JLabel statusLabel;
	private JButton refreshButton;
	
	public InformationPanel() {
		init();
	}

	private void init() {
		final KenoApp app = KenoApp.getInstance();
		final ResourceBundle bundle = app.getResourceBundle();
		final LotteryService service = app.getLotteryService();
		
		statusLabel = new JLabel(getStatus(service.getMostRecentDraw(), bundle));
		refreshButton = new JButton(bundle.getString(REFRESH_KEY));
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				statusLabel.setText(bundle.getString(STATUS_DOWNLOADING_KEY));
				refreshButton.setEnabled(false);
				
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
								statusLabel.setText(getStatus(service.getMostRecentDraw(), bundle));
								refreshButton.setEnabled(true);
							}
						});
					}
				}.execute();
			}
		});
		
		add(statusLabel);
		add(refreshButton);
	}
	
	public static String getStatus(Draw mostRecent, ResourceBundle bundle) {
		return mostRecent == null ? bundle.getString(STATUS_MISSING_KEY) : 
			bundle.getString(STATUS_LAST_DRAW_KEY)
				+ ": " + new SimpleDateFormat(bundle.getString(DATE_FORMAT_KEY))
					.format(mostRecent.getDate());
	}

}
