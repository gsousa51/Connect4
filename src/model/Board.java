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

	// Used when we restart game. Resets board to empty.
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
		Space color = type;
		Space otherColor;
		if (type == Space.RED) {
			otherColor = Space.BLACK;
		} else
			otherColor = Space.RED;
		int moveR = move.y;
		int moveC = move.x;
		int blockR = block.y;
		int blockC = block.x;
		if (!this.validMove(moveR, moveC) || !this.validMove(blockR, blockC)) {
			return false;
		}
		// Comp takes its desired move
		this.setSpace(moveR, moveC, color);
		if (this.checkForWin(type)) {
			return true;
		}
		// If user didn't go here, we assume computer would to check for win
		// If computer couldn't win in spot anyways, user wouldn't have blocked

		this.setSpace(blockR, blockC, color);
		if (!this.checkForWin(color)) {
			resetSpaces(move, block);
			return false;
		} else if (type == Space.RED) {
			// If this would set up a win by the user, return false.
			this.setSpace(blockR, blockC, Space.BLACK);
			if (this.checkForWin(Space.BLACK)) {
				resetSpaces(move, block);
				return false;
			}
		} else
			this.setSpace(blockR, blockC, Space.RED);
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				if (this.validMove(r, c)) {
					this.setSpace(r, c, color);
					if (this.checkForWin(color)) {
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

	/**
	 * 
	 * @param row
	 *            of spot to inspect
	 * @param col
	 *            of spot to inspect
	 * @return true if user would be able to pull of double-kill or more false
	 *         is not.
	 */
	public boolean userCouldWin(int row, int col) {
		// Keep track of future win possibilities (obviously we'd prefer 0)
		int winPossibilities = 0;
		// Set the space to a user move temporarily
		this.setSpace(row, col, Space.BLACK);

		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				// If the spot is valid, we inspect
				if (this.validMove(r, c)) {
					// Temporarily set to user move to check future possibilites
					this.setSpace(r, c, Space.BLACK);
					// If this move would end up as a win for user
					if (this.checkForWin(Space.BLACK)) {
						// increment sum
						winPossibilities++;
					}
					// reset space to empty
					this.setSpace(r, c, Space.EMPTY);
					// If we found two win possiblities, we should take the
					// space

					if (winPossibilities >= 2) {
						this.setSpace(row, col, Space.EMPTY);
						return true;
					}
				}
			}
		}
		// If we get here, there were one or less possibilities to win.
		// If there was only one, our search later will snuff it out.
		this.setSpace(row, col, Space.EMPTY);
		return false;
	}

	/**
	 * 1) Go through the list of possible moves. 2) If there is somewhere user
	 * could go that would force a block AND they'd win after the block, take
	 * that space. JUST AS LONG AS IT WOULDN'T CAUSE THE USER TO WIN THE GAME IF
	 * WE WERE GOING TO GO THERE.
	 * 
	 * 
	 */
	public Point lookAhead() {
		int[] list = getListOfValidMoves();
		int tempR;
		int tempR2;

		for (int c = 0; c < list.length; c++) {
			if (list[c] >= 0 && !this.wouldCauseLoss(list[c], c)) {
				// Store row we just had user go.
				tempR = list[c];
				this.setSpace(list[c], c, Space.BLACK);
				// Set valid move up a row.
				list[c]--;
				// check to see if user would be able to create a win
				// after going in this hypothetical spot.
				for (int c2 = 0; c2 < list.length; c2++) {
					if (list[c2] >= 0) {
						this.setSpace(list[c2], c2, Space.BLACK);
						// If user went in place and it would cause a win
						// We would have to have blocked that spot.
						if (this.checkForWin(Space.BLACK)) {
							tempR2 = list[c2];
							// Put a block
							this.setSpace(list[c2], c2, Space.RED);
							list[c2]--;
							for (int c3 = 0; c3 < list.length; c3++) {
								if (list[c3] >= 0) {
									this.setSpace(list[c3], c3, Space.BLACK);
									if (checkForWin(Space.BLACK)) {
										this.setSpace(list[c3], c3, Space.EMPTY);
										this.setSpace(tempR2, c2, Space.EMPTY);
										this.setSpace(tempR, c, Space.EMPTY);
										return new Point(c, tempR);
									} // end if
									else{
										this.setSpace(list[c3], c3, Space.EMPTY);
									}
								}
							} // end for
							list[c2]++;
						} // end if
						this.setSpace(list[c2], c2, Space.EMPTY);
					}//end if
				} // end c2for
				this.setSpace(++list[c], c, Space.EMPTY);
			} // end if
		}
		return new Point(-1, -1);
	}

	public int[] getListOfValidMoves() {
		int[] list = new int[WIDTH];

		for (int c = 0; c < WIDTH; c++) {
			// Initialize as having no valid space in column
			list[c] = -1;
			for (int r = HEIGHT - 1; r >= 0; r--) {
				// We found a valid move, log it, break out of loop.
				if (board[r][c] == Space.EMPTY) {
					list[c] = r;
					break;
				}
			}
		}
		return list;
	}

	private boolean wouldCauseLoss(int r, int c) {
		boolean wouldCauseWin = false;
		/**
		 * If this move wasn't at the top space, we check. Otherwise it couldn't
		 * possibly allow a win. If there's only losing moves left, we just tell
		 * the comp it can't causea win It really has no other option
		 **/
		if (r > 0 && !this.onlyLosingMoves()) {
			this.setSpace(r - 1, c, Space.BLACK);
			wouldCauseWin = this.checkForWin(Space.BLACK);
			this.setSpace(r - 1, c, Space.EMPTY);
		}
		return wouldCauseWin;
	}
}
