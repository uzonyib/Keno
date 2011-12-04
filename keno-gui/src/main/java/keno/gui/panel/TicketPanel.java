package keno.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import keno.KenoApp;
import keno.model.NumberState;

public class TicketPanel extends JPanel {

	private static class TicketButton extends JButton {
		
		private static final long serialVersionUID = 1L;
		
		private static Color defaultColor = Color.WHITE;
		private static Color selectedColor = Color.GREEN;
		
		private TicketPanel ticketPanel;
		
		private byte number;
		private NumberState state;

		public TicketButton(TicketPanel ticketPanel, byte number) {
			super("" + number);
			this.number = number;
			this.ticketPanel = ticketPanel;
			setState(NumberState.ANY);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					toggle();
				}
			});
		}
		
		public NumberState getState() {
			return state;
		}
		
		public byte getNumber() {
			return number;
		}
		
		public void setState(NumberState state) {
			if (state == null) {
				this.state = NumberState.ANY;
			} else {
				this.state = state;
			}
			
			switch (this.state) {
			case SELECTED:
				setBackground(selectedColor);
				break;
			default:
				setBackground(defaultColor);
				break;
			}
		}
		
		public void toggle() {
			switch (state) {
			case ANY:
				if (ticketPanel.getSelectedCount() < MAX_SELECTIONS) {
					setState(NumberState.SELECTED);
				}
				break;
			default:
				setState(NumberState.ANY);
				break;
			}
			
			ticketPanel.refresh();
		}
		
	}
	
	private static class HitCheckBox extends JCheckBox {
		
		private static final long serialVersionUID = 1L;
		
		private byte hitCount;

		public HitCheckBox(byte hitCount, String label, boolean selected) {
			super(label, selected);
			this.hitCount = hitCount;
		}

		public byte getHitCount() {
			return hitCount;
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	
	private static final String COLOR_DEFAULT_KEY = "draws.color.default";
	private static final String COLOR_SELECTED_KEY = "draws.color.selected";
	
	private static final String TICKET_HIT_COUNT_KEY = "draws.ticket.hitcount";
	private static final String TICKET_CLEAR_KEY = "draws.ticket.clear";
	
	private static final int MAX_SELECTIONS = 10;
	
	private DrawsPanel drawsPanel;
	
	private JPanel buttonPanel;
	private Box buttonBox;
	private Box hitCountBox;
	
	private List<TicketButton> buttons;
	private List<HitCheckBox> checkBoxes;
	private JButton clearTicket;
	
	private String checkBoxLabel;

	public TicketPanel(DrawsPanel drawsPanel) {
		this.drawsPanel = drawsPanel;
		init();
	}
	
	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		checkBoxLabel = bundle.getString(TICKET_HIT_COUNT_KEY);
		
		TicketButton.defaultColor = Color.decode(bundle.getString(COLOR_DEFAULT_KEY));
		TicketButton.selectedColor = Color.decode(bundle.getString(COLOR_SELECTED_KEY));
		
		buttonPanel = new JPanel(new GridLayout(8, 10));
		
		buttons = new ArrayList<TicketButton>();
		for (byte i = 1; i <= 80; ++i) {
			TicketButton button = new TicketButton(this, i);
			button.setPreferredSize(new Dimension(50, 30));
			buttons.add(button);
			buttonPanel.add(button);
		}
		
		buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(buttonPanel);
		buttonBox.add(Box.createHorizontalGlue());
		
		clearTicket = new JButton(bundle.getString(TICKET_CLEAR_KEY));
		clearTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				TicketPanel.this.clear();
				TicketPanel.this.refresh();
			}
		});
		
		checkBoxes = new ArrayList<TicketPanel.HitCheckBox>();
		hitCountBox = Box.createVerticalBox();
		hitCountBox.add(clearTicket);
		
		Box box = Box.createHorizontalBox();
		box.add(buttonBox);
		box.add(hitCountBox);
		
		add(box);
	}
	
	public List<Byte> getSelectedNumbers() {
		List<Byte> numbers = new ArrayList<Byte>();
		for (TicketButton button : buttons) {
			if (button.getState() == NumberState.SELECTED) {
				numbers.add(button.getNumber());
			}
		}
		return numbers;
	}
	
	public void clear() {
		for (TicketButton button : buttons) {
			button.setState(NumberState.ANY);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (TicketButton button : buttons) {
			button.setEnabled(enabled);
		}
		for (HitCheckBox checkBox : checkBoxes) {
			checkBox.setEnabled(enabled);
		}
		clearTicket.setEnabled(enabled);
	}
	
	private int getSelectedCount() {
		int maxHitCount = 0;
		for (TicketButton button : buttons) {
			if (button.getState() == NumberState.SELECTED) {
				++maxHitCount;
			}
		}
		return maxHitCount;
	}
	
	private void refresh() {
		checkBoxes = new ArrayList<TicketPanel.HitCheckBox>();
		switch (getSelectedCount()) {
		case 1:
			checkBoxes.add(createHitCheckBox((byte) 1, true));
			break;
		case 2:
			checkBoxes.add(createHitCheckBox((byte) 2, true));
			break;
		case 3:
			checkBoxes.add(createHitCheckBox((byte) 2));
			checkBoxes.add(createHitCheckBox((byte) 3, true));
			break;
		case 4:
			checkBoxes.add(createHitCheckBox((byte) 3));
			checkBoxes.add(createHitCheckBox((byte) 4, true));
			break;
		case 5:
			checkBoxes.add(createHitCheckBox((byte) 3));
			checkBoxes.add(createHitCheckBox((byte) 4));
			checkBoxes.add(createHitCheckBox((byte) 5, true));
			break;
		case 6:
			checkBoxes.add(createHitCheckBox((byte) 4));
			checkBoxes.add(createHitCheckBox((byte) 5));
			checkBoxes.add(createHitCheckBox((byte) 6, true));
			break;
		case 7:
			checkBoxes.add(createHitCheckBox((byte) 4));
			checkBoxes.add(createHitCheckBox((byte) 5));
			checkBoxes.add(createHitCheckBox((byte) 6));
			checkBoxes.add(createHitCheckBox((byte) 7, true));
			break;
		case 8:
			checkBoxes.add(createHitCheckBox((byte) 5));
			checkBoxes.add(createHitCheckBox((byte) 6));
			checkBoxes.add(createHitCheckBox((byte) 7));
			checkBoxes.add(createHitCheckBox((byte) 8, true));
			break;
		case 9:
			checkBoxes.add(createHitCheckBox((byte) 5));
			checkBoxes.add(createHitCheckBox((byte) 6));
			checkBoxes.add(createHitCheckBox((byte) 7));
			checkBoxes.add(createHitCheckBox((byte) 8));
			checkBoxes.add(createHitCheckBox((byte) 9, true));
			break;
		case 10:
			checkBoxes.add(createHitCheckBox((byte) 6));
			checkBoxes.add(createHitCheckBox((byte) 7));
			checkBoxes.add(createHitCheckBox((byte) 8));
			checkBoxes.add(createHitCheckBox((byte) 9));
			checkBoxes.add(createHitCheckBox((byte) 10, true));
			break;
		default:
			break;
		}
		
		hitCountBox.removeAll();
		hitCountBox.add(clearTicket);
		for (HitCheckBox hitCheckBox : checkBoxes) {
			hitCountBox.add(hitCheckBox);
		}
		
		validate();
		repaint();
		drawsPanel.refreshTable();
	}
	
	private HitCheckBox createHitCheckBox(byte hitCount) {
		return createHitCheckBox(hitCount, false);
	}
	
	private HitCheckBox createHitCheckBox(byte hitCount, boolean selected) {
		HitCheckBox hitCheckBox = new HitCheckBox(hitCount,
				MessageFormat.format(checkBoxLabel, hitCount), selected);
		hitCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				TicketPanel.this.drawsPanel.refreshTable();
			}
		});
		return hitCheckBox;
	}
	
	public List<Byte> getSelectedHitCounts() {
		List<Byte> counts = new ArrayList<Byte>();
		for (HitCheckBox hitCheckBox : checkBoxes) {
			if (hitCheckBox.isSelected()) {
				counts.add(hitCheckBox.getHitCount());
			}
		}
		return counts;
	}

}
