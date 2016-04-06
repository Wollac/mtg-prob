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
}
