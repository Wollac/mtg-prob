package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;

public final class CardUtils {
	private CardUtils() {
	}

	final static Card getDummyCard() {
		return DUMMY_CARD;
	}

	final static boolean isSpell(Card card) {
		return (card instanceof Spell);
	}

	final static boolean isLand(Card card) {
		return  (card instanceof Land);
	}

	final static boolean isDummy(Card card) {
		return card.equals(DUMMY_CARD);
	}

	public final static boolean isBasicLand(Card card) {
		return (card instanceof BasicLand);
	}

	public static boolean isFetchLand(Card card) {
		return (card instanceof FetchLand);
	}

	public final static void retainAllLands(Collection<Card> cards) {
		retain(cards, c -> isLand(c));
	}

	public final static Collection<Land> retainAllLandsToArrayList(
			Collection<Card> cards) {

		Collection<Card> lands = new ArrayList<>(cards);
		retainAllLands(lands);

		return uncheckedCast(lands);
	}

	public final static void retainAllSpells(Collection<Card> cards) {
		retain(cards, c -> isSpell(c));
	}

	public final static Collection<Spell> retainAllSpellsToArrayList(
			Collection<Card> cards) {

		Collection<Card> spells = new ArrayList<>(cards);
		retainAllSpells(spells);

		return uncheckedCast(spells);
	}

	/** Create a set that contains the cards sorted by getName() */
	public final static SortedSet<Card> sortCardsByName(Collection<Card> cards) {
		SortedSet<Card> sorted = new TreeSet<>(
				Comparator.comparing(Card::getName));
		sorted.addAll(cards);

		return sorted;
	}

	/**
	 * Remove all elements from Card collection that don't fulfill the predicate
	 */
	private static void retain(Collection<Card> cards, Predicate<Card> predicate) {

		for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext();) {
			Card card = iterator.next();

			if (!predicate.test(card)) {
				iterator.remove();
			}
		}
	}

	@SuppressWarnings("unchecked")
	final static private <T> Collection<T> uncheckedCast(Collection<Card> cards) {
		return (Collection<T>) cards;

	}

	private static final Card DUMMY_CARD = new Card() {

		@Override
		public String getName() {
			return "Dummy";
		}

		@Override
		public CardType getCardType() {
			return CardType.Other;
		}

		@Override
		public String toString() {
			return getName();
		}

	};

}
