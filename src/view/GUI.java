package view;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.ConnectFour;
import model.Space;


public class GUI extends JPanel implements Observer{
	private JButton[][] buttons;
	private ConnectFour game;
	private final static int height  = 360;
	private final static int width = 300;
	

	public void setGame(ConnectFour game){
		this.game = game;
		initializeGame();
	}
	private void initializeGame() {
		buttons = new JButton[6][7];
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6,7, 5,5));
		ButtonListener buttonListener = new ButtonListener();
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 7; c++) {
				buttons[r][c] = new JButton();
				buttons[r][c].setBackground(Color.GRAY);
				buttons[r][c].addActionListener(buttonListener);
				buttonPanel.add(buttons[r][c]);
			}
		}
	    this.setLayout(null);
	    buttonPanel.setLocation(10, 5);
	    buttonPanel.setSize(width - 30, height - 110);
	    buttonPanel.setBackground(Color.green);
	    updateButtons();
		this.add(buttonPanel);
	}

	private void updateButtons() {
		for (int r = 0; r < buttons.length; r++) {
			for (int c = 0; c < buttons[0].length; c++) {
				if (game.validMove(r, c)) {
					buttons[r][c].setEnabled(true);
				} else
					buttons[r][c].setEnabled(false);
				if (game.getSpace(r, c) == Space.BLACK) {
					buttons[r][c].setBackground(Color.BLACK);
				} else if (game.getSpace(r, c) == Space.RED) {
					buttons[r][c].setBackground(Color.RED);
				} else
					buttons[r][c].setBackground(Color.GRAY);
			}
		}
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton buttonPress = (JButton) e.getSource();
			for (int r = 0; r < buttons.length; r++) {
				for (int c = 0; c < buttons[0].length; c++) {
					if (buttons[r][c] == buttonPress) {
						game.userMove(r, c);
					}
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
			updateButtons();
	}

}
