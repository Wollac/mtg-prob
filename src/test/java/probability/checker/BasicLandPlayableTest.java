package probability.checker;

import org.junit.Assert;
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

    @Test
    public void testNoLandInFirstTurn() {

        Spell spell = createSpell("1");

        Land basic = createLand("B");
        Hand hand = createDrawingHand(basic);

        Assert.assertFalse(isPlayable(spell, 1, hand));
        Assert.assertTrue(isPlayable(spell, 2, hand));
    }

    @Test
    public void testOneManaSpell() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Land basic = createLand(COLOR);
        Hand hand = createStartingHand(basic);

        Assert.assertTrue(isPlayable(spell, 1, hand));
    }

    @Test
    public void testTwoManaSpell() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);
        Land basic = createLand(COLOR);

        Hand hand = createDrawingHand(basic, basic);

        Assert.assertFalse(isPlayable(spell, 2, hand));
        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

}
