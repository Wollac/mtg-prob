package probability.checker;

import org.junit.Test;
import probability.core.Color;
import probability.core.Colors;
import probability.core.Spell;
import probability.core.land.FastLand;
import probability.core.land.Land;

public class FastLandPlayableTest extends AbstractSingleSpellPlayableTest {

  @Override Land createLand(Colors colors) {
    return new FastLand("FAST-" + colors.toString(), colors);
  }

  // Spell: G
  // Starting Hand: []
  // Draws: 2->Fast(G)
  // Expected: playable in turn two, as the fast land does not come into play tapped
  @Test public void usableFirstLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land);

    assertIsPlayableFirstInTurn(spell, hand, 2);
  }

  // Spell: GG
  // Starting Hand: []
  // Draws: 2->Fast(G) 3->Fast(G)
  // Expected: playable in turn three, as the fast lands do not come into play tapped
  @Test public void usableSecondLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land, land);

    assertIsPlayableFirstInTurn(spell, hand, 3);
  }

  // Spell: GGG
  // Starting Hand: []
  // Draws: 2->Fast(G) 3->Fast(G) 4->Fast(G)
  // Expected: not playable earlier in turn three, as the last fast land comes into play tapped
  @Test public void usableThirdLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR, COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land, land, land);

    assertIsPlayableFirstInTurn(spell, hand, 4);
  }

  // Spell: GGGG
  // Starting Hand: []
  // Draws: 2->Fast(G) 3->Fast(G) 4->Fast(G) 5->Fast(G)
  // Expected: not playable earlier than turn six, as the last fast land comes into play tapped
  @Test public void notUsableForthLand() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR, COLOR, COLOR);
    Land land = createLand(COLOR);

    Hand hand = createDrawingHand(land, land, land, land);

    assertIsPlayableFirstInTurn(spell, hand, 6);
  }

  // Spell: GGG
  // Starting Hand: []
  // Draws: 2->Basic(G) 3->Basic(W) 4->Fast(G) 5->Fast(G)
  // Expected: playable in turn five, as the last fast land comes into play untapped, if the second basic is not played
  @Test public void usableIfLandIsNotPlayed() {

    final Color COLOR = Color.Green;

    Spell spell = createSpell(COLOR, COLOR, COLOR);

    Land check = createLand(COLOR);

    Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);
    Land differentBasic = CheckerTestUtils.createBasicLand(differentColor);
    Land sameBasic = CheckerTestUtils.createBasicLand(COLOR);

    Hand hand = createDrawingHand(sameBasic, differentBasic, check, check);

    assertIsPlayableFirstInTurn(spell, hand, 5);
  }
}
