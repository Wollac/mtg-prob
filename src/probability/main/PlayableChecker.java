package probability.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

	public PlayableChecker(List<Card> hand) {
		_cards = new ArrayList<Card>(hand);
	}

	public boolean isPlayable(int turn) {

		Set<Spell> spells = filterSpellTypes(_cards);

		if (spells.isEmpty()) {
			return true;
		}

		List<Land> lands = Collections.unmodifiableList(filterLands(_cards));

		for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext();) {
			Spell spell = iterator.next();

			if (spell.getCMC() > turn || spell.getCMC() > lands.size()) {
				iterator.remove();
			}
		}

		for (Spell spell : spells) {
			ManaCost spellColors = spell.getCost();

			if (spell.getCMC() == turn) {
				if (isPlayable(spellColors, lands)) {
					return true;
				}
			} else {
				if (isTappedPlayable(spellColors, lands)) {
					return true;
				}
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

	private List<Land> filterLands(List<Card> cards) {
		List<Land> result = new ArrayList<>();

		for (Card card : cards) {
			if (card instanceof Land) {
				result.add((Land) card);
			}
		}

		return result;
	}

	private boolean isPlayable(ManaCost spellColors, Collection<Land> lands) {
		for (Color color : spellColors.getColors()) {
			ManaCost reducedCosts = reduceCost(color, spellColors);

			for (Land land : lands) {
				if (land.canProduce(color) && !land.comesIntoPlayTapped()) {

					Collection<Land> availableLands = new ArrayList<Land>(lands);
					availableLands.remove(land);

					if (isTappedPlayable(reducedCosts, availableLands))
						return true;
				}
			}
		}

		return false;
	}

	private boolean isTappedPlayable(ManaCost spellColors,
			Collection<Land> lands) {

		if (spellColors.getCMC() == 0)
			return true;

		for (Land land : lands) {
			Collection<Land> availableLands = new ArrayList<Land>(lands);
			availableLands.remove(land);

			for (Color color : land.colors()) {
				if (spellColors.getCount(Color.Colorless) < 1
						&& spellColors.getCount(color) < 1) {
					continue;
				}

				ManaCost reducedCosts = reduceCost(color, spellColors);

				if (isTappedPlayable(reducedCosts, availableLands)) {
					return true;
				}
			}
		}

		return false;
	}

	private ManaCost reduceCost(Color color, ManaCost spellColors) {
		ManaCost reducedCosts = new ManaCost(spellColors);

		if (reducedCosts.getCount(color) >= 1) {
			reducedCosts.decreaseCount(color);

		} else if (reducedCosts.getCount(Color.Colorless) >= 1) {
			reducedCosts.decreaseCount(Color.Colorless);
		}

		return reducedCosts;
	}

}
