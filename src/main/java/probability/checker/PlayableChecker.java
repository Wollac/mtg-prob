package probability.checker;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import probability.config.Settings;
import probability.core.Card;
import probability.core.Color;
import probability.core.Deck;
import probability.core.EnumCount;
import probability.core.ManaCost;
import probability.core.MulliganRule;
import probability.core.Spell;
import probability.core.land.Land;

public class PlayableChecker {

    private final List<CardObject> _cards;

    private final MulliganRule _mulliganRule;

    public PlayableChecker(Deck deck, MulliganRule mulliganRule) {

        _cards = deck.cards().stream().map(CardObject::new).collect(Collectors.toList());
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

    private static boolean spellIsPlayable(ManaCost spellCost, EnumCount<Color> producableColors, int maxCMC) {

        if (spellCost.getConverted() > maxCMC) {
            return false;
        }

        for (Color color : Color.values()) {
            if (spellCost.count(color) > producableColors.count(color)) {
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

            hand.markAllUnplayed();
        }

        return good;
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
        for (CardObject cardObject : Iterables.limit(_cards, handSize)) {
            startingHand.add(cardObject.get());
        }

        return startingHand;
    }

    private Hand createHand(int handSize, int turn) {

        return new Hand(_cards.subList(0, handSize),
                _cards.subList(handSize, handSize + turn - 1));
    }

    private boolean isPlayable(Hand hand, int turn) {

        //TODO
        //initializeFetchLands(turn);

        Set<Spell> playableSpellTypes = getPlayableSpellTypes(hand, turn);

        for (Spell spell : playableSpellTypes) {

            PlayableRecursion recursion = new PlayableRecursion(hand, turn, spell.getCost());

            if (recursion.check()) {
                return true;
            }
        }

        return false;
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

        EnumCount<Color> producableColors = new EnumCount<>(Color.class);
        for (Land land : lands) {
            producableColors.increaseEach(land.producibleColors());
        }

        final int maxConverted = Math.min(lands.size(), turn);

        for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext(); ) {
            Spell spell = iterator.next();

            if (!spellIsPlayable(spell.getCost(), producableColors, maxConverted)) {
                iterator.remove();
            }
        }

        return spells;
    }

}
