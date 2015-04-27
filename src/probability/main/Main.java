package probability.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import probability.core.BasicLand;
import probability.core.Card;
import probability.core.Color;
import probability.core.Deck;
import probability.core.Land;
import probability.core.NonBasicLand;
import probability.core.TapLand;
import probability.csv.SpellCSVParser;

public class Main {

	private static final Land BAYOU = new BasicLand("Bayou", Color.Black,
			Color.Green);

	private static final Land WOODLAND = new TapLand("Woodland Cemetery",
			Color.Black, Color.Green);

	private static final Land TWILIGHT = new NonBasicLand("Twilight Mire",
			Color.Black, Color.Green);

	private static final Land SWAMP = new BasicLand("Swamp", Color.Black);

	private static final Land FORREST = new BasicLand("Forrest", Color.Green);

	public static void main(String[] args) {

		Deck deck = new Deck();

		deck.add(BAYOU, 3);

		deck.add(TWILIGHT, 2);
		deck.add(WOODLAND, 4);

		deck.add(SWAMP, 5);
		deck.add(FORREST, 9);

		deck.add(new TapLand("Bird", Color.allColors()), 4);

		addSpells(deck);

		deck.fillWithDummies(60);
		
		System.out.println(deck);
		

		System.out.println(count(deck, 2) / 5000.0);

	}

	private static void addSpells(Deck deck) {

		try {
			SpellCSVParser parse = new SpellCSVParser(new FileReader(
					"spells.csv"));

			deck.addAll(parse.readAll());
		} catch (FileNotFoundException e) {

			System.err.println(e.getMessage());

		} catch (IOException e) {
			
			System.err.println(e.getMessage());
		}
	}

	private static int count(Deck deck, int turn) {
		int good = 0;

		for (int i = 0; i < 500000; i++) {
			deck.shuffle();

			List<Card> hand = deck.draw(6 + turn);

			PlayableChecker checker = new PlayableChecker(hand);

			if (checker.isPlayable(turn)) {
				good++;
			}
		}
		return good;
	}

	public static int randomInteger(int min, int max) {

		Random rand = new Random();

		// nextInt excludes the top value so we have to add 1 to include the top
		// value
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
