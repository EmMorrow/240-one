package hangman;

import java.io.File;
import java.util.Scanner;

//import hangman.EvilHangmanGame.GuessAlreadyMadeException;

public class Main {
	public static void main(String args[]) {
		if (args.length != 3) throwError();
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		
		EvilHangmanGame game = new EvilHangmanGame();
		game.startGame(dictionary,wordLength);
		while (guesses > 0) {
			System.out.println("You have " + guesses + " guesses left");
			System.out.print("Used letters: " );
			if (game.getGuesses().size() > 0) {
				System.out.println(game.getGuesses().toString());
			}
			else System.out.println("");
			System.out.println("Word: " + game.getCurrentWord());
			System.out.print("Enter guess: ");
			
			Scanner sc = new Scanner(System.in);
			//String myGuess = sc.next();
			String myGuess = sc.nextLine();
			
			while (myGuess.equals("") || myGuess.length() != 1 || Character.isWhitespace(myGuess.charAt(0)) || myGuess.charAt(0) == '\n'|| myGuess.length() > 1 || !Character.isLetter(myGuess.charAt(0))) {
				System.out.println("Guess must be one letter");
				System.out.print("Enter guess: ");
				sc = new Scanner(System.in);
				myGuess = sc.nextLine();
				
			}
			

			char guess = Character.toLowerCase(myGuess.charAt(0));
			
			
			try {
				game.makeGuess(guess);
			} catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
				System.out.println("You already guessed that letter\n");
				continue;
			}
			game.addGuess(guess);
	
			
			if (game.getWord().equals(game.getCurrentWord())) {
				System.out.println("You Win! " + game.getWord());
				System.exit(0);
			}
			
			if (game.getLetters() > 0) {
				System.out.println("Yes, there is " + game.getLetters() + " " + myGuess);
			}
			else {
				System.out.println("Sorry, there are no " + myGuess +"’s"); 
				guesses--;
			}
			System.out.println("");
			
		}
		if (game.getWord().equals(game.getCurrentWord())) {
			System.out.println("You Win! " + game.getWord());
		}
		else {
			System.out.println("You lose");
			System.out.println("The word was " + game.getWord());
		}
		
		System.exit(0);
		
	}
	
	public static void throwError() {
		System.out.println("USAGE: java Main dictionary wordLength guesses");
		System.exit(1);
	}
}
