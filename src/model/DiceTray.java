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

import java.util.LinkedList;
import java.util.Random;

public class DiceTray {

	private Dice[][] diceBoard;
	private int COLS = 4;
	private String findThis;
	private int ROWS = 4;
	boolean wordFound;
	private Random random = new Random();
	private LinkedList<Dice> dieBag = null;

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
		diceBoard = charArrayToDiceArray(newBoard);
	}

	/**
	 * Constructor
	 * Description: This constructor will create a random game board.
	 */
	public DiceTray() {
		createDieBag();
		randomizeBoard();
	}

	/**
	 * 
	 * Description: This turns the character array into an array of arrays of
	 * dice.
	 * 
	 * @param charArray
	 *                  A char[][] representing the game board
	 * @return
	 *         Dice[][] representing each die on the playing field.
	 */
	private Dice[][] charArrayToDiceArray(char[][] charArray) {
		Dice[][] diceArray = new Dice[ROWS][COLS];
		for (int i = 0; i < charArray.length; i++) {
			for (int j = 0; j < charArray[0].length; j++) {
				diceArray[i][j] = new Dice(charArray[i][j]);
			}
		}
		return diceArray;
	}

	/**
	 * 
	 * Description: This takes the sides of each boggle die and turns it into a
	 * Dice object and places it in a linkedList/bag.
	 */
	private void createDieBag() {
		final String[][] dieList = { { "L", "R", "Y", "T", "T", "E" },
				{ "V", "T", "H", "R", "W", "E" },
				{ "E", "G", "H", "W", "N", "E" },
				{ "S", "E", "O", "T", "I", "S" },
				{ "A", "N", "A", "E", "E", "G" },
				{ "I", "D", "S", "Y", "T", "T" },
				{ "O", "A", "T", "T", "O", "W" },
				{ "M", "T", "O", "I", "C", "U" },
				{ "A", "F", "P", "K", "F", "S" },
				{ "X", "L", "D", "E", "R", "I" },
				{ "H", "C", "P", "O", "A", "S" },
				{ "E", "N", "S", "I", "E", "U" },
				{ "Y", "L", "D", "E", "V", "R" },
				{ "Z", "N", "R", "N", "H", "L" },
				{ "N", "M", "I", "H", "U", "Qu" },
				{ "O", "B", "B", "A", "O", "J" } };

		dieBag = new LinkedList<Dice>();
		for (String[] diceSides : dieList) {
			dieBag.add(new Dice(diceSides));
		}
	}

	/**
	 * 
	 * Description: Randomly select a dice from the dieBag and place it on the
	 * diceBoard.
	 */
	private void randomizeBoard() {
		int r;
		Dice d;
		diceBoard = new Dice[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				r = random.nextInt(dieBag.size());
				d = dieBag.remove(r);
				diceBoard[i][j] = d;
			}
		}
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
		str = str + diceBoard[i][j];

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
		attempt = attempt.replaceAll("QU", "Q");
		boolean[][] visited = new boolean[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (diceBoard[i][j].asChar() == findThis.charAt(0)) {
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
		for (Dice[] row : diceBoard) {
			for (Dice die : row) {
				str.append("  " + die);
			}
			str.append("\n");
		}
		str.setLength(str.length() - 1);

		return str.toString();
	}

	/**
	 * Description: This class models each individual dice in the tray.
	 */
	class Dice {
		private String[] sides;
		private int side;

		/**
		 * Constructor
		 * Description: When passed a String[], it stores the possible sides of
		 * the die and sets the side that is visible to the players.
		 * 
		 * 
		 * @param str
		 */
		Dice(String[] str) {
			sides = str;
			rollDie();
		}

		/**
		 * Constructor
		 * Description: When passed a character, this will create a dice. This
		 * is used for testing when DiceTray is passed a static char[][]
		 * 
		 * @param c
		 */
		Dice(char c) {
			String s = c + "";
			sides = new String[1];
			sides[0] = s.toUpperCase();
			side = 0;
		}

		/**
		 * 
		 * Description: Randomly select a number between 0 and sides.length,
		 * excluding the last number. This simulates the roll of a dice.
		 */
		void rollDie() {
			side = random.nextInt(sides.length);
		}

		/**
		 * Description: Returns the currently showing side of the die.
		 */
		public String toString() {
			return sides[side];
		}

		/**
		 * 
		 * Description: Used for comparison, this provides the showing character
		 * as a char value.
		 * 
		 * @return
		 */
		public char asChar() {
			return sides[side].charAt(0);
		}
	}

	/**
	 * Getter
	 * Description: Creates a String[][] from the Dice[][]
	 * 
	 * @return
	 *         String[][] to be used when creating things such as the GUI dice
	 *         tray
	 */
	public String[][] getStringArray() {
		String[][] arr = new String[diceBoard.length][diceBoard[0].length];
		for (int i = 0; i < diceBoard.length; i++) {
			for (int j = 0; j < diceBoard[0].length; j++) {
				arr[i][j] = diceBoard[i][j].toString();
			}
		}
		return arr;
	}

}