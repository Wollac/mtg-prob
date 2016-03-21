package probability.checker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import probability.core.Card;
import probability.core.Deck;
import probability.core.Spell;
import probability.core.land.Land;

public class PlayableChecker {

    private final Deck _deck;

    private final Hand _hand;

    public PlayableChecker(Deck deck, Hand hand) {
        _deck = deck;
        _hand = hand;
    }

    public boolean isPlayable(int turn) {

        initializeFetchLands(turn);

        Set<Spell> playableSpellTypes = getPlayableSpellTypes(turn);

        for (Spell spell : playableSpellTypes) {

            PlayableRecursion recursion = new PlayableRecursion(spell, _hand, turn);

            if (recursion.check()) {
                return true;
            }
        }

        return false;
    }

    private void initializeFetchLands(int turn) {
        Collection<Land> lands = _hand.getLandsUntilTurn(turn);
        new FetchableColorComputer(getRemainingCards()).initializeFetchLands(lands);
    }

    private Set<Spell> getPlayableSpellTypes(int turn) {
        Set<Spell> spells = _hand.getSpellTypesUntilTurn(turn);

        Collection<Land> lands = _hand.getLandsUntilTurn(turn);

        return getPlayableSpells(spells, turn, lands);
    }


    private Collection<Card> getRemainingCards() {
        List<Card> allCards = _deck.cards();

        return allCards.subList(_hand.size(), allCards.size());
    }

    private Set<Spell> getPlayableSpells(Collection<Spell> spells, int turn,
                                         Collection<Land> lands) {

        Set<Spell> result = new HashSet<>(spells);

        for (Iterator<Spell> iterator = result.iterator(); iterator.hasNext(); ) {
            Spell spell = iterator.next();

            if (spell.getCMC() > turn || spell.getCMC() > lands.size()) {
                iterator.remove();
            }
        }

        return result;
    }

}
