package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class EvilHangmanGame implements IEvilHangmanGame {
	private Set<String> words = new HashSet<String>();
	private ArrayList<String> guesses = new ArrayList<String>();
	private Map<String,HashSet<String>> partitions = new TreeMap<String,HashSet<String>>();
	private String currentWord = "";
	private int letters = 0; 

	public void startGame(File dictionary, int wordLength)  {
		for (int i = 0; i < wordLength; i++) currentWord += "-";
		try {
			Scanner scan = new Scanner(dictionary);
			while (scan.hasNext()) {
				String word = scan.next();
				if (word.length() == wordLength) words.add(word);
			}
			
			scan.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException{
		partitions.clear();
		letters = 0;
		String newGuess = "" + guess;
		if (guesses.contains(newGuess)) {
			throw new GuessAlreadyMadeException();
		}
		
		partitionWords(guess);
		words = findLargestPartition(guess);
		
		return words;
	}
	
	public void partitionWords(char guess) {
		// Partition words based on the letter guessed
		for (String word : words) {
			String pattern = generatePattern(word,guess);
			if (partitions.containsKey(pattern)) {
				addToSet(pattern,word);
			}
			else {
				HashSet<String> myset = new HashSet<String>();
				myset.add(word);
				partitions.put(pattern, myset);
			}
		}
	}
	
	public Set<String> findLargestPartition(char guess) {
		// Choose the biggest partition and use that set of words
		HashSet<String> biggestSet = null;
		
		String newWord = "";
		for (Map.Entry<String, HashSet<String>> entry : partitions.entrySet()) {
			if (biggestSet == null || entry.getValue().size() > biggestSet.size()) {
				biggestSet = entry.getValue();
				letters = charCounter(entry.getKey(),guess);
				newWord = entry.getKey();
			}
			
			else if (entry.getValue().size() == biggestSet.size()) {
				if (letters > charCounter(entry.getKey(),guess)){
					biggestSet = entry.getValue();
					letters = charCounter(entry.getKey(),guess);
					newWord = entry.getKey();
				}
				else if (letters == charCounter(entry.getKey(),guess)) {
					if(newWord.indexOf(guess) < entry.getKey().indexOf(guess)) {
						biggestSet = entry.getValue();
						letters = charCounter(entry.getKey(),guess);
						newWord = entry.getKey();
					}
				}
			}
		}
		
		char[] currChars = currentWord.toCharArray();
		for (int i = 0; i < currentWord.length(); i++) {
			if (newWord.charAt(i) != '-') {
				currChars[i] = newWord.charAt(i);
			}
		}
		currentWord = String.valueOf(currChars);
		return biggestSet;
	}
	
	public int charCounter(String word, char guess) {
		int counter = 0;
		for( int i=0; i<word.length(); i++ ) {
		    if( word.charAt(i) == guess) {
		        counter++;
		    } 
		}
		return counter;
	}
	
	public void addToSet(String pattern, String word) {
		for (Map.Entry<String, HashSet<String>> entry : partitions.entrySet()) {
			if (entry.getKey().equals(pattern)) {
				entry.getValue().add(word);
			}
		}
	}
	
	public String generatePattern(String word, char guess) {
		String pattern = "";
	    for (int i = 0; i < word.length(); i++) {
	        if (word.charAt(i) == guess) {
	            pattern += guess;
	        }
	        else {
	        		pattern += '-';
	        }
	    }
	    return pattern;
	}
	
	public ArrayList<String> getGuesses() {
		Collections.sort(guesses);
		return guesses;
	}
	
	public void addGuess(char myGuess) {
		String newGuess = "" + myGuess;
		guesses.add(newGuess);
		Collections.sort(guesses);
	}
	
	public String getCurrentWord() {
		return currentWord;
	}

	public int getLetters() {
		return letters;
	}
	
	public String getWord() {
		String firstElement = "";
		if ( ! words.isEmpty() ) {
		    firstElement = words.iterator().next();
		}
		return firstElement;
	}
}
