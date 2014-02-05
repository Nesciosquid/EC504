package EC504.samegame.Brain;

import java.util.LinkedList;

import EC504.samegame.GUI;
import EC504.samegame.CircleColor;

/**
 * A brain for Ari Trachtenberg's Samegame based on a greedy algorithm that
 * selects the smallest group of circles possible.
 * 
 * Optimized to run from the bottom right to the top left, with bottom taking
 * priority. Since no clusters can have a size smaller than 1, any cluster with
 * count == 1 is taken immedaitely, which enforces the bottom-right priority and
 * speeds up computation.
 * 
 * This was adapted from Ari Trachtenberg's "Lazy Brain" example, and will
 * obviously contain some of his code and comments.
 * 
 * @author Aaron Heuckroth
 * 
 */
public class GreedyBrain extends EC504.samegame.Brain.Brain {
	// fields
	private volatile boolean allDone = false; // when set to true, the Brain
												// should stop what it's doing
												// and exit (at an appropriate
												// time)
	private board currState;

	public GreedyBrain(GUI myGUI) {
		super(myGUI);
	}

	public void done() {
		allDone = true;
	}

	public String name() {
		return "Aaron's Greedy Brain";
	}

	public void run() {
		// Initialize and set up the board with the current position
		currState = new board();
		for (int xx = 0; xx < myGUI.boardWidth(); xx++)
			for (int yy = 0; yy < myGUI.boardHeight(); yy++)
				currState.modify(xx, yy,
						myGUI.colorAt(xx, myGUI.boardHeight() - yy - 1));

		while (!allDone && !myGUI.gameOverQ()) {
			pos nextMove = chooseMove(); // picks smallest group.
			myGUI.makeMove(nextMove.xx, nextMove.yy); // sends choice to GUI
		}
	}

	// internal classes

	/**
	 * Stores an (xx,yy) coordinate
	 */
	public class pos {
		public int xx, yy;

		public pos(int xx, int yy) {
			this.xx = xx;
			this.yy = yy;
		}
	}

	/**
	 * Stores a board set up
	 */
	private class board {
		LinkedList<LinkedList<CircleColor>> data;
		final private int width, height;

		// constructs a board of specified width and height
		board(int width, int height) {
			this.width = width;
			this.height = height;

			// allocate the data structure
			data = new LinkedList<LinkedList<CircleColor>>();

			// set up the data structure
			for (int ii = 0; ii < width; ii++) {
				LinkedList<CircleColor> temp = new LinkedList<CircleColor>();
				for (int jj = 0; jj < height; jj++)
					temp.add(CircleColor.NONE);
				data.add(temp);
			}
		}

		/**
		 * default constructor
		 */
		board() {
			this(myGUI.boardWidth(), myGUI.boardHeight());
		}

		/**
		 * copy constructor
		 * 
		 * @param basis
		 *            the board to copy
		 */
		board(board basis) {
			// allocate space
			this(basis.width, basis.height);

			// copy over all the specific items
			for (int xx = 0; xx < columns(); xx++)
				for (int yy = 0; yy < rows(xx); yy++)
					modify(xx, yy, basis.get(xx, yy));
		}

		/**
		 * @return the color of the yy'th item in the xx'th column, or
		 *         CircleColor.NONE if the cell is not in the <b>data</b>
		 *         container
		 */
		private CircleColor get(int xx, int yy) {
			try {
				return data.get(xx).get(yy);
			} catch (IndexOutOfBoundsException e) {
				return CircleColor.NONE;
			}
		}

		/**
		 * @return the xx'th column, or null if the xx'th column does not exist
		 *         in the <b>data</b> container
		 */
		private LinkedList<CircleColor> get(int xx) {
			try {
				return data.get(xx);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}

		/**
		 * Changes yy'th item in xx'th column %R: The circle at this location
		 * must still be in the <b>data</b> field
		 */
		private void modify(int xx, int yy, CircleColor col) {
			data.get(xx).set(yy, col);
		}

		/**
		 * Deletes yy'th item in xx'th column %R: The circle at this location
		 * must still be in the <b>data</b> field
		 */
		private void delete(int xx, int yy) {
			data.get(xx).remove(yy);
		}

		/**
		 * Deletes the column at (horizontal) board location xx * %R: The column
		 * at this location must still be in the <b>data</b> field
		 */
		private void delete(int xx) {
			data.remove(xx);
		}

		// public accessors
		/**
		 * Simulates a click on the yy'th cell of the xx'th column
		 * 
		 * @return the number of cells deleted
		 * @modifies Deletes cells in the same region as cell (xx,yy)
		 * @expects The clicked cell is assumed not to be empty
		 */
		public int clickNode(int xx, int yy) {
			CircleColor myColor = get(xx, yy); // the color to match

			// mark the region
			int count = clickNodeHelper(xx, yy, myColor);

			// clean up
			// ... delete all cells with no color
			// ... delete from the end to the front so that there are no
			// problems with re-indexing
			for (int ii = data.size() - 1; ii >= 0; ii--) {
				for (int jj = get(ii).size() - 1; jj >= 0; jj--)
					if (get(ii, jj) == CircleColor.NONE)
						delete(ii, jj);

				// ... delete the column if it is empty
				if (get(ii).size() == 0)
					delete(ii);
			}

			return count;
		}

		/**
		 * Recursive procedure for propagating a click at a location with color
		 * "col". All items in the same region as the clicked cell are made to
		 * have CircleColor.NONE
		 * 
		 * @modifies the color of some cells, but no cells are actually deleted
		 * @return the number of cells changed to CircleColor.NONE in this call
		 *         (and its recursive subcalls)
		 */
		private int clickNodeHelper(int xx, int yy, CircleColor col) {
			if (get(xx, yy) == CircleColor.NONE || // we've either already seen
													// this, or we've hit an
													// empty space
					get(xx, yy) != col) // this is not the color we want
				return 0;

			// modify the current cell
			modify(xx, yy, CircleColor.NONE);

			return 1 + // 1 is for the current cell
					clickNodeHelper(xx - 1, yy, col) + // cell to the left
					clickNodeHelper(xx + 1, yy, col) + // cell to the right
					clickNodeHelper(xx, yy - 1, col) + // cell below
					clickNodeHelper(xx, yy + 1, col) // cell above
			;
		}

		/**
		 * @return the number of columns in the current state
		 */
		public int columns() {
			return data.size();
		}

		/**
		 * 
		 * @param xx
		 *            the column being considered (assumed to exist)
		 * @return the number of rows in column <b>xx</b>
		 */
		public int rows(int xx) {
			return data.get(xx).size();
		}

		/**
		 * @return a "pretty-printed" version of the data structure
		 */
		public String display() {
			String temp = data.toString();
			return temp.replace("], [", "]\n[");
		}
	}

	// internal methods
	/**
	 * Chooses the next move to make.
	 * 
	 * In this case, it iterates through each row, starting from the bottom, and
	 * each column, starting from the right, looking for the smallest move
	 * possible. Ties for the smallest move do not overwrite each other, such
	 * that the 'winner' of a tie will be the lowest, then rightmost, group.
	 * 
	 * Note that LOW takes priority over RIGHT, for this algorithm, as defined
	 * by Prof. Trachtenberg on the Twiki EC544 Homework One Forum.
	 * 
	 * @return the move chosen, in GUI coordinates
	 */
	private pos chooseMove() {
		int min = -1; // the minimum number of points for the best position
						// (i.e., smallest group)
						// (dummy value, reset on first comparison)
		pos bestPos = new pos(0, 0); // the best position found
		board currStateCopy = new board(currState);
		boolean done = false;

		/*
		 * iterate from the bottom of the board to the top
		 */
		for (int yy = 0; yy < myGUI.boardHeight(); yy++) {

			/*
			 * allows you to 'skip to the end', speeds up calculation for the
			 * first round, where the smallest clusters all have size == 0
			 */
			if (!done) {

				/*
				 * iterate from the right of the board to the left
				 */
				for (int xx = currState.columns() - 1; xx >= 0; xx--) {

					/*
					 * skip calculation within a row if an ideal match is found
					 * first
					 */
					if (!done) {

						/*
						 * Only check for moves if the current yy value is less
						 * than the total number of rows of the given column.
						 * Skipping this step would easily lead to
						 * IndexOutOfBoundsErrors!
						 */
						if (yy < currState.rows(xx)) {
							// test the size of the group at the current
							// location if the node is not already checked
							if (currStateCopy.get(xx, yy) != CircleColor.NONE) {
								board test = new board(currStateCopy);
								currStateCopy.clickNodeHelper(xx, yy,
										test.get(xx, yy));
								int count = test.clickNode(xx, yy);

								// if the size is minimal, skip to the end and
								// take this move
								if (count == 1) {
									bestPos = new pos(xx, yy);
									xx = -1;
									yy = myGUI.boardHeight();
									done = true;

									/*
									 * if the size is less than the previous
									 * minimum size, or this is the first group,
									 * set this to the current target position
									 */
								} else if (count < min || min == -1) {
									min = count;
									bestPos = new pos(xx, yy);
								}
							}
						}
					}
				}
			}
		}

		// register the selected move on the board
		currState.clickNode(bestPos.xx, bestPos.yy);

		// convert bestPos to GUI coordinates
		bestPos.yy = myGUI.boardHeight() - 1 - bestPos.yy;

		// return the result to the GUI
		return bestPos;
	}

}
