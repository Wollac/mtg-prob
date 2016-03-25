package probability.checker;

import org.junit.Assert;
import org.junit.Test;

import java.util.EnumSet;

import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.CheckLand;
import probability.core.land.Land;

public class CheckLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return new CheckLand("CHECK-" + colors.toString(), colors);
    }

    @Test
    public void notUsableFirstLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land);

        Assert.assertFalse(isPlayable(spell, 2, hand));
    }

    @Test
    public void notUsableTwoCheckLands() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);
        Land land = createLand(COLOR);

        Hand hand = createDrawingHand(land, land);

        Assert.assertFalse(isPlayable(spell, 3, hand));
    }

    @Test
    public void usableSameColorBasicLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);
        Land check = createLand(COLOR);
        Land basic = new BasicLand("", new Colors(COLOR));

        Hand hand = createDrawingHand(basic, check);

        Assert.assertFalse(isPlayable(spell, 2, hand));
        Assert.assertTrue(isPlayable(spell, 3, hand));
    }

    @Test
    public void notUsableDifferentColorBasicLand() {

        final Color CHECK_COLOR = Color.Green;

        Spell spell = createSpell(CHECK_COLOR, CHECK_COLOR);
        Land check = createLand(CHECK_COLOR);

        Color differentColor = EnumSet.complementOf(EnumSet.of(CHECK_COLOR)).iterator().next();
        Land basic = new BasicLand("", new Colors(differentColor));

        Hand hand = createDrawingHand(basic, check);

        Assert.assertFalse(isPlayable(spell, 3, hand));
    }

}
