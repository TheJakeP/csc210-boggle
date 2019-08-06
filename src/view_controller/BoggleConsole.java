/**
 * 
 * Description: This class references Boggle.java and interprets the results so
 * that the game can be used in a command line scenario.
 * 
 * @author Jacob Phelps
 * @file Boggle.java
 *
 *       Course CSC: 210
 *       Assignment: Boggle Two
 */
package view_controller;

import java.util.Scanner;
import java.util.TreeSet;

import model.Boggle;
import model.DiceTray;

public class BoggleConsole {

	private static Boggle boggle;
	private static DiceTray tray;

	public static void main(String[] args) {
		// Use this for testing
		char[][] a = { { 'A', 'B', 'S', 'E' }, { 'I', 'M', 'T', 'N' },
				{ 'N', 'D', 'E', 'D' }, { 'S', 'S', 'E', 'N' }, };

		tray = new DiceTray(a);
		boggle = new Boggle(tray);

		setupGame();
		String input = getInput();
		boggle.gradeUserAnswers(input);
		gameConclusion();
	}

	/**
	 * Description: setupGame() prints the start of game prompts including the
	 * dice tray.
	 */
	private static void setupGame() {
		System.out.println("Play one game of Boggle:\n");
		System.out.println(tray + "\n");
		System.out.println("Enter words or ZZ then enter to quit:\n");
	}

	/**
	 * Description: getInput() collects console input from the player and adds
	 * words to the userInput dictionary
	 * 
	 * @return trimmed string of all user input as lowercase input
	 */
	private static String getInput() {
		Scanner scanner = new Scanner(System.in);
		StringBuilder userInput = new StringBuilder();
		String word;
		while (scanner.hasNext()) {
			word = scanner.next();
			if (word.equals("ZZ")) {
				break;
			} else {
				userInput.append(" " + word);
			}
		}
		scanner.close();
		return userInput.toString();
	}

	/**
	 * Description: gameConclusion() prints the results of the round.
	 */
	private static void gameConclusion() {
		TreeSet<String> wordSet = boggle.getCorrectGuesses();
		String resultTitle = "\nWords you found:";

		System.out.println("\nScore: " + boggle.getScore());
		printOutput(resultTitle, wordSet);

		wordSet = boggle.getIncorrectGuesses();
		resultTitle = "\nIncorrect words:";
		printOutput(resultTitle, wordSet);

		wordSet = boggle.getMissedWords();
		resultTitle = "\nYou could have found these " + wordSet.size()
				+ " more words:";
		printOutput(resultTitle, wordSet);
	}

	/**
	 * Description: printOutput() is a helper function for gameConclusion. It
	 * prints the prompt and a TreeSet<String> set of words.
	 * 
	 * @param prompt
	 *               A string title to be printed before the list of words
	 * @param set
	 *               A TreeSet<String> containing the words to be printed.
	 */
	private static void printOutput(String prompt, TreeSet<String> set) {
		System.out.println(prompt);
		StringBuilder str = new StringBuilder();
		for (String word : set) {
			str.append(" " + word);
		}
		System.out.println(str.toString().trim());
	}

}
