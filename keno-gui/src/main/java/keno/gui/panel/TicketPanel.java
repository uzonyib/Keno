package keno.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import keno.KenoApp;
import keno.model.NumberState;

public class TicketPanel extends JPanel {
	
	private static class TicketButton extends JButton {
		
		private static final long serialVersionUID = 1L;
		
		private static Color defaultColor = Color.WHITE;
		private static Color selectedColor = Color.GREEN;
		private static Color unselectedColor = Color.RED;
		
		private NumberState state;

		public TicketButton(String label) {
			super(label);
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
			case UNSELECTED:
				setBackground(unselectedColor);
				break;
			default:
				setBackground(defaultColor);
				break;
			}
		}
		
		public void toggle() {
			switch (state) {
			case ANY:
				setState(NumberState.SELECTED);
				break;
			case SELECTED:
				setState(NumberState.UNSELECTED);
				break;
			default:
				setState(NumberState.ANY);
				break;
			}
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	
	private static final String COLOR_DEFAULT_KEY = "draws.color.default";
	private static final String COLOR_SELECTED_KEY = "draws.color.selected";
	private static final String COLOR_UNSELECTED_KEY = "draws.color.unselected";
	
	private JPanel buttonPanel;
	private Box buttonBox;
	
	private List<TicketButton> buttons;

	public TicketPanel() {
		init();
	}
	
	private void init() {
		KenoApp app = KenoApp.getInstance();
		ResourceBundle bundle = app.getResourceBundle();
		
		TicketButton.defaultColor = Color.decode(bundle.getString(COLOR_DEFAULT_KEY));
		TicketButton.selectedColor = Color.decode(bundle.getString(COLOR_SELECTED_KEY));
		TicketButton.unselectedColor = Color.decode(bundle.getString(COLOR_UNSELECTED_KEY));
		
		buttonPanel = new JPanel(new GridLayout(8, 10));
		
		buttons = new ArrayList<TicketButton>();
		for (int i = 0; i < 80; ++i) {
			TicketButton button = new TicketButton("" + (i + 1));
			button.setPreferredSize(new Dimension(50, 30));
			buttons.add(button);
			buttonPanel.add(button);
		}
		
		buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(buttonPanel);
		buttonBox.add(Box.createHorizontalGlue());
		
		add(buttonBox);
	}
	
	public List<NumberState> getNumberStates() {
		List<NumberState> states = new ArrayList<NumberState>();
		for (TicketButton button : buttons) {
			states.add(button.getState());
		}
		return states;
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
	}

}
