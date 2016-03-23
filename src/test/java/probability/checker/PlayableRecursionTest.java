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

    private Spell createSpell(String costString) {
        return new Spell("", new ManaCost(costString));
    }

    private Hand createEmptyHand() {
        return new Hand(Collections.emptySet(), Collections.emptySet());
    }

    private Hand createDrawingHand(Land... lands) {
        return new Hand(0, Arrays.asList(lands));
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

        PlayableRecursion checker = new PlayableRecursion(hand, 8, spell.getCost());
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testNoLandInFirstTurn() {

        Spell spell = createSpell("1");
        Land basic = createBasicLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(hand, 1, spell.getCost());
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testOneManaSpell() {

        Spell spell = createSpell("1");
        Land basic = createBasicLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(hand, 2, spell.getCost());
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }

    @Test
    public void testWrongColor() {

        Spell spell = createSpell("W");
        Hand hand = createDrawingHand(createBasicLand("B"));

        PlayableRecursion checker = new PlayableRecursion(hand, 8, spell.getCost());
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testNotEnoughColor() {

        Spell spell = createSpell("WW");
        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("B"));

        PlayableRecursion checker = new PlayableRecursion(hand, 8, spell.getCost());
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testTwoLands() {

        Spell spell = createSpell("WW");
        Hand hand = createDrawingHand(createBasicLand("W"), createBasicLand("W"));

        PlayableRecursion checker = new PlayableRecursion(hand, 3, spell.getCost());
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }


    @Test
    public void testTapLandIsTapped() {

        Spell spell = createSpell("1");
        Land basic = createTapLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(hand, 2, spell.getCost());
        boolean playable = checker.check();

        Assert.assertFalse(playable);
    }

    @Test
    public void testTapLandIsAvailableNextTurn() {

        Spell spell = createSpell("1");
        Land basic = createTapLand("B");

        Hand hand = createDrawingHand(basic);

        PlayableRecursion checker = new PlayableRecursion(hand, 3, spell.getCost());
        boolean playable = checker.check();

        Assert.assertTrue(playable);
    }

}