package probability.checker;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import probability.core.Colors;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.Land;
import probability.core.land.TapLand;

public class PlayableRecursionTest {

    private static boolean isPlayable(Spell spell, int turn, Hand hand) {

        PlayableRecursion checker = new PlayableRecursion(hand, turn, spell.getCost());
        boolean playable = checker.check();

        hand.markAllUnplayed();

        return playable;
    }

    private Spell createSpell(String costString) {
        return new Spell("", new ManaCost(costString));
    }

    private Hand createEmptyHand() {
        return new Hand(Collections.emptySet(), Collections.emptySet());
    }

    private Hand createDrawingHand(Land... lands) {
        return new Hand(0, Arrays.asList(lands));
    }

    private Hand createStartingHand(Land... lands) {
        return new Hand(lands.length, Arrays.asList(lands));
    }

    private Land createBasicLand(String colorString) {
        return new BasicLand("", Colors.valueOf(colorString));
    }

    private Land createTapLand(String colorString) {
        return new TapLand("", Colors.valueOf(colorString));
    }

    @Test
    public void testEmpty() {

        Spell spell = createSpell("1");
        Hand hand = createEmptyHand();

        Assert.assertFalse(isPlayable(spell, 8, hand));
    }

    @Test
    public void testNoLandInFirstTurn() {

        Spell spell = createSpell("1");

        Land basic = createBasicLand("B");
        Hand hand = createDrawingHand(basic);

        Assert.assertFalse(isPlayable(spell, 1, hand));
    }

    @Test
    public void testOneManaSpell() {

        Spell spell = createSpell("1");

        Land basic = createBasicLand("B");
        Hand hand = createDrawingHand(basic);

        Assert.assertTrue(isPlayable(spell, 2, hand));
    }

    @Test
    public void testWrongColor() {

        Spell spell = createSpell("W");

        Land basic = createBasicLand("B");
        Hand hand = createDrawingHand(basic);

        Assert.assertFalse(isPlayable(spell, 8, hand));
    }

    @Test
    public void testNotEnoughColor() {

        Spell spell = createSpell("WW");

        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("B"));

        Assert.assertFalse(isPlayable(spell, 8, hand));
    }

    @Test
    public void testTwoLands() {

        Spell spell = createSpell("WW");

        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("W"));

        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

    @Test
    public void testTapLandIsTapped() {

        Spell spell = createSpell("1");
        Land tap = createTapLand("B");

        Hand hand = createStartingHand(tap);

        Assert.assertFalse(isPlayable(spell, 1, hand));
        Assert.assertTrue(isPlayable(spell, 2, hand));
    }

    @Test
    public void testTapLandIsAvailableNextTurn() {

        Spell spell = createSpell("1");

        Land basic = createTapLand("B");
        Hand hand = createDrawingHand(basic);

        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

}