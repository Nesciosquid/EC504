package EC504.samegame.Brain;

import java.util.LinkedList;
import java.util.HashSet;

import EC504.samegame.GUI;
import EC504.samegame.CircleColor;

public class SmarterBrain_Aaron extends EC504.samegame.Brain.Brain {
	// fields
	private volatile boolean allDone = false; // when set to true, the Brain
												// should stop what it's doing
												// and exit (at an appropriate
												// time)
	private HashSet<Pos> nodeIndex = new HashSet<Pos>();
	private board currState;
	private int badCounters = 0;
	private int endCounter = 0;

	/**
	 * Instantiates a Brain based on a given GUI sg
	 * 
	 * @param sg
	 *            The GUI that will be instantiating the Brain
	 */
	public SmarterBrain_Aaron(GUI myGUI) {
		super(myGUI);
	}

	public void done() {
		allDone = true;
	}

	public String name() {
		return "Aaron Heuckroth";
	}

	public void run() {
		// Initialize and set up the board with the current position
		currState = new board();

		for (int xx = 0; xx < myGUI.boardWidth(); xx++)
			for (int yy = 0; yy < myGUI.boardHeight(); yy++)
				currState.modify(xx, yy,
						myGUI.colorAt(xx, myGUI.boardHeight() - yy - 1));

		LinkedList<Cluster> d = detectClusters(currState);
		int start = d.size();
		int startMod = 0;
		for (int dd = 0; dd < d.size(); dd++) {
			startMod += d.get(dd).getWidth();
		}
		System.out.println("Starting clusters: " + start + ", modded: "
				+ startMod);

		while (!allDone && !myGUI.gameOverQ()) {
			// Pos nextMove = chooseHeavyMove();
			Pos nextMove = chooseClusterMove();
			if (endCounter % 100 == 0) {
				LinkedList<Cluster> f = detectClusters(currState);
				int cur = f.size();
				int curMod = 0;
				for (int dd = 0; dd < cur; dd++) {
					curMod += d.get(dd).getWidth();
				}
				System.out
						.println("BadCounters: " + badCounters
								+ ", Current Clusters: " + cur
								+ ", End clusters: " + endCounter
								+ ", current modified by width: " + curMod);

			}
			myGUI.makeMove(nextMove.xx, nextMove.yy); // i.e. click on the lower
														// left corner
		}

		System.out.println("BadCounters: " + badCounters + ", Start clusters: "
				+ start + ", End clusters: " + endCounter
				+ ", Start modified by width: " + startMod);
	}

	// internal classes

	/**
	 * Stores an (xx,yy) coordinate
	 */
	public class Pos {
		public int xx, yy;

		public Pos(int xx, int yy) {
			this.xx = xx;
			this.yy = yy;
		}

	}

	private class Node {
		public int xx, yy;
		public boolean checked;
		public CircleColor color;

		public Node(int x, int y, CircleColor col) {
			xx = x;
			yy = y;
			checked = false;
			color = col;
		}

		public boolean isOnTop(SuperBoard b) {
			if (!b.isNode(xx, yy + 1)) {
				// System.out.println("Node ("+xx+", "+yy+") is on top of board.");
				return true;
			} else if (b.getNode(xx, yy + 1).color.equals(color)) {
				// System.out.println("Node above ("+xx+", "+yy+") is same color.");
				return true;
			} else {
				// System.out.println("Node ("+xx+", "+yy+") is not on top!");
				return false;
			}

		}

	}

	private class Cluster {
		public CircleColor myColor;
		public LinkedList<Node> nodes = new LinkedList<Node>();
		public SuperBoard myBoard;
		public HashSet<Node> myIndex;
		public Node root;

		public Cluster(Node n, SuperBoard b) {
			myColor = n.color;
			myBoard = b;
			root = n;
			recursiveAdd(n);
		}

		public Pos rootPos() {
			return new Pos(root.xx, root.yy);
		}

		public boolean containsNode(Node n) {
			return nodes.contains(n);
		}

		public boolean isNotSandwiched(SuperBoard sb) {
			Node top = topMost();
			Node bot = bottomMost();

			if (getWidth() == 1) {

				if (!sb.isNode(top.xx, top.yy + 1)
						|| !sb.isNode(bot.xx, bot.yy - 1)) {
					return true;
				} else if (sb.getNode(top.xx, top.yy + 1).color.equals(sb
						.getNode(bot.xx, bot.yy - 1).color)) {
					return false;
				} else {
					return true;
				}
			} else
				return false;
		}

		public boolean isNotSandwichedRedux(SuperBoard sb) {
			Node top = topMost();
			Node bot = bottomMost();

			if (getWidth() == 1) {

				if (!sb.isNode(top.xx, top.yy + 1)) {
					return true;
				} else if (sb.isNode(bot.xx, bot.yy - 1)
						&& sb.getNode(top.xx, top.yy + 1).color.equals(sb
								.getNode(bot.xx, bot.yy - 1).color)) {
					return false;
				}

				if (sb.isNode(bot.xx + 1, bot.yy)
						&& sb.getNode(bot.xx + 1, bot.yy).color.equals(sb
								.getNode(top.xx, top.yy + 1).color)) {
					return false;
				} else if (sb.isNode(bot.xx - 1, bot.yy)
						&& sb.getNode(bot.xx - 1, bot.yy).color.equals(sb
								.getNode(top.xx, top.yy + 1).color)) {
					return false;
				}

				return true;
			} else
				return false;
		}
		
		public int superScoreCluster(board b){
			board myCopy = new board(b);
			SuperBoard mySuperCopy = new SuperBoard(myCopy);
			LinkedList<Cluster> original = detectClusters(myCopy, this);
			myCopy.clickNode(rootPos().xx, rootPos().yy);
			LinkedList<Cluster> result = detectClusters(myCopy, this);
			return result.size() - original.size();
		}

		public int scoreColumnClusterRedux(board b) {
			int l = leftMost().xx - 1;
			int r = rightMost().xx + 1;

			if (l < 0) {
				l = 0;
			}
			if (r > b.columns() - 1) {
				r = b.columns() - 1;
			}

			board myCopy = new board(b);
			SuperBoard mySuperCopy = new SuperBoard(myCopy);
			int scoreStart = 0;
			int scoreEnd = 0;
			int columnCount = 0;

			for (int i = l; i <= r; i++) {
				LinkedList<Cluster> ori = detectColumnClusters(mySuperCopy, i);
				for (int jj = 0; jj < ori.size(); jj++) {
					Cluster cur = ori.get(jj);
					if (cur.isNotSandwiched(mySuperCopy)) {
						scoreStart++;
					}
				}
				columnCount++;
			}

			myCopy.clickNode(root.xx, root.yy);
			mySuperCopy = new SuperBoard(myCopy);

			if (l < 0) {
				l = 0;
			}
			if (r > myCopy.columns() - 1) {
				r = myCopy.columns() - 1;
			}

			for (int i = l; i <= r; i++) {
				LinkedList<Cluster> ori = detectColumnClusters(mySuperCopy, i);
				for (int jj = 0; jj < ori.size(); jj++) {
					Cluster cur = ori.get(jj);
					if (cur.isNotSandwiched(mySuperCopy)) {
						scoreEnd++;
					}
				}
			}

			int myScore = scoreEnd - scoreStart;
			// System.out.println("Start: " + scoreStart + " New: " + scoreEnd +
			// " Score: " + myScore + ", Columns: " + columnCount + "L: " + l +
			// "R: " + r);
			// ", Score: " + myScore);

			return myScore;
		}

		public int scoreColumnCluster(board b) {
			int l = leftMost().xx - 1;
			int r = rightMost().xx + 1;

			if (l < 0) {
				l = 0;
			}
			if (r > b.columns() - 1) {
				r = b.columns() - 1;
			}

			board myCopy = new board(b);
			SuperBoard mySuperCopy = new SuperBoard(myCopy);
			int scoreStart = 0;
			int scoreEnd = 0;
			int columnCount = 0;

			for (int i = l; i <= r; i++) {
				scoreStart += detectColumnClusters(mySuperCopy, i).size();
				columnCount++;
			}

			myCopy.clickNode(root.xx, root.yy);
			mySuperCopy = new SuperBoard(myCopy);

			if (l < 0) {
				l = 0;
			}
			if (r > myCopy.columns() - 1) {
				r = myCopy.columns() - 1;
			}

			for (int i = l; i <= r; i++) {
				scoreEnd += detectColumnClusters(mySuperCopy, i).size();
			}

			int myScore = scoreEnd - scoreStart;
			// System.out.println("Start: " + scoreStart + " New: " + scoreEnd +
			// " Score: " + myScore + ", Columns: " + columnCount + "L: " + l +
			// "R: " + r);
			// ", Score: " + myScore);

			return myScore;
		}

		public int scoreCluster(board b, int oldCount) {
			board myCopy = new board(b);
			myCopy.clickNode(root.xx, root.yy);
			int newCount = detectClusters(myCopy).size();
			int myScore = newCount - oldCount;
			// System.out.println("Start: " + oldCount + " New: " + newCount +
			// " Score: " + myScore);
			// ", Score: " + myScore);
			return myScore;
		}

		public Node topMost() {
			Node winner = null;
			for (int i = 0; i < nodes.size(); i++) {
				Node current = nodes.get(i);
				if (winner == null) {
					winner = nodes.get(i);
				} else if (winner.yy < current.yy) {
					winner = current;
				}
			}
			return winner;
		}

		public Node bottomMost() {
			Node winner = null;
			for (int i = 0; i < nodes.size(); i++) {
				Node current = nodes.get(i);
				if (winner == null) {
					winner = nodes.get(i);
				} else if (winner.yy > current.yy) {
					winner = current;
				}
			}
			return winner;
		}

		public Node leftMost() {
			Node winner = null;
			for (int i = 0; i < nodes.size(); i++) {
				Node current = nodes.get(i);
				if (winner == null) {
					winner = nodes.get(i);
				} else if (winner.xx > current.xx) {
					winner = current;
				}
			}
			return winner;
		}

		public Node rightMost() {
			Node winner = null;
			for (int i = 0; i < nodes.size(); i++) {
				Node current = nodes.get(i);
				if (winner == null) {
					winner = nodes.get(i);
				} else if (winner.xx < current.xx) {
					winner = current;
				}
			}
			return winner;
		}

		public boolean isOnTop() {
			for (int i = 0; i < nodes.size(); i++) {
				if (!nodes.get(i).isOnTop(myBoard)) {
					// System.out.print("Cluster  not on top!");
					// System.out.println("... Root Node ("+root.xx+", "+root.yy+"), size: "
					// + nodes.size() + ", color: " + myColor);

					return false;
				}
			}
			return true;
		}

		public int getWidth() {
			HashSet<Integer> uniqueColumns = new HashSet<Integer>();
			for (int i = 0; i < nodes.size(); i++) {
				uniqueColumns.add(nodes.get(i).xx);
			}
			return uniqueColumns.size();
		}

		public int getHeight() {
			HashSet<Integer> uniqueRows = new HashSet<Integer>();
			for (int i = 0; i < nodes.size(); i++) {
				uniqueRows.add(nodes.get(i).yy);
			}
			return uniqueRows.size();
		}

		public void add(Node n) {
			nodes.add(n);
		}

		public void destroy(HashSet<Node> set) {
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).checked = false;
				set.remove(nodes.get(i));
			}
		}

		public void recursiveAdd(Node n) {
			if (n.color.equals(myColor) && !n.checked && !containsNode(n)) {
				add(n);
				n.checked = true;

				if (myBoard.isNode(n.xx, n.yy + 1)) {
					Node up = myBoard.getNode(n.xx, n.yy + 1);
					recursiveAdd(up);
				}
				if (myBoard.isNode(n.xx, n.yy - 1)) {
					Node down = myBoard.getNode(n.xx, n.yy - 1);
					recursiveAdd(down);
				}
				if (myBoard.isNode(n.xx + 1, n.yy)) {
					Node right = myBoard.getNode(n.xx + 1, n.yy);
					recursiveAdd(right);
				}
				if (myBoard.isNode(n.xx - 1, n.yy)) {
					Node left = myBoard.getNode(n.xx - 1, n.yy);
					recursiveAdd(left);
				}
			}
		}
	}

	private class SuperBoard {
		LinkedList<LinkedList<Node>> data;
		int width, height;

		// constructs a board of specified width and height
		SuperBoard(int width, int height) {
			this.width = width;
			this.height = height;

			// allocate the data structure
			data = new LinkedList<LinkedList<Node>>();

			// set up the data structure
			for (int ii = 0; ii < width; ii++) {
				LinkedList<Node> temp = new LinkedList<Node>();
				for (int jj = 0; jj < height; jj++)
					temp.add(new Node(ii, jj, CircleColor.NONE));
				data.add(temp);
			}
		}

		SuperBoard(board b) {
			width = b.width;
			height = b.height;

			data = new LinkedList<LinkedList<Node>>();

			// copy over all the specific items
			for (int xx = 0; xx < b.columns(); xx++) {
				data.add(new LinkedList<Node>());
				for (int yy = 0; yy < b.rows(xx); yy++) {
					CircleColor c = b.get(xx, yy);
					Node n = new Node(xx, yy, c);
					data.get(xx).add(n);
				}
			}

		}

		public void removeCluster(Cluster c) {
			for (int ii = 0; ii < c.nodes.size(); ii++) {
				Node n = c.nodes.get(ii);
				for (int jj = 0; jj < data.size(); jj++) {

					if (data.get(jj).contains(n)) {
						data.get(jj).remove(n);
					}

				}
			}
		}

		public boolean isNode(int xx, int yy) {
			if (xx >= 0 && xx < columns() && yy >= 0 && yy < rows(xx)) {
				if (!getNode(xx, yy).color.equals(CircleColor.NONE)) {
					return true;
				} else {
					// System.out.println("IsNode: Node is clear!");
					return false;
				}
			} else {
				// System.out.println("Node index is outside of range!");
				return false;

			}
		}

		public void setNode(int xx, int yy, Node n) {
			data.get(xx).set(yy, n);
		}

		public Node getNode(int xx, int yy) {
			return data.get(xx).get(yy);
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
	 * Chooses the next move to make
	 * 
	 * @return the move chosen, in GUI coordinates
	 */

	private Pos chooseHeavyMove() {
		SuperBoard sb = new SuperBoard(currState);
		LinkedList<Cluster> topClusters = detectTopClusters(currState);
		LinkedList<Cluster> clusters = detectClusters(currState);

		boolean done = false;
		Cluster targetCluster = null;
		int max = -9999;
		Pos bestPos = new Pos(0, 0);

		for (int i = topClusters.size() - 1; i >= 0; i--) {

			Cluster current = topClusters.get(i);
			if (!done) {

				if (current.isOnTop() && current.getWidth() == 1) {
					targetCluster = current;
					done = true;
				}
			}
		}

		if (!done) {
			int l = currState.columns() - 3;
			if (l < 0) {
				l = 0;
			}
			for (int j = currState.columns() - 1; j >= l; j -= 2) {
				LinkedList<Cluster> columnClusters = detectColumnClusters(
						currState, j);
				for (int i = columnClusters.size() - 1; i >= 0; i--) {
					Cluster current = columnClusters.get(i);
					int score = current.scoreColumnCluster(currState);

					if (score > max) {
						targetCluster = current;
						max = score;
					}
				}
			}
		}

		/*
		 * if (!done) { for (int i = clusters.size() - 1; i >= 0; i--) { Cluster
		 * current = clusters.get(i); int score =
		 * current.scoreColumnCluster(currState, clusters.size());
		 * 
		 * if (score > max) { targetCluster = current; max = score; } } }
		 */

		if (targetCluster != null) {
			if (!targetCluster.isOnTop()) {
				badCounters++;
			}
			bestPos.xx = targetCluster.rootPos().xx;
			bestPos.yy = targetCluster.rootPos().yy;
			// sb.removeCluster(targetCluster);
			endCounter++;

		}

		System.out.println("Move number: " + endCounter + ", Move Score: "
				+ max + ", Number of clusters: " + clusters.size());

		// System.out.println("Picked move: " + bestPos.xx + ", " + bestPos.yy +
		// ", " + currState.get(bestPos.xx, bestPos.yy));
		// register the selected move on the board
		currState.clickNode(bestPos.xx, bestPos.yy);

		// convert bestPos to GUI coordinates
		bestPos.yy = myGUI.boardHeight() - 1 - bestPos.yy;

		// return the result to the GUI
		return bestPos;
	}

	private Pos chooseClusterMove() {
		SuperBoard sb = new SuperBoard(currState);
		LinkedList<Cluster> tops = detectTopClusters(currState);
		boolean done = false;
		Cluster targetCluster = null;
		int min = -1;
		Pos bestPos = new Pos(0, 0);

		for (int i = tops.size() - 1; i >= 0; i--) {

			Cluster current = tops.get(i);
			if (!done) {

				if (current.isOnTop()) {
					if (current.getWidth() == 1) {

						targetCluster = current;
						done = true;
					} else if (min == -1 || current.getWidth() < min) {
						targetCluster = current;
					}
				}
			}
		}

		int end = 0; // change to alter distance from end to search for good clusters
		if (end < 0) {
			end = 0;
		}

		if (!done) {
			for (int i = 1; i < sb.columns(); i++) {
				if (!done) {
					if (i % 2 == 1) {
						LinkedList<Cluster> column = detectColumnClusters(
								currState, i);
						// if (i % 4 == ) {
						for (int j = column.size() - 1; j >= 0; j--) {
							Cluster current = column.get(j);

							if (current.getWidth() == 1
									&& current.isNotSandwichedRedux(sb)) {
								targetCluster = current;
								done = true;
							}

							/*
							 * } } else { for (int j = 0; j < column.size();
							 * j++) { if (!done) { Cluster current =
							 * column.get(j);
							 * 
							 * if (current.getWidth() == 1 &&
							 * current.isNotSandwiched(sb)) { targetCluster =
							 * current; done = true; }
							 * 
							 * } }
							 */

						}
					}
				}
			}
			
			for (int i = 0; i < sb.columns(); i++) {
				if (!done) {
					if (i % 2 == 0) {
						LinkedList<Cluster> column = detectColumnClusters(
								currState, i);
						for (int j = column.size() - 1; j >= 0; j--) {
							if (!done) {
								Cluster current = column.get(j);

								if (current.getWidth() == 1
										&& current.isNotSandwichedRedux(sb)) {
									targetCluster = current;
									done = true;
								}
							}
						}
					}
				}
			}
		}
		
		/*
		
		if (!done) {
			for (int i = 1; i < sb.columns(); i++) {
				if (!done) {
					if (i % 2 == 1) {
						LinkedList<Cluster> column = detectColumnClusters(
								currState, i);
						// if (i % 4 == ) {
						for (int j = column.size() - 1; j >= 0; j--) {
							Cluster current = column.get(j);

							if (current.getWidth() == 1
									&& current.isNotSandwiched(sb)) {
								targetCluster = current;
								done = true;
							}

						}
					}
				}
			}
			
			for (int i = 0; i < sb.columns(); i++) {
				if (!done) {
					if (i % 2 == 0) {
						LinkedList<Cluster> column = detectColumnClusters(
								currState, i);
						for (int j = column.size() - 1; j >= 0; j--) {
							if (!done) {
								Cluster current = column.get(j);

								if (current.getWidth() == 1
										&& current.isNotSandwiched(sb)) {
									targetCluster = current;
									done = true;
								}
							}
						}
					}
				}
			}
		}
		
		*/

		if (!done) {
			min = -999;
			for (int i = end; i < sb.columns(); i++) {
				LinkedList<Cluster> remainingClusters = detectColumnClusters(
						currState, i);
				for (int j = 0; j < remainingClusters.size(); j++) {
					if (!done) {
						Cluster current = remainingClusters.get(j);
						int currentScore = current
								.superScoreCluster(currState);

						/*
						 * if (currentScore >= 1) { done = true; targetCluster =
						 * current; }
						 */

						if (currentScore > min) {
							targetCluster = current;
							min = current.getWidth();
						}

						/*
						 * if (min == -1 || current.getWidth() < min) {
						 * targetCluster = current; min = current.getWidth(); }
						 */

					}
				}
			}
		}

		if (targetCluster != null) {
			if (!targetCluster.isOnTop()) {
				badCounters++;
			}
			bestPos.xx = targetCluster.rootPos().xx;
			bestPos.yy = targetCluster.rootPos().yy;
			// sb.removeCluster(targetCluster);
			endCounter++;

		}

		// System.out.println("Picked move: " + bestPos.xx + ", " + bestPos.yy +
		// ", " + currState.get(bestPos.xx, bestPos.yy));
		// register the selected move on the board
		currState.clickNode(bestPos.xx, bestPos.yy);

		// convert bestPos to GUI coordinates
		bestPos.yy = myGUI.boardHeight() - 1 - bestPos.yy;

		// return the result to the GUI
		return bestPos;

	}

	private Pos chooseNewMove() {
		// greedy choice
		int min = -1; // the maximum number of points for the best position
						// found (dummy value)
		Pos bestPos = new Pos(0, 0); // the best position found
		board currStateCopy = new board(currState);
		boolean done = false;

		for (int yy = 0; yy < myGUI.boardHeight(); yy++) {
			if (!done) {
				for (int xx = currState.columns() - 1; xx >= 0; xx--) {
					if (!done) {
						if (yy < currState.rows(xx)) {
							if (currStateCopy.get(xx, yy) != CircleColor.NONE) {
								board test = new board(currStateCopy);
								currStateCopy.clickNodeHelper(xx, yy,
										test.get(xx, yy));
								int count = test.clickNode(xx, yy);

								if (count == 1) {
									bestPos = new Pos(xx, yy);
									xx = -1;
									yy = myGUI.boardHeight();
									done = true;
								} else if (count < min || min == -1) {
									min = count;
									bestPos = new Pos(xx, yy);
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

	// internal methods
	/**
	 * Chooses the next move to make
	 * 
	 * @return the move chosen, in GUI coordinates
	 */
	
	private LinkedList<Cluster> detectClusters(board b, int minX, int maxX, int minY) {
		SuperBoard sBoard = new SuperBoard(b);
		LinkedList<Cluster> allClusters = new LinkedList<Cluster>();
		if (minX < 0){
			minX = 0;
		}
		if (minY <0){
			minY = 0;
		}
		
		for (int xx = minX; xx < maxX && xx < sBoard.columns(); xx++) {
			for (int yy = minY; yy < sBoard.rows(xx); yy++) {
				if (sBoard.isNode(xx, yy) && !sBoard.getNode(xx, yy).checked) {
					Cluster c = new Cluster(sBoard.getNode(xx, yy), sBoard);
					allClusters.add(c);
					// System.out.println("New cluster, width: " + c.getWidth()
					// + ", height: " +c.getHeight());
				}
			}
		}

		// System.out.println("Clusters found: " + allClusters.size());
		return allClusters;
	}
	
	private LinkedList<Cluster> detectClusters(board b, Cluster rootCluster) {
		return detectClusters(b, rootCluster.leftMost().xx-1, rootCluster.rightMost().xx+1, rootCluster.bottomMost().yy-1);
		}

	private LinkedList<Cluster> detectClusters(board b) {
		SuperBoard sBoard = new SuperBoard(b);
		LinkedList<Cluster> allClusters = new LinkedList<Cluster>();
		for (int xx = 0; xx < sBoard.columns(); xx++) {
			for (int yy = 0; yy < sBoard.rows(xx); yy++) {
				if (sBoard.isNode(xx, yy) && !sBoard.getNode(xx, yy).checked) {
					Cluster c = new Cluster(sBoard.getNode(xx, yy), sBoard);
					allClusters.add(c);
					// System.out.println("New cluster, width: " + c.getWidth()
					// + ", height: " +c.getHeight());
				}
			}
		}

		// System.out.println("Clusters found: " + allClusters.size());
		return allClusters;
	}

	private LinkedList<Cluster> detectColumnClusters(board b, int column) {
		SuperBoard sBoard = new SuperBoard(b);
		LinkedList<Cluster> columnClusters = new LinkedList<Cluster>();
		for (int yy = 0; yy < sBoard.rows(column); yy++) {
			if (sBoard.isNode(column, yy)) {
				if (!sBoard.getNode(column, yy).checked) {
					Cluster c = new Cluster(sBoard.getNode(column, yy), sBoard);
					columnClusters.add(c);
				}
			}
		}
		return columnClusters;
	}

	private LinkedList<Cluster> detectColumnClusters(SuperBoard sBoard,
			int column) {
		LinkedList<Cluster> columnClusters = new LinkedList<Cluster>();
		for (int yy = 0; yy < sBoard.rows(column); yy++) {
			if (sBoard.isNode(column, yy)) {
				if (!sBoard.getNode(column, yy).checked) {
					Cluster c = new Cluster(sBoard.getNode(column, yy), sBoard);
					columnClusters.add(c);
				}
			}
		}
		return columnClusters;
	}

	private LinkedList<Cluster> detectTopClusters(board b) {
		SuperBoard sBoard = new SuperBoard(b);
		LinkedList<Cluster> allClusters = new LinkedList<Cluster>();
		for (int xx = 0; xx < sBoard.columns(); xx++) {
			int row = sBoard.rows(xx) - 1;
			// System.out.println("Checking for node at: " + xx + ", " + row);
			if (sBoard.isNode(xx, row)) {
				if (!sBoard.getNode(xx, sBoard.rows(xx) - 1).checked) {

					Cluster c = new Cluster(sBoard.getNode(xx,
							sBoard.rows(xx) - 1), sBoard);
					allClusters.add(c);
					// System.out.println("New cluster, width: " + c.getWidth()
					// + ", height: " +c.getHeight());
				}

				// System.out.println("Top node is already checked!");
			}
			// System.out.println("Top node is not a node!");
		}

		// System.out.println("Clusters found on top only: " +
		// allClusters.size());
		return allClusters;
	}

	private Pos chooseMove() {
		// greedy choice
		int max = 0; // the maximum number of points for the best position found
		Pos bestPos = new Pos(0, 0); // the best position found
		board currStateCopy = new board(currState);

		for (int xx = 0; xx < currState.columns(); xx++)
			for (int yy = 0; yy < currState.rows(xx); yy++) {
				if (currStateCopy.get(xx, yy) != CircleColor.NONE) {
					board test = new board(currStateCopy);
					currStateCopy.clickNodeHelper(xx, yy, test.get(xx, yy)); // mark
																				// all
																				// other
																				// nodes
																				// in
																				// the
																				// region
																				// as
																				// "clear"
																				// (but
																				// does
																				// not
																				// delete
																				// anything)
					int count = test.clickNode(xx, yy); // try removing the
														// region to see what is
														// left over
					if (count > max) {
						// record a new best move
						max = count;
						bestPos = new Pos(xx, yy);
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
