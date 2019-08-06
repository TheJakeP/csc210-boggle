/**
 * 
 * Description: This class is the actual boggle game. It computes the input
 * words, uses the DiceTray to verify the user input and uses the dictionary to
 * return a list of words the user missed.
 * 
 * @author Jacob Phelps
 * @file Boggle.java
 *
 *       Course CSC: 210
 *       Assignment: Boggle Two
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

public class Boggle {

	private TreeSet<String> answerWords = new TreeSet<String>();
	private TreeSet<String> dictionary = new TreeSet<String>();
	private TreeSet<String> correctGuesses = new TreeSet<String>();
	private TreeSet<String> incorrect = new TreeSet<String>();
	private TreeSet<String> missedWords = new TreeSet<String>();
	private DiceTray tray;

	public Boggle() {
		createDictionary();
	}

	/**
	 * Constructor
	 * Description: This constructor takes the parameter DiceTray.
	 * 
	 * @param tray
	 */
	public Boggle(DiceTray t) {
		createDictionary();
		tray = t;
		findAllAnswers();
	}

	/**
	 * Description: Finds all answers in the dice tray based upon the answers
	 * that are in the dictionary. It adds all answers to the answerWords class
	 * variable
	 */
	private void findAllAnswers() {
		for (String word : dictionary) {
			if (tray.found(word)) {
				answerWords.add(word.toLowerCase());
			}
		}
	}

	/**
	 * Description: Reads the dictionary file and adds words to the dictionary
	 * class variable.
	 */
	private void createDictionary() {
		File dict = new File("BoggleWords.txt");
		String word;
		try {
			Scanner dictFile = new Scanner(dict);

			while (dictFile.hasNext()) {
				word = dictFile.next().toLowerCase();
				dictionary.add(word);
			}
			dictFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("WORDS FILE NOT FOUND");
		}

	}

	/**
	 * Getter
	 * Description: This getter function takes the user's input as a string,
	 * creates a scanner instance, and returns a set containing all of the
	 * user's correct answers.
	 * 
	 * @return TreeSet<String>
	 *         Containing all valid answers the user
	 */
	public TreeSet<String> gradeUserAnswers(String input) {
		Scanner scanner = new Scanner(input);
		return checkInput(scanner);
	}

	/**
	 * Description: Checks the user's input against the dictionary
	 * 
	 * @param input
	 *              A scanner class object that can be parsed.
	 */
	public TreeSet<String> checkInput(Scanner input) {
		String word;

		while (input.hasNext()) {
			word = input.next().toLowerCase();
			if (answerWords.contains(word)) {
				correctGuesses.add(word);
			} else {
				incorrect.add(word);
			}
		}
		input.close();
		return correctGuesses;
	}

	/**
	 * Description: Scores each correct word per the grading criterian
	 */
	public int getScore() {
		int score = 0;
		int wordLen;
		for (String word : correctGuesses) {
			wordLen = word.length();

			if (wordLen == 3) {
				score += 1;
			} else if (wordLen == 4) {
				score += 1;
			} else if (wordLen == 5) {
				score += 2;
			} else if (wordLen == 6) {
				score += 3;
			} else if (wordLen == 7) {
				score += 4;
			} else if (wordLen >= 8) {
				score += 11;
			}
		}
		return score;
	}

	/**
	 * Getter
	 * Description: This getter function returns the user's correct guesses.
	 * 
	 * @return
	 *         TreeSet with all the correct guesses.
	 */
	public TreeSet<String> getCorrectGuesses() {
		return correctGuesses;
	}

	/**
	 * Getter
	 * Description: This getter function returns the user's incorrect guesses.
	 * 
	 * @return
	 *         TreeSet with all the incorrect guesses.
	 */
	public TreeSet<String> getIncorrectGuesses() {
		return incorrect;
	}

	/**
	 * Getter
	 * Description: This getter function returns the words the user missed.
	 * 
	 * @return
	 *         TreeSet with all the missed guesses.
	 */
	public TreeSet<String> getMissedWords() {
		for (String word : answerWords) {
			if (!correctGuesses.contains(word)) {
				missedWords.add(word);
			}

		}
		return missedWords;
	}

}