package probability.checker;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import probability.core.Color;
import probability.core.Colors;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.Land;
import probability.core.land.ReflectingLand;
import probability.core.land.TapLand;

public class PlayableRecursionTest {

    private Spell createSpell(String costString) {
        return new Spell("", new ManaCost(costString));
    }

    private Hand createEmptyHand() {
        return new Hand(Collections.emptySet(), Collections.emptySet());
    }

    private Hand createDrawingHand(Land... lands) {
        return new Hand(Collections.emptySet(), Arrays.asList(lands));
    }

    private Land createBasicLand(String colorString) {
        return new BasicLand("", Colors.valueOf(colorString));
    }

    private Land createTapLand(String colorString) {
        return new TapLand("", Colors.valueOf(colorString));
    }

    private Land createReflectingLand() {
        return new ReflectingLand("", new Colors(Color.values()));
    }

    @Test
    public void testEmpty() {

        Spell spell = createSpell("1");
        Hand hand = createEmptyHand();

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 8);
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testNoLandInFirstTurn() {

        Spell spell = createSpell("1");
        Land basic = createBasicLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 1);
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testOneManaSpell() {

        Spell spell = createSpell("1");
        Land basic = createBasicLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 2);
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }

    @Test
    public void testWrongColor() {

        Spell spell = createSpell("W");
        Hand hand = createDrawingHand(createBasicLand("B"));

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 8);
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testNotEnoughColor() {

        Spell spell = createSpell("WW");
        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("B"));

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 8);
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testTwoLands() {

        Spell spell = createSpell("WW");
        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("W"));

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 3);
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }


    @Test
    public void testTapLandIsTapped() {

        Spell spell = createSpell("1");
        Land basic = createTapLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 2);
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testTapLandIsAvailableNextTurn() {

        Spell spell = createSpell("1");
        Land basic = createTapLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(spell, hand, 3);
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }

}