package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import probability.core.land.BasicLand;
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
		return (card instanceof Land);
	}

	public final static boolean isBasicLand(Card card) {
		return (card instanceof BasicLand);
	}

	final static boolean isDummy(Card card) {
		return card.equals(DUMMY_CARD);
	}

	final static void retainAllLands(Collection<Card> cards) {
		retain(cards, c -> isLand(c));
	}

	final static Collection<Land> retainAllLandsToArrayList(
			Collection<Card> cards) {

		Collection<Card> lands = new ArrayList<>(cards);
		retainAllLands(lands);

		return uncheckedCast(lands);
	}

	final static void retainAllSpells(Collection<Card> cards) {
		retain(cards, c -> isSpell(c));
	}

	final static Collection<Spell> retainAllSpellsToArrayList(
			Collection<Card> cards) {

		Collection<Card> spells = new ArrayList<>(cards);
		retainAllSpells(spells);

		return uncheckedCast(spells);
	}

	/**
	 * Remove all elements from Card collection that don't fulfill the predicate
	 */
	final static private void retain(Collection<Card> cards,
			Predicate<Card> predicate) {

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
		public String toString() {
			return getName();
		}
	};

}
