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
		refreshButton = new JButton(bundle.getString("info.refresh"));
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				statusLabel.setText(bundle.getString("info.refresh.status.downloading"));
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
		return mostRecent == null ? bundle.getString("info.missing") : 
			bundle.getString("info.lastdraw")
				+ ": " + new SimpleDateFormat("yyyy.mm.dd.")
					.format(mostRecent.getDate());
	}

}
