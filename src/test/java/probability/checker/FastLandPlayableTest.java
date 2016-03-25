package probability.checker;

import org.junit.Assert;
import org.junit.Test;

import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.FastLand;
import probability.core.land.Land;

public class FastLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return new FastLand("FAST-" + colors.toString(), colors);
    }

    @Test
    public void usableFirstLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land);

        Assert.assertFalse(isPlayable(spell, 1, hand));
        Assert.assertTrue(isPlayable(spell, 2, hand));
    }

    @Test
    public void usableSecondLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land, land);

        Assert.assertFalse(isPlayable(spell, 2, hand));
        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

    @Test
    public void notUsableThirdLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR, COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land, land, land);

        Assert.assertFalse(isPlayable(spell, 3, hand));
    }

    @Test
    public void thirdLandIsAvailableNextTurn() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR, COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land, land, land);

        Assert.assertTrue(isPlayable(spell, 4, hand));
    }
}
