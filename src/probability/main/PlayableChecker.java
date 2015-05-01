package probability.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import probability.core.Card;
import probability.core.Color;
import probability.core.Land;
import probability.core.ManaCost;
import probability.core.Spell;

public class PlayableChecker {

	private List<Card> _cards;
	private int _startingHand;

	public PlayableChecker(List<Card> hand, int startingHand) {
		_cards = new ArrayList<Card>(hand);
		_startingHand = startingHand;
	}

	public boolean isPlayable(int turn) {

		Set<Spell> spells = filterSpellTypes(_cards);

		if (spells.isEmpty()) {
			return true;
		}

		Collection<Land> lands = retainPlayableLands(_cards, turn);

		for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext();) {
			Spell spell = iterator.next();

			if (spell.getCMC() > turn || spell.getCMC() > lands.size()) {
				iterator.remove();
			}
		}

		for (Spell spell : spells) {
			ManaCost cost = spell.getCost();

			if (isPlayableRecursion(cost, lands)) {
				return true;
			}
		}

		return false;
	}

	private Set<Spell> filterSpellTypes(List<Card> cards) {
		Set<Spell> result = new HashSet<>();

		for (Card card : cards) {
			if (card instanceof Spell) {
				result.add((Spell) card);
			}
		}

		return result;
	}

	private boolean isPlayableRecursion(ManaCost remainingCost,
			Collection<Land> remainingLands) {

		if (remainingCost.getCMC() == 0) {
			return true;
		}

		for (Land land : remainingLands) {

			Collection<Land> lands = filterToArrayList(remainingLands, land);

			for (Color color : land.colors()) {

				if (!remainingCost.containsColor(color)) {
					continue;
				}

				ManaCost reducedCosts = reduceCost(color, remainingCost);

				if (isPlayableRecursion(reducedCosts, lands)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Return a new ArrayList containing all lands but the first occurrence of
	 * the given land.
	 */
	private static Collection<Land> filterToArrayList(Collection<Land> lands,
			Land land) {

		Collection<Land> availableLands = new ArrayList<Land>(lands);
		availableLands.remove(land);

		return availableLands;
	}

	private Collection<Land> retainPlayableLands(List<Card> cards, int turn) {

		Collection<Land> result = new ArrayList<Land>();

		int num = 1;
		for (Card card : cards) {
			if (card instanceof Land) {
				Land land = (Land) card;

				int cardTurn = getAvailableTurn(num);

				if (cardTurn < turn
						|| (cardTurn == turn && !land.comesIntoPlayTapped())) {

					result.add(land);
				}
			}

			num++;
		}

		return result;
	}

	private int getAvailableTurn(int num) {
		return Math.max(num - _startingHand - 1, 1);
	}

	private static ManaCost reduceCost(Color color, ManaCost spellColors) {
		ManaCost reducedCosts = new ManaCost(spellColors);

		if (reducedCosts.getCount(color) >= 1) {
			reducedCosts.decreaseCount(color);

		} else if (reducedCosts.getCount(Color.Colorless) >= 1) {
			reducedCosts.decreaseCount(Color.Colorless);
		}

		return reducedCosts;
	}

}
