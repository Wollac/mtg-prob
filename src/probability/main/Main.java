package probability.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import probability.config.Config;
import probability.core.BasicLand;
import probability.core.Card;
import probability.core.Color;
import probability.core.Deck;
import probability.core.Land;
import probability.core.NonBasicLand;
import probability.core.Spell;
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

		Config config = new Config();

		try {
			config.load(new FileReader("mtg.config"));
		} catch (IOException e) {
			e.printStackTrace();

			return;
		}

		Deck deck = buildDeck(config.GetNumberOfCards());
		System.out.println(deck);

		Set<Integer> cmcs = getCmcs(deck);

		int minCmc = Collections.min(cmcs);
		int maxCmc = Collections.min(cmcs);

		for (int turn = minCmc; turn <= maxCmc + 3; turn++) {

			System.out.println("Turn " + turn + ": " + count(deck, turn)
					/ 10000.0);
		}

	}

	private static Set<Integer> getCmcs(Deck deck) {

		Set<Integer> result = new HashSet<>();

		for (Card card : deck.cards()) {

			if (card instanceof Spell) {
				result.add(((Spell) card).getCMC());
			}
		}

		return result;
	}

	private static Deck buildDeck(int numberOfCards) {
		Deck deck = new Deck();

		deck.add(BAYOU, 2);

		deck.add(TWILIGHT, 2);
		deck.add(WOODLAND, 4);

		deck.add(SWAMP, 5);
		deck.add(FORREST, 9);

		deck.add(new TapLand("Bird", Color.allColors()), 4);

		addSpells(deck);

		deck.fillWithDummies(numberOfCards);

		return deck;
	}

	private static void addSpells(Deck deck) {

		try {
			SpellCSVParser parse = new SpellCSVParser(new FileReader(
					"spells.csv"));

			deck.addAll(parse.readAll());
		} catch (IOException e) {

			System.err.println(e.getMessage());

		}
	}

	private static int count(Deck deck, int turn) {
		int good = 0;

		for (int i = 0; i < 1000000; i++) {
			deck.shuffle();

			List<Card> hand = deck.draw(6 + turn);

			PlayableChecker checker = new PlayableChecker(hand, 6);

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
