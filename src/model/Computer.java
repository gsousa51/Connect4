package model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Computer {
	private Deque<Point> possibleMoves;
	private Point move;
	private ConnectFour game;

	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;

	public Computer(ConnectFour game) {
		possibleMoves = new ArrayDeque<Point>();
		this.game = game;
	}

	/**
	 * Method used by controller to get move from Computer. Computer first looks
	 * for a winning move then searches for a way to stop an imminent victory by
	 * user.
	 */
	public Point getMove() {
		// Start looking for a move based on a string of size 4
		// Countdown from there.
		for (int size = 4; size >= 3; size--) {
			// Look for winning move in 4 directions
			if (checkDiag(Space.RED, size))
				return move;
			else if (checkHor(Space.RED, size))
				return move;
			else if (checkVert(Space.RED, size))
				return move;
			else if (checkBackDiag(Space.RED, size))
				return move;
			// We look for a way to stop a possible setup from the user
			// by looking one step ahead of the user.
			// But only if the size is less than 4 because the other methods
			// more effectively stop a winning move.
			// else if (size<4 && searchForBlock() ) {
			// return move;
			// }
			// Next we look for a move to block a possible win by the user
			else if (checkHor(Space.BLACK, size))
				return move;
			else if (checkVert(Space.BLACK, size))
				return move;
			else if (checkDiag(Space.BLACK, size))
				return move;
			else if (checkBackDiag(Space.BLACK, size))
				return move;

		}
		// If we get here, none of the above moves worked out.
		// Look for one from our movelist.
		Point peek;
		// If we find neither, we go to the list of useful moves we gathered
		while (!possibleMoves.isEmpty()) {
			peek = possibleMoves.peek();
			// If the move is valid and doesn't cause us to lose, choose it.
			if (game.validMove(peek.y, peek.x) && !game.wouldCauseLoss(peek.y, peek.x)) {
				System.out.println("Move Chose By : MoveList");
				return peek;
			}
			// Else, check the next one.
			else {
				possibleMoves.pop();
			}
		}
		// If we get here, none of the possibleMoves worked.
		// Choose a random move.
		System.out.println("Random Move Chosen");

		Random randX = new Random();
		Random randY = new Random();
		Point randP = new Point(randX.nextInt(WIDTH), randY.nextInt(HEIGHT));
		// Make sure the move is valid and won't cause us to lose.
		while (!game.validMove(randP.y, randP.x) || game.wouldCauseLoss(randP.y, randP.x)) {
			randP = new Point(randX.nextInt(WIDTH), randY.nextInt(HEIGHT));
		}
		return randP;
	}

	/**
	 * Look for a move in the vertical direction that either completes "size" in
	 * row OR blocks the user's "size" in a row Method goes through grid,
	 * inspecting sections of size four that are aligned veritcally. If it finds
	 * three spaces that are of parameter type, we fill in the blank space that
	 * would connect them. If it finds a blank spaces but we don't have an
	 * adajacency sum of 3 it adds it to our possible moves list to check later.
	 * At time of adding to our movelist, if adj sum is >=2, it pushes it to
	 * front otherwise it adds move to back of list.
	 * 
	 * @param Space
	 *            type (Red or Black)
	 * @return Point containing row and column of our move
	 */
	private boolean checkVert(Space type, int size) {
		// Calculating a start allows to avoid redundant searching
		int start = Math.abs(4 - size);
		// Used for debugging purposes
		Point starting;
		// Counts number of spaces we found in our section of four spaces
		int adj;
		// Used to count out four spaces in a row
		int indx;
		// Used to keep track of blankspaces found in our section of four.
		Point blankSpace = new Point();
		Point blankSpace2 = new Point();
		int blankFound = 0;
		for (int c = 0; c < WIDTH; c++) {
			for (int r = start; r <= HEIGHT - size; r++) {
				// reset the adjacent sum to 0
				adj = 0;
				indx = 0;
				blankFound = 0;
				// Make an invalid blankSpace to start
				// (Helps to prevent adding the blankspace for an unrelated
				// section)
				blankSpace = new Point(-1, -1);
				blankSpace2 = new Point(-1, -1);
				starting = new Point(c, r);
				// Inspect our section of four.
				while (indx < size) {
					// Go from top to bottom of section
					if (game.getSpace(r + indx, c) == type) {
						adj++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else {
						// If there was an empty space found and it's adjacent
						// to a type we're searching for
						// add it to our possible moves
						// This could either block the user or start a string
						// for comp

						if (game.getSpace(r + indx, c) == Space.EMPTY) {
							blankFound++;
							// Store space into our blankspace var
							if (blankFound == 1)
								blankSpace = new Point(c, r + indx);
							else if (blankFound == 2) {
								/**
								 * If we found two blanks, there's a possiblity
								 * of a "Double Kill" move. We could create a
								 * string of size 3 spaces, causing user to have
								 * to block, then we may be able to capitalize
								 * elsewhere
								 */
								blankSpace2 = new Point(c, r + indx);
								if (size != 4 && game.killShot(type, blankSpace, blankSpace2) && size != 4) {
									move = blankSpace;
									// TODO: Delete debugging console output
									System.out.println("---KILLSHOT VERT---");
									return true;
								} else if (size != 4 && game.killShot(type, blankSpace2, blankSpace)) {
									move = blankSpace2;
									System.out.println("---KILLSHOT VERT---");
									return true;
								}
							}
							// If its a valid move, add it to our possiblemoves
							if (game.validMove(r + indx, c)) {
								// If adj is high, push it to front
								if (adj >= 2) {
									possibleMoves.push(blankSpace);
								} else {
									// otherwise add at back
									possibleMoves.addLast(blankSpace);
								}
							} // end if
						} // end if
						else {
							// Else, if we ran into the opposite piece, abandon
							// section
							// It is useless in this version of search
							break;
						} // end else
					} // end else
					indx++;
				} // end while
					// If we found size-1 occupied by parameter type
					// We check to make sure the blankspace found is valid.
					// If so, we store it in our selection variable and return
					// true.
				if (adj == size - 1) {
					if (game.validMove(blankSpace.y, blankSpace.x)) {
						// If it's a winning or blocking move, immediately use
						// it.
						if (size == 4) {
							System.out.println("Move Chose By : Vert");
							System.out.println(starting);
							move = blankSpace;
							return true;
						}
					}
				}
			}
		}
		// If we get here we didn't find a winning/blocking or doublekill move
		return false;
	}

	// Method is identical to the above, just horizontal searches
	private boolean checkHor(Space type, int size) {
		Point starting;
		int adj;
		int indx;
		int i = 0;
		int start = Math.abs(4 - size);
		boolean blankSpaceLogged = false;

		Point blankSpace = new Point();
		Point blankSpace2 = new Point();
		int blanksFound = 0;
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = start; c <= WIDTH - size; c++) {
				// reset the adjacent sum to 0
				adj = 0;
				indx = 0;
				blanksFound = 0;
				blankSpace = new Point(-1, -1);
				blankSpace2 = new Point(-1, -1);
				starting = new Point(c, r);
				blankSpaceLogged = false;
				i++;
				while (indx < size) {
					if (game.getSpace(r, c + indx) == type) {
						adj++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else {
						// If there was an empty space found and it's adjacent
						// to a type we're searching for
						// add it to our possible moves
						// This could either block the user or start a string
						// for comp

						if (game.getSpace(r, c + indx) == Space.EMPTY) {
							blanksFound++;
							if (blanksFound == 1)
								blankSpace = new Point(c + indx, r);
							else if (blanksFound == 2) {
								blankSpace2 = new Point(c + indx, r);
								if (size != 4 && game.killShot(type, blankSpace, blankSpace2)) {
									move = blankSpace;
									System.out.println("----KILLSHOT HOR----");
									return true;
								} else if (size != 4 && game.killShot(type, blankSpace2, blankSpace)) {
									move = blankSpace2;
									System.out.println("----KILLSHOT HOR----");
									return true;
								}
							}
							if (game.validMove(r, c + indx)) {
								if (adj >= 2 && !blankSpaceLogged) {
									blankSpaceLogged = true;
									possibleMoves.push(blankSpace);
								} else {
									possibleMoves.addLast(blankSpace);
									blankSpaceLogged = true;
								}
							}
						} else
							break;
					}
					indx++;
				} // end while
					// If we found three adjacent spaces occupied by parameter
					// type
					// We check to see if the blank space was valid
				if (adj == size - 1) {
					if (size == 4) {
						if (game.validMove(blankSpace.y, blankSpace.x)) {
							System.out.println("Move Chose By : Hor");
							System.out.println(starting);
							move = blankSpace;
							return true;
						} // end if
					}
				} // end if
			} // end for
		} // end for
		return false;
	}

	/**
	 * Identical to above methods, but searches diagonally
	 * 
	 * i.e :
	 *
	 *
	 *
	 *
	 * 
	 */
	private boolean checkDiag(Space type, int size) {
		Point starting;
		int adj;
		int indx;
		int blanksFound = 0;
		Point blankSpace = new Point();
		Point blankSpace2 = new Point();
		for (int c = size - 1; c < WIDTH; c++) {
			for (int r = 0; r <= HEIGHT - size; r++) {
				adj = 0;
				indx = 0;
				blanksFound = 0;
				blankSpace = new Point(-1, -1);
				blankSpace2 = new Point(-1, -1);
				starting = new Point(c, r);
				while (indx < size) {
					if (game.getSpace(r + indx, c - indx) == type) {
						adj++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else {
						// If there was an empty space found and it's adjacent
						// to a type we're searching for
						// add it to our possible moves
						// This could either block the user or start a string
						// for comp

						if (game.getSpace(r + indx, c - indx) == Space.EMPTY) {
							blanksFound++;
							if (blanksFound == 1)
								blankSpace = new Point(c - indx, r + indx);
							else if (blanksFound == 2) {
								blankSpace2 = new Point(c - indx, r + indx);
								if (size != 4 && game.killShot(type, blankSpace, blankSpace2)) {
									move = blankSpace;
									System.out.println("----KILLSHOT DIAG----");
									return true;
								} else if (size != 4 && game.killShot(type, blankSpace2, blankSpace)) {
									move = blankSpace2;
									System.out.println("----KILLSHOT DIAG----");
									return true;
								}

							}
							if (game.validMove(r + indx, c - indx)) {
								if (adj >= 2) {
									possibleMoves.push(blankSpace);
								} else
									possibleMoves.add(blankSpace);
							}
						} else {
							break;
						}

					} // end else
					indx++;
				} // end while
					// If we found size-1 spaces occupied by parameter type
					// We check the end points to see if there's a valid move
				if (adj == size - 1) {
					if (size == 4) {
						if (game.validMove(blankSpace.y, blankSpace.x)) {
							System.out.println("Move Chose By : Diag");
							System.out.println(starting);
							move = blankSpace;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean checkBackDiag(Space type, int size) {
		Point starting;
		int adj;
		int indx;
		int blanksFound = 0;
		Point blankSpace = new Point();
		Point blankSpace2 = new Point();
		for (int c = WIDTH - 1; c >= size - 1; c--) {
			for (int r = HEIGHT - 1; r >= size - 1; r--) {
				adj = 0;
				indx = 0;
				blanksFound = 0;
				starting = new Point(c, r);
				blankSpace = new Point(-1, -1);
				blankSpace2 = new Point(-1, -1);

				while (indx < size) {
					if (game.getSpace(r - indx, c - indx) == type) {
						adj++;
					}
					// else, we hit a space not occupied by the type we're
					// looking for
					// exit the loop.
					else {
						// If there was an empty space found and it's adjacent
						// to a type we're searching for
						// add it to our possible moves
						// This could either block the user or start a string
						// for comp
						// If it's an empty space and not occupied by
						// anybody
						if (game.getSpace(r - indx, c - indx) == Space.EMPTY) {
							blanksFound++;
							if (blanksFound == 1)
								blankSpace = new Point(c - indx, r - indx);
							else if (blanksFound == 2) {
								blankSpace2 = new Point(c - indx, r - indx);
								if (size != 4 && game.killShot(type, blankSpace, blankSpace2)) {
									move = blankSpace;
									System.out.println("----KILLSHOT BACKDIAG----");
									return true;
								} else if (size != 4 && game.killShot(type, blankSpace2, blankSpace)) {
									move = blankSpace2;
									System.out.println("----KILLSHOT BACKDIAG----");
									return true;
								}
							}
							if (game.validMove(r - indx, c - indx)) {
								// If we've only seen one in a row, put it
								// at the end
								if (adj >= 2) {
									possibleMoves.push(new Point(c - indx, r - indx));
								} else
									// else we've seen 2 in a row. Put it on
									// the front
									// It's more useful of a move.
									possibleMoves.add(new Point(c - indx, r - indx));
							}
						} else {
							break;
						}

					} // end else
					indx++;
				} // end while
					// If we found size-1 spaces occupied by parameter type
					// We check the end points to see if there's a valid move
				if (adj == size - 1) {
					if (size == 4) {
						if (game.validMove(blankSpace.y, blankSpace.x)) {
							System.out.println("Move Chose By : Diag");
							System.out.println(starting);
							move = new Point(blankSpace.x, blankSpace.y);
							return true;
						}
					}
				}
			} // end for
		} // end for
		return false;
	}

	/**
	 * Method searches for a block against user by looking one step into future
	 * It checks for moves that could set computer up for a "double-kill" One
	 * example of a "double kill" is |E| * | * | * |E| If user finds a way to
	 * achieve this, computer would lose immediately no matter where they try to
	 * block.
	 */
	private boolean searchForBlock() {
		for (int row = HEIGHT - 1; row >= 0; row--) {
			for (int col = WIDTH - 1; col >= 0; col--) {
				if (game.validMove(row, col)) {
					// If we find a double-kill in the future
					// we take that move
					if (game.userCouldWin(row, col)) {
						move = new Point(col, row);
						System.out.println("BLOCK");
						return true;
					}
				}
			}
		}
		return false;
	}
}
