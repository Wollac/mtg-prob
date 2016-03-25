package probability.checker;


import com.google.common.base.Strings;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import probability.config.Settings;
import probability.core.Color;
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

        hand.markAllInHand();

        return playable;
    }

    private Spell createSpell(String costString) {
        return new Spell("", new ManaCost(costString));
    }

    private Hand createEmptyHand() {
        return new Hand(Collections.emptySet(), Collections.emptySet());
    }

    private Hand createDrawingHand(Land... lands) {
        return createDrawingHand(Arrays.asList(lands));
    }

    private Hand createDrawingHand(Collection<Land> lands) {
        return new Hand(0, lands);
    }

    private Hand createStartingHand(Land... lands) {
        return createStartingHand(Arrays.asList(lands));
    }

    private Hand createStartingHand(Collection<Land> lands) {
        return new Hand(lands.size(), lands);
    }

    private Land createBasicLand(String colorString) {
        return new BasicLand(colorString, Colors.valueOf(colorString));
    }

    private Land createBasicLand(Color... colors) {

        Colors landColors = new Colors(colors);
        return new BasicLand(landColors.toString(), landColors);
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
    public void testManyLands() {

        final Color SPELL_COLOR = Color.Green;
        final int COUNT = 4;

        String costString = Strings.repeat(Character.toString(SPELL_COLOR.getLetterCode()), COUNT);
        Spell spell = createSpell(costString);

        Set<Color> unnecessaryColors = EnumSet.complementOf(EnumSet.of(SPELL_COLOR));

        Collection<Land> lands = new ArrayList<>();
        unnecessaryColors.stream().forEach(c -> lands.add(createBasicLand(c)));
        lands.addAll(Collections.nCopies(COUNT, createBasicLand(SPELL_COLOR)));

        Hand hand = createStartingHand(lands);

        Assert.assertTrue(isPlayable(spell, COUNT, hand));
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