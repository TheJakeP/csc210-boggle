/**
 * 
 * Description: This class represents the Boggle Dice Tray. It can be called to
 * check that a character sequence is valid and available in the gameplay. It
 * does not check those against a dictionary.
 * 
 * @author Jacob Phelps
 * @file DiceTray.java
 *
 *       Date: Jul 29, 2019
 *       Course CSC: 210
 *       Assignment: 34 - Boggle 1
 */

package model;

public class DiceTray {

	private char[][] board;
	private int COLS;
	private String findThis = "";
	private int ROWS;
	boolean wordFound;

	/**
	 * Construct a tray of dice using a hard coded 2D array of chars. Use this
	 * for testing
	 * 
	 * @param newBoard
	 *                 The 2D array of characters used in testing
	 */
	public DiceTray(char[][] newBoard) {
		ROWS = newBoard.length;
		COLS = newBoard[0].length;
		board = newBoard;
		makeArrayCharsUpperCase();

	}

	/**
	 * Converts the board and makes all characters uppercase so that the game is
	 * case insensitive.
	 */
	private void makeArrayCharsUpperCase() {
		char[][] tmp = new char[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				tmp[i][j] = Character.toUpperCase(board[i][j]);
			}
		}
		board = tmp;
	}

	/**
	 * Using recursive backtracking and pruning, this function only visits die
	 * that are making progress towards the search term.
	 * 
	 * @param visited
	 *                The 2D boolean array of which die have been visited on
	 *                this branch
	 * @param i
	 *                The current row position on the board.
	 * @param j
	 *                The current col position on the board
	 * @param str
	 *                The currently built string for this branch
	 */
	private void wordFinder(boolean visited[][], int i, int j, String str) {
		visited[i][j] = true;
		str = str + board[i][j];

		// Prune out invalid options
		if (str.equals(findThis)) {
			wordFound = true;
			return;
		} else if (breakBranch(str)) {
			visited[i][j] = false;
			return;
		}

		// Check neighboring die
		for (int row = i - 1; row <= i + 1 && row < ROWS; row++) {
			for (int col = j - 1; col <= j + 1 && col < COLS; col++) {
				if (row >= 0 && col >= 0) {
					if (!visited[row][col]) {
						wordFinder(visited.clone(), row, col, str);
					}
				}
			}
		}
		// Undo changes
		str = "" + str.charAt(str.length() - 1);
		visited[i][j] = false;
	}

	/**
	 * Description: This function determines if the string that is currently
	 * being built is making progress towards search term. If it is not, it
	 * returns false to prevent that are impossible.
	 * 
	 * @param str
	 * @return boolean
	 */
	private boolean breakBranch(String str) {
		int strLen = str.length();
		int fndLen = findThis.length();
		if (strLen == fndLen) {
			return true;
		} else {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) != findThis.charAt(i)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return true if search is word that can found on the board following the
	 * rules of Boggle
	 * 
	 * @param str
	 *            A word that may be in the board by connecting consecutive
	 *            letters
	 * @return True if search is found
	 */
	public boolean found(String attempt) {
		wordFound = false;
		findThis = attempt.toUpperCase();

		boolean[][] visited = new boolean[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == findThis.charAt(0)) {
					wordFinder(visited, i, j, "");
				}
			}
		}
		return wordFound;
	}

	/**
	 * Returns the Dice Tray as a string of the following format:
	 * A B S E
	 * I M T N
	 * N D E D
	 * S S E N
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (char[] row : board) {
			for (char c : row) {
				str.append("  " + c);
			}
			str.append("\n");
		}
		str.setLength(str.length() - 1);

		return str.toString();
	}

}