package model;

import java.awt.Point;
import java.util.ArrayDeque;

public class Board {
	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;
	private Space[][] board;

	public Board() {
		this.board = new Space[HEIGHT][WIDTH];
		// Initialize the board to be empty
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				board[r][c] = Space.EMPTY;
			}
		}
	}

	public Space[][] getBoard() {
		return this.board;
	}

	public void reset() {
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 7; c++) {
				board[r][c] = Space.EMPTY;
			}
		}
	}

	public Space getSpace(int row, int col) {
		return this.board[row][col];
	}

	public void setSpace(int row, int col, Space move) {
		this.board[row][col] = move;
	}

	public boolean checkForWin(Space type) {
		return checkHorizontalWin(type) || checkVertWin(type) || checkForwardDiagWin(type)
				|| checkBackwardDiagWin(type);

	}

	private boolean checkHorizontalWin(Space type) {
		int adj;
		int indx;
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c <= WIDTH - 4; c++) {
				// reset the adjacent sum to 0
				adj = 0;
				indx = 0;

				while (indx < 4) {
					if (board[r][c + indx] == type) {
						adj++;
						indx++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else
						break;
				}
				// If we found 4 in a row, game is over
				if (adj == 4) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkVertWin(Space type) {
		int adj;
		int indx;
		for (int c = 0; c < WIDTH; c++) {
			for (int r = 0; r <= HEIGHT - 4; r++) {
				// reset the adjacent sum to 0
				adj = 0;
				indx = 0;

				while (indx < 4) {
					if (board[r + indx][c] == type) {
						adj++;
						indx++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else
						break;
				}
				// If we found 4 in a row, game is over
				if (adj == 4) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkForwardDiagWin(Space type) {
		int adj;
		int indx;
		for (int c = 0; c <= WIDTH - 4; c++) {
			for (int r = 0; r <= HEIGHT - 4; r++) {
				// reset the adjacent sum to 0
				adj = 0;
				indx = 0;

				while (indx < 4) {
					if (board[r + indx][c + indx] == type) {
						adj++;
						indx++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else
						break;
				}
				// If we found 4 in a row, game is over
				if (adj == 4) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkBackwardDiagWin(Space type) {
		int adj;
		int indx;
		for (int c = 3; c < WIDTH; c++) {
			for (int r = 0; r <= HEIGHT - 4; r++) {
				adj = 0;
				indx = 0;

				while (indx < 4) {
					if (board[r + indx][c - indx] == type) {
						adj++;
						indx++;
					} else
						break;
					// If we found 4 in a row, game is over
					if (adj == 4) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// Will be used exclusively by our AI. Checks that space is a valid move
	// before computer chooses it.
	public boolean validMove(int row, int col) {
		if (row >= HEIGHT || row < 0 || col >= WIDTH || col < 0) {
			return false;
		}
		if (board[row][col] != Space.EMPTY) {
			return false;
		}
		// Make sure the piece will be able to land on the bottom of board or on
		// top of another piece
		if (row == HEIGHT - 1 || board[row + 1][col] != Space.EMPTY) {
			return true;
		} else
			return false;
	}

	public boolean boardFull() {
		// Check for any empty spots on top row.
		// The only way the board could be full if it's filled to brim.
		for (int c = 0; c < board[0].length; c++) {
			if (board[0][c] == Space.EMPTY) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return True if all valid moves would result in user winning
	 */
	public boolean onlyLosingMoves() {
		boolean wouldLose = false;
		// If there are ANY valid moves on the top row, return false.
		for (int c = 0; c < board[0].length; c++) {
			if (this.validMove(0, c))
				return false;
		}
		for (int c = 0; c < board[0].length; c++) {
			for (int r = 1; r < board.length; r++) {
				if (this.validMove(r, c)) {
					// Set this hypothetical space to red
					this.setSpace(r, c, Space.RED);
					// Set the space above it to black
					// This tests if this move would be a losing move
					this.setSpace(r - 1, c, Space.BLACK);
					wouldLose = this.checkForWin(Space.BLACK);
					this.setSpace(r, c, Space.EMPTY);
					this.setSpace(r - 1, c, Space.EMPTY);
					// If there's a valid space to go without causing a victory
					if (!wouldLose) {
						return false;
					} else
						// else reset the boolean back to false.
						wouldLose = false;
				}
			}
		}
		// If we get here, every valid move is going to be a losing move.
		return true;
	}

	/**
	 * Parameters: Two empty points found by the computer when searching for a
	 * move that would create a string of size 3. The computer sends in the
	 * intended move, and where the move would be blocked. If the user blocks
	 * them and there's another place for the computer to go to win This would
	 * be considered a killshot.
	 **/
	public boolean killShot(Space type, Point move, Point block) {
		int moveR = move.y;
		int moveC = move.x;
		int blockR = block.y;
		int blockC = block.x;
		if (type != Space.RED || !this.validMove(moveR, moveC) || !this.validMove(blockR, blockC)) {
			return false;
		}
		// Comp takes its desired move
		this.setSpace(moveR, moveC, Space.RED);
		// If user didn't go here, we assume computer would to check for win
		// If computer couldn't win in spot anyways, user wouldn't have blocked

		this.setSpace(blockR, blockC, Space.RED);
		if (!this.checkForWin(Space.RED)) {
			resetSpaces(move, block);
			return false;
		} else {
			// If this would set up a win by the user, return false.
			this.setSpace(blockR, blockC, Space.BLACK);
			if (this.checkForWin(Space.BLACK)) {
				resetSpaces(move, block);
				return false;
			}

		}
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				if (this.validMove(r, c)) {
					this.setSpace(r, c, Space.RED);
					if (this.checkForWin(Space.RED)) {
						resetSpaces(move, block);
						this.setSpace(r, c, Space.EMPTY);
						return true;
					} else
						this.setSpace(r, c, Space.EMPTY);
				}
			}
		}
		resetSpaces(move, block);
		return false;
	}

	private void resetSpaces(Point move, Point block) {
		this.setSpace(move.y, move.x, Space.EMPTY);
		this.setSpace(block.y, block.x, Space.EMPTY);
	}

	public boolean userCouldWin(int row, int col) {
		int winPossibilities = 0;
		this.setSpace(row, col, Space.BLACK);
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (this.validMove(r, c)) {
					this.setSpace(r, c, Space.BLACK);
					if (this.checkForWin(Space.BLACK)) {
						winPossibilities++;
					}
					this.setSpace(r, c, Space.EMPTY);
					if (winPossibilities >= 2) {
						this.setSpace(r, c, Space.EMPTY);
						this.setSpace(row, col, Space.EMPTY);
						return true;
					}
				}
			}
		}
		this.setSpace(row, col, Space.EMPTY);
		return false;
	}
}
