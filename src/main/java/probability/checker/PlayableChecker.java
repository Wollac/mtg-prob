package probability.checker;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import probability.config.Settings;
import probability.core.CardObject;
import probability.core.Color;
import probability.core.Deck;
import probability.core.ManaCost;
import probability.core.MulliganRule;
import probability.core.land.Land;
import probability.utils.EnumCount;

public class PlayableChecker {

    private final List<CardObject> _cards;

    private final MulliganRule _mulliganRule;

    public PlayableChecker(Deck deck, MulliganRule mulliganRule) {

        Preconditions.checkArgument(deck.cards().size() >= initialHandSize(),
                "not enough cards in the deck");

        _cards = CardObject.toCardObjects(deck.cards());

        _mulliganRule = mulliganRule;
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

    private static int sampleSize() {
        return Settings.config.sampleSize();
    }

    private static boolean drawOnTurn() {
        return Settings.config.drawOnTurn();
    }

    private static int initialHandSize() {
        return Settings.config.initialHandSize();
    }

    private void markAllNotPlayed() {

        _cards.forEach(CardObject::markNotPlayed);
    }

    private Hand getStartingHand(int turn) {

        for (int mulligan = 0; mulligan < initialHandSize(); mulligan++) {

            //TODO: only shuffle the used cards
            Collections.shuffle(_cards);

            int handSize = initialHandSize() - mulligan;
            if (drawOnTurn()) {
                handSize++;
            }

            if (!_mulliganRule.takeMulligan(_cards.subList(0, handSize))) {
                return createHand(handSize, turn);
            }
        }

        return createHand(0, turn);
    }

    private Hand createHand(int handSize, int turn) {

        return new Hand(_cards.subList(0, handSize), _cards.subList(handSize, handSize + turn - 1));
    }

    private boolean isPlayable(Hand hand, int maxTurn) {

        Set<ManaCost> spellCosts = hand.getAllSpellCosts();

        // we are computing the combined failure probability, thus we have a
        // success if there are no spells have been drawn
        if (spellCosts.isEmpty()) {
            return true;
        }

        List<CardObject> landObjects = hand.getAllLandObjects();

        // initialize fetch lands only if there are some lands
        if (!landObjects.isEmpty()) {
            initializeFetchLands(landObjects, hand.size());
        }

        Set<ManaCost> playableSpellCosts = getPlayableManaCosts(spellCosts, landObjects, maxTurn);

        for (ManaCost spellCost : playableSpellCosts) {

            PlayableRecursion recursion = new PlayableRecursion(hand, maxTurn, spellCost);

            if (recursion.check()) {
                return true;
            }
        }

        return false;
    }

    private void initializeFetchLands(List<CardObject> landObjects, int skipObjectsInDeck) {

        Iterable<CardObject> objectsToFetch = () -> _cards.listIterator(skipObjectsInDeck + 1);
        FetchLandInitializer initializer = new FetchLandInitializer(objectsToFetch);

        initializer.initializeFetchLands(landObjects);
    }

    /**
     * Creates a new set containing only those ManaCosts that could definitely be paid using the
     * provided landObjects.
     */
    private Set<ManaCost> getPlayableManaCosts(Collection<ManaCost> spellCosts,
                                               Collection<CardObject> landObjects, int maxTurn) {

        // if there are no costs to begin with, return an empty set
        if (spellCosts.isEmpty()) {
            return Collections.emptySet();
        }

        EnumCount<Color> maxColorCount = new EnumCount<>(Color.class);
        for (CardObject o : landObjects) {
            Land land = (Land) o.get();
            maxColorCount.increaseEach(land.producibleColors());
        }

        // assuming that a land can produce at most one mana
        final int maxConverted = Math.min(landObjects.size(), maxTurn);

        Set<ManaCost> result = new HashSet<>();
        for (ManaCost cost : spellCosts) {
            if (spellIsPlayable(cost, maxColorCount, maxConverted)) {
                result.add(cost);
            }
        }

        return result;
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

}
