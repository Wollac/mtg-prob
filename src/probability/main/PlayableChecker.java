package probability.main;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import probability.core.Board;
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

		Collection<Land> lands = _hand.getLandsUntilTurn(turn);

		Collection<Spell> spells = _hand.getSpellsUntilTurn(turn);

		if (spells.isEmpty()) {
			return true;
		}

		Set<Spell> playableSpells = getPlayableSpells(spells, turn, lands);

		for (Spell spell : playableSpells) {

			PlayableRecursion recursion = new PlayableRecursion(spell, _hand,
					turn);

			if (recursion.check()) {
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

	private static class PlayableRecursion {

		private final Spell _spell;

		private final Hand _hand;

		private final int _maxTurn;

		public PlayableRecursion(Spell spell, Hand hand, int maxTurn) {
			_spell = spell;
			_hand = hand;
			_maxTurn = maxTurn;
		}

		public boolean check() {
			Board board = new Board();

			ManaCost cost = _spell.getCost();

			return recursion(board, null, cost, 1);
		}

		private boolean recursion(Board board, Color tappedColor,
				ManaCost remainingCost, int turn) {

			if (turn > _maxTurn) {
				return false;
			}

			if (tappedColor != null) {
				remainingCost = reduceCost(tappedColor, remainingCost);

				if (remainingCost.getCMC() == 0) {
					return true;
				}
			}

			Set<Land> availableLandTypes = getAvailableLandTypes(board, turn);

			for (Land land : availableLandTypes) {

				final boolean tapped = land.comesIntoPlayTapped(board);

				board.playLand(land);

				for (Color color : land.colors()) {
					if (!remainingCost.containsColor(color)) {
						continue;
					}

					if (tapped) {
						tappedColor = color;

						if (recursion(board, tappedColor, remainingCost,
								turn + 1)) {
							return true;
						}
					} else {
						remainingCost = reduceCost(color, remainingCost);

						if (remainingCost.getCMC() == 0) {
							return true;
						}

						if (recursion(board, null, remainingCost, turn + 1)) {
							return true;
						}
					}

				}

				board.popLand();
			}

			return recursion(board, null, remainingCost, turn + 1);
		}

		private Set<Land> getAvailableLandTypes(Board board, int turn) {

			Collection<Land> lands = _hand.getLandsUntilTurn(turn);

			for (Land land : board.getPlayedLands()) {
				lands.remove(land);
			}

			return new HashSet<Land>(lands);
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

}
