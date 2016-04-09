package probability.checker;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import probability.config.Settings;
import probability.core.CardObject;
import probability.core.Color;
import probability.core.Deck;
import probability.core.ManaCost;
import probability.core.MulliganRule;
import probability.core.Spell;
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
        for (CardObject card : _cards) {
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

            if (!_mulliganRule.takeMulligan(_cards.subList(0, handSize))) {
                return createHand(handSize, turn);
            }
        }

        return createHand(0, turn);
    }

    private Hand createHand(int handSize, int turn) {

        return new Hand(_cards.subList(0, handSize),
                _cards.subList(handSize, handSize + turn - 1));
    }

    private boolean isPlayable(Hand hand, int maxTurn) {

        Set<Spell> spells = hand.getAllSpellTypes();
        if (spells.isEmpty()) {
            return true;
        }

        List<CardObject> landObjects = hand.getAllLands();
        initializeFetchLands(landObjects, hand.size());

        Set<Spell> playableSpellTypes = getPlayableSpellTypes(spells, landObjects, maxTurn);

        for (Spell spell : playableSpellTypes) {

            PlayableRecursion recursion = new PlayableRecursion(hand, maxTurn, spell.getCost());

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

    private Set<Spell> getPlayableSpellTypes(Set<Spell> spells, Collection<CardObject> landObjects, int maxTurn) {

        if (landObjects.isEmpty()) {
            return Collections.emptySet();
        }

        EnumCount<Color> maxColorCount = new EnumCount<>(Color.class);
        for (CardObject o : landObjects) {
            Land land = (Land) o.get();
            maxColorCount.increaseEach(land.producibleColors());
        }

        final int maxConverted = Math.min(landObjects.size(), maxTurn);

        for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext(); ) {
            Spell spell = iterator.next();

            if (!spellIsPlayable(spell.getCost(), maxColorCount, maxConverted)) {
                iterator.remove();
            }
        }

        return spells;
    }

}
