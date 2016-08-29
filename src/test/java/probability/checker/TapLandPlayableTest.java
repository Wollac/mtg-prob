package probability.checker;

import org.junit.Test;

import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.Land;
import probability.core.land.TapLand;

public class TapLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return new TapLand("TAP-" + colors.toString(), colors);
    }

    // Spell: G
    // Starting Hand: [Tap(G)]
    // Expected: playable not earlier than turn two, as the land comes into play tapped
    @Test
    public void isTappedInStartingHand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land tap = createLand(COLOR);

        Hand hand = createStartingHand(tap);

        assertIsPlayableFirstInTurn(spell, hand, 2);
    }

    // Spell: G
    // Starting Hand: []
    // Draws: 2->Tap(G)
    // Expected: playable not earlier than turn three, as the land comes into play tapped
    @Test
    public void isTappedWhenDrawn() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land basic = createLand(COLOR);

        Hand hand = createDrawingHand(basic);

        assertIsPlayableFirstInTurn(spell, hand, 3);
    }

    // Spell: G
    // Starting Hand: Basic(G) Tap(G)
    // Expected: playable in first turn, as the basic land can be used
    @Test
    public void testPreferBasicLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Land tap = createLand(COLOR);
        Land basic = CheckerTestUtils.createBasicLand(COLOR);

        Hand hand = createStartingHand(tap, basic);

        assertIsPlayableFirstInTurn(spell, hand, 1);
    }
}
