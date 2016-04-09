package probability.checker;

import org.junit.Test;

import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.Land;

public class BasicLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return new BasicLand("BASIC-" + colors.toString(), colors);
    }

    // Spell: 0
    // Starting Hand: []
    // Expected: playable in the first turn
    @Test
    public void testFreeSpell() {

        Spell spell = createSpell("0");
        Hand hand = createEmptyHand();

        assertIsPlayableFirstInTurn(spell, hand, 1);
    }

    // Spell: 1
    // Starting Hand: []
    // Draws: 2->Basic(B)
    // Expected: playable not earlier than turn two
    @Test
    public void testNoLandInFirstTurn() {

        Spell spell = createSpell("1");

        Land basic = createLand("B");
        Hand hand = createDrawingHand(basic);

        assertIsPlayableFirstInTurn(spell, hand, 2);
    }

    // Spell: G
    // Starting Hand: Basic(G)
    // Expected: playable in turn one
    @Test
    public void testOneManaSpell() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Land basic = createLand(COLOR);
        Hand hand = createStartingHand(basic);

        assertIsPlayableFirstInTurn(spell, hand, 1);
    }

    // Spell: G
    // Starting Hand: Basic(WURGB)
    // Expected: playable in turn one, as the basic land can produce all colors
    @Test
    public void testMultiColorBasicLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Land basic = createLand(Color.values());
        Hand hand = createStartingHand(basic);

        assertIsPlayableFirstInTurn(spell, hand, 1);
    }

    // Spell: GG
    // Starting Hand: []
    // Draws: 2->Basic(G) 3->Basic(G)
    // Expected: playable not earlier than turn three
    @Test
    public void testTwoManaSpell() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);
        Land basic = createLand(COLOR);

        Hand hand = createDrawingHand(basic, basic);

        assertIsPlayableFirstInTurn(spell, hand, 3);
    }

    // Spell: 1G
    // Starting Hand: []
    // Draws: 2->Basic(WUBRG) 3->Basic(W)
    // Expected: playable in turn three
    @Test
    public void testBacktracking() {

        Color[] allColors = Color.values();
        final Color COLOR = allColors[allColors.length - 1];

        Spell spell = createSpell("1" + COLOR);

        Land basic2 = CheckerTestUtils.createBasicLand(CheckerTestUtils.getDifferentColor(COLOR));
        Land basic1 = CheckerTestUtils.createBasicLand(Color.values());

        Hand hand = createDrawingHand(basic1, basic2);

        assertIsPlayableFirstInTurn(spell, hand, 3);
    }
}
