package probability.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import probability.core.Color;
import probability.core.Hand;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.Land;

public class PlayableChecker {

	private Hand _hand;

	public PlayableChecker(Hand hand) {
		_hand = hand;
	}

	public boolean isPlayable(int turn) {

		Collection<Land> lands = getPlayableLands(turn);

		Collection<Spell> spells = _hand.getSpellsUntilTurn(turn);

		if (spells.isEmpty()) {
			return true;
		}

		Set<Spell> playableSpells = getPlayableSpells(spells, turn, lands);

		for (Spell spell : playableSpells) {
			ManaCost cost = spell.getCost();

			if (isPlayableRecursion(cost, lands, 0)) {
				return true;
			}
		}

		return false;
	}

	private Set<Spell> getPlayableSpells(Collection<Spell> spells, int turn,
			Collection<Land> lands) {

		Set<Spell> result = new HashSet<>(spells);

		for (Iterator<Spell> iterator = result.iterator(); iterator.hasNext();) {
			Spell spell = iterator.next();

			if (spell.getCMC() > turn || spell.getCMC() > lands.size()) {
				iterator.remove();
			}
		}

		return result;
	}

	private boolean isPlayableRecursion(ManaCost remainingCost,
			Collection<Land> remainingLands, int turn) {

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

				if (isPlayableRecursion(reducedCosts, lands, turn + 1)) {
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

	private Collection<Land> getPlayableLands(int turn) {

		Collection<Land> result = _hand.getLandsUntilTurn(turn - 1);

		Collection<Land> landsLastTurn = _hand.getLandsInTurn(turn);

		for (Land land : landsLastTurn) {
			if (!land.comesIntoPlayTapped()) {
				result.add(land);
			}
		}

		return result;
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
