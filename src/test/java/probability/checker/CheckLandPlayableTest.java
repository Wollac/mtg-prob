package probability.checker;

import org.junit.Test;
import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.CheckLand;
import probability.core.land.Land;

public class CheckLandPlayableTest extends AbstractSingleSpellPlayableTest {

  @Override Land createLand(Colors colors) {
    return new CheckLand("CHECK-" + colors.toString(), colors);
  }

  // Spell: G
  // Starting Hand: []
  // Draws: 2->Check(G)
  // Expected: playable not earlier than turn three, as the check land comes into play tapped
  @Test public void notUsableFirstLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land);

    assertIsPlayableFirstInTurn(spell, hand, 3);
  }

  // Spell: GG
  // Starting Hand: []
  // Draws: 2->Check(G) 3->Check(G)
  // Expected: playable not earlier than turn four, as the check land comes into play tapped
  @Test public void notUsableTwoCheckLands() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land, land);

    assertIsPlayableFirstInTurn(spell, hand, 4);
  }

  // Spell: GG
  // Starting Hand: []
  // Draws: 2->Basic(G) 3->Check(G)
  // Expected: playable not earlier than turn three, the check land does not come into play tapped
  @Test public void usableSameColorBasicLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR);

    Land basic = CheckerTestUtils.createBasicLand(COLOR);
    Land check = createLand(COLOR);

    Hand hand = createDrawingHand(basic, check);

    assertIsPlayableFirstInTurn(spell, hand, 3);
  }

  // Spell: G
  // Starting Hand: []
  // Draws: 2->Basic(W) 3->Check(G)
  // Expected: playable not earlier than turn four, as the check land comes into play tapped
  @Test public void notUsableDifferentColorBasicLand() {

    final Color CHECK_COLOR = Color.Green;

    Spell spell = createSpell(CHECK_COLOR);
    Land check = createLand(CHECK_COLOR);

    Color differentColor = CheckerTestUtils.getDifferentColor(CHECK_COLOR);
    Land basic = CheckerTestUtils.createBasicLand(differentColor);

    Hand hand = createDrawingHand(basic, check);

    assertIsPlayableFirstInTurn(spell, hand, 4);
  }

  // Spell: G
  // Starting Hand: []
  // Draws: 2->Basic(W) 3->Check(W U R B G)
  // Expected: playable in turn three, as the check land comes into play untapped, if the basic land has been played
  @Test public void unusableBasicIsNecessary() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR);
    Land check = createLand(Color.values());

    Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);
    Land basic = CheckerTestUtils.createBasicLand(differentColor);

    Hand hand = createDrawingHand(basic, check);

    assertIsPlayableFirstInTurn(spell, hand, 3);
  }
}
