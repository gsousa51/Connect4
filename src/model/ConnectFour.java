package model;

import java.awt.Point;
import java.util.Observable;

import javax.swing.JOptionPane;

import view.GUI;

public class ConnectFour extends Observable {
	private static final long serialVersionUID = 1L;
	private GUI gui;
	private Board board;
	private Computer comp;
	private int movesLeft;

	public ConnectFour() {
		this.board = new Board();
		this.comp = new Computer(this);
		movesLeft = 42;
	}

	public void userMove(int row, int col) {
		board.setSpace(row, col, Space.BLACK);
		movesLeft--;
		if (board.checkForWin(Space.BLACK)) {
			setChanged();
			notifyObservers();
			int reply = JOptionPane.showConfirmDialog(null, "You Won! \n Play Again?", "WINNER",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				board.reset();
				movesLeft = 42;
				setChanged();
				notifyObservers();
			} else {
				JOptionPane.showMessageDialog(null, "GOODBYE");
				System.exit(0);
			}
		} else if (board.boardFull()) {
			setChanged();
			notifyObservers();
			int reply = JOptionPane.showConfirmDialog(null, "Tie! \n Play Again?", "WINNER", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				board.reset();
				movesLeft = 42;
				setChanged();
				notifyObservers();
			} else {
				JOptionPane.showMessageDialog(null, "GOODBYE");
				System.exit(0);
			}
		} else {
			Point compMove = comp.getMove();
			board.setSpace(compMove.y, compMove.x, Space.RED);
			movesLeft--;
			setChanged();
			notifyObservers();

			if (board.checkForWin(Space.RED)) {
				int reply = JOptionPane.showConfirmDialog(null, "You Lost... \n Play Again?", "Woops...",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					board.reset();
					movesLeft = 42;
					setChanged();
					notifyObservers();
				} else {
					JOptionPane.showMessageDialog(null, "Thanks for Playing!");
					System.exit(0);
				}
			} else if (board.boardFull()) {
				setChanged();
				notifyObservers();
				int reply = JOptionPane.showConfirmDialog(null, "Tie! \n Play Again?", "WINNER",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					board.reset();
					movesLeft = 42;
					setChanged();
					notifyObservers();
				} else {
					JOptionPane.showMessageDialog(null, "GOODBYE");
					System.exit(0);
				}
			}
		}
	}

	/**
	 * Makes a hypothetical move by user above where computer wants to go If
	 * their move would allow a win by the user, return true. If it's a safe
	 * move, return false.
	 */
	public boolean wouldCauseLoss(int r, int c) {
		boolean wouldCauseWin = false;
		/**If this move wasn't at the top space, we check.
		*Otherwise it couldn't possibly allow a win.
		*If there's only losing moves left, we just tell the comp it can't causea win
		*It really has no other option
		**/
		if (r > 0 && !board.onlyLosingMoves()) {
			board.setSpace(r - 1, c, Space.BLACK);
			wouldCauseWin = board.checkForWin(Space.BLACK);
			board.setSpace(r - 1, c, Space.EMPTY);
		}
		return wouldCauseWin;
	}

	public boolean validMove(int r, int c) {
		return board.validMove(r, c);
	}

	public Space getSpace(int r, int c) {
		return board.getSpace(r, c);
	}
	public boolean killShot(Space type , Point move, Point block){
		return board.killShot(type, move, block)&&!wouldCauseLoss(move.y,move.x);
	}
	public Board getBoard(){
		//FOR TESTING
		return board;
	}
	public Computer getComp(){
		return comp;
	}
}
