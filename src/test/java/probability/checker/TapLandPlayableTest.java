package probability.checker;

import org.junit.Assert;
import org.junit.Test;

import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.Land;
import probability.core.land.TapLand;

public class TapLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return new TapLand("TAP-" + colors.toString(), colors);
    }

    @Test
    public void testTapLandIsTapped() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land tap = createLand(COLOR);

        Hand hand = createStartingHand(tap);

        Assert.assertFalse(isPlayable(spell, 1, hand));
        Assert.assertTrue(isPlayable(spell, 2, hand));
    }

    @Test
    public void testTapLandIsAvailableNextTurn() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land basic = createLand(COLOR);

        Hand hand = createDrawingHand(basic);

        Assert.assertFalse(isPlayable(spell, 2, hand));
        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

    @Test
    public void testPreferBasicLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land tap = createLand(COLOR);
        Land basic = new BasicLand("", new Colors(COLOR));

        Hand hand = createStartingHand(tap, basic);

        Assert.assertTrue(isPlayable(spell, 1, hand));
    }
}
