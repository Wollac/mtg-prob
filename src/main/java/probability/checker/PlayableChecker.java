package probability.checker;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import probability.config.Settings;
import probability.core.Card;
import probability.core.Color;
import probability.core.Deck;
import probability.core.IdentifiedCardObject;
import probability.core.ManaCost;
import probability.core.MulliganRule;
import probability.core.Spell;
import probability.core.land.Land;
import probability.utils.EnumCount;

public class PlayableChecker {

    private final List<IdentifiedCardObject> _cards;

    private final MulliganRule _mulliganRule;

    public PlayableChecker(Deck deck, MulliganRule mulliganRule) {

        Preconditions.checkArgument(deck.cards().size() >= initialHandSize(),
                "not enough cards in the deck");

        _cards = IdentifiedCardObject.toCardObjects(deck.cards());

        _mulliganRule = mulliganRule;
    }

    private static int sampleSize() {
        return Settings.config.sampleSize();
    }

    private static boolean drawOnTurn() {
        return Settings.config.drawOnTurn();
    }

    private static int initialHandSize() {
        return Settings.config.initialHandSize();
    }

    private static boolean spellIsPlayable(ManaCost spellCost, EnumCount<Color> maxColorCount, int maxCMC) {

        if (spellCost.getConverted() > maxCMC) {
            return false;
        }

        for (Color color : Color.values()) {
            if (spellCost.count(color) > maxColorCount.count(color)) {
                return false;
            }
        }

        return true;
    }

    public int countPlayable(int turn) {

        int good = 0;
        for (int i = 0; i < sampleSize(); i++) {

            Hand hand = getStartingHand(turn);

            if (isPlayable(hand, turn)) {
                good++;
            }

            markAllNotPlayed();
        }

        return good;
    }

    private void markAllNotPlayed() {

        // this seams to be faster without streams
        //noinspection Convert2streamapi
        for (IdentifiedCardObject card : _cards) {
            card.markNotPlayed();
        }
    }

    private Hand getStartingHand(int turn) {

        for (int mulligan = 0; mulligan < initialHandSize(); mulligan++) {

            //TODO: only shuffle the used cards
            Collections.shuffle(_cards);

            int handSize = initialHandSize() - mulligan;
            if (drawOnTurn()) {
                handSize++;
            }

            List<Card> startingHand = getStartingCards(handSize);

            if (!_mulliganRule.takeMulligan(startingHand)) {
                return createHand(handSize, turn);
            }
        }

        return createHand(0, turn);
    }

    private List<Card> getStartingCards(int handSize) {

        List<Card> startingHand = new ArrayList<>(handSize);

        Iterator<IdentifiedCardObject> it = _cards.iterator();
        for (int i = 0; i < handSize; i++) {
            startingHand.add(it.next().get());
        }

        return startingHand;
    }

    private Hand createHand(int handSize, int turn) {

        return new Hand(_cards.subList(0, handSize),
                _cards.subList(handSize, handSize + turn - 1));
    }

    private boolean isPlayable(Hand hand, int turn) {

        if (hand.getSpellTypesUntilTurn(turn).isEmpty()) {
            return true;
        }

        initializeFetchLands(hand, turn);

        Set<Spell> playableSpellTypes = getPlayableSpellTypes(hand, turn);

        for (Spell spell : playableSpellTypes) {

            PlayableRecursion recursion = new PlayableRecursion(hand, turn, spell.getCost());

            if (recursion.check()) {
                return true;
            }
        }

        return false;
    }

    private void initializeFetchLands(Hand hand, int turn) {

        FetchLandInitializer initializer = new FetchLandInitializer(() -> _cards.listIterator(hand.size() + 1));
        initializer.initializeFetchLands(hand.getLandCardsUntilTurn(turn));
    }

    private Set<Spell> getPlayableSpellTypes(Hand hand, int turn) {

        Set<Spell> spells = hand.getSpellTypesUntilTurn(turn);

        if (spells.isEmpty()) {
            return Collections.emptySet();
        }

        Collection<Land> lands = hand.getLandCardsUntilTurn(turn);

        if (lands.isEmpty()) {
            return Collections.emptySet();
        }

        EnumCount<Color> maxColorCount = new EnumCount<>(Color.class);
        for (Land land : lands) {
            maxColorCount.increaseEach(land.producibleColors());
        }

        final int maxConverted = Math.min(lands.size(), turn);

        for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext(); ) {
            Spell spell = iterator.next();

            if (!spellIsPlayable(spell.getCost(), maxColorCount, maxConverted)) {
                iterator.remove();
            }
        }

        return spells;
    }

}
