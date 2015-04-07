package probability;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import core.Card;
import core.Color;
import core.Deck;
import core.Land;
import core.BasicLand;
import core.NonBasicLand;
import core.Spell;
import core.TapLand;

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

		// deck.add(new Spell("Bird of Paradies", "G"), 4);

		 deck.add(new Spell("Putird Leech", "BG"), 4);
		 deck.add(new Spell("Bitterblossom", "1B"), 4);
		 deck.add(new Spell("Umezawa's Jitte", "2"), 2);

//		 deck.add(new Spell("Master of the Wild Hunt", "2GG"), 3);
//		 deck.add(new Spell("Garruk Wildspeaker", "2GG"), 3);
//		 deck.add(new Spell("Creakwood Liege", "4"), 4);

		// deck.add(new Spell("Spiritmonger", "3BG"), 2);

		deck.fillWithDummies(60);

		System.out.println(count(deck, 2) / 5000.0);

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
