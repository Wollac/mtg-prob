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

import probability.core.Color;
import probability.core.Colors;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.Land;

abstract class AbstractSingleSpellPlayableTest {

    private static final int MAX_TURN = 12;

    /**
     * Checks whether the given spell can be played with the given hand.
     */
    private static boolean isPlayable(Spell spell, int turn, Hand hand) {

        PlayableRecursion checker = new PlayableRecursion(hand, turn, spell.getCost());
        boolean playable = checker.check();

        hand.markAllInHand();

        return playable;
    }

    /**
     * Checks whether the given spell can be played not earlier than the specified turn.
     */
    static void assertIsPlayableFirstInTurn(Spell spell, Hand hand, int turn) {

        for (int i = 1; i < turn; i++) {
            assertNotPlayableInTurn(spell, hand, i);
        }
        for (int i = turn; i <= MAX_TURN; i++) {
            assertPlayableInTurn(spell, hand, i);
        }
    }

    static void assertPlayableInTurn(Spell spell, Hand hand, int i) {
        Assert.assertTrue("unexpectedly not playable in turn " + i, isPlayable(spell, i, hand));
    }

    static void assertNotPlayableInTurn(Spell spell, Hand hand, int i) {
        Assert.assertFalse("unexpectedly playable in turn " + i, isPlayable(spell, i, hand));
    }

    Spell createSpell(String costString) {
        return new Spell("", new ManaCost(costString));
    }

    Spell createSpell(Color... colors) {
        StringBuilder sb = new StringBuilder();
        for (Color color : colors) {
            sb.append(color.getLetterCode());
        }

        return createSpell(sb.toString());
    }

    private Hand createEmptyHand() {
        return new Hand(Collections.emptySet(), Collections.emptySet());
    }

    Hand createDrawingHand(Land... lands) {
        return createDrawingHand(Arrays.asList(lands));
    }

    private Hand createDrawingHand(Collection<Land> lands) {
        return new Hand(0, lands);
    }

    Hand createStartingHand(Land... lands) {
        return createStartingHand(Arrays.asList(lands));
    }

    private Hand createStartingHand(Collection<Land> lands) {
        return new Hand(lands.size(), lands);
    }

    Land createLand(String colorString) {
        return createLand(Colors.valueOf(colorString));
    }

    Land createLand(Color... colors) {

        return createLand(new Colors(colors));
    }

    abstract Land createLand(Colors colors);

    @Test
    public void testEmpty() {

        Spell spell = createSpell("1");
        Hand hand = createEmptyHand();

        Assert.assertFalse(isPlayable(spell, MAX_TURN, hand));
    }

    // Spell: G
    // Starting Hand: W U B R
    // Expected: never playable
    @Test
    public void testWrongColor() {

        final Color SPELL_COLOR = Color.Green;

        Spell spell = createSpell(SPELL_COLOR);

        Set<Color> differentColors = EnumSet.complementOf(EnumSet.of(SPELL_COLOR));

        Collection<Land> lands = new ArrayList<>();
        differentColors.stream().forEach(c -> lands.add(createLand(c)));

        Hand hand = createStartingHand(lands);

        Assert.assertFalse(isPlayable(spell, MAX_TURN, hand));
    }

    // Spell: GGGG
    // Starting Hand: W U B R G G G
    // Expected: never playable
    @Test
    public void testNotEnoughColor() {

        final Color SPELL_COLOR = Color.Green;
        final int COUNT = 4;

        String costString = Strings.repeat(Character.toString(SPELL_COLOR.getLetterCode()), COUNT);
        Spell spell = createSpell(costString);

        Set<Color> differentColors = CheckerTestUtils.getDifferentColors(SPELL_COLOR);

        Collection<Land> lands = new ArrayList<>();
        differentColors.stream().forEach(c -> lands.add(createLand(c)));
        lands.addAll(Collections.nCopies(COUNT - 1, createLand(SPELL_COLOR)));

        Hand hand = createStartingHand(lands);

        Assert.assertFalse(isPlayable(spell, MAX_TURN, hand));
    }
}
