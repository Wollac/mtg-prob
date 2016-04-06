package probability.checker;

import org.junit.Test;
import probability.core.Color;
import probability.core.Colors;
import probability.core.IdentifiedCardObject;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FetchLandPlayableTest extends AbstractSingleSpellPlayableTest {

    @Override
    Land createLand(Colors colors) {
        return createFetchLand(colors);
    }

    private static FetchLand createFetchLand(Color... colors) {
        Colors landColors = new Colors(colors);
        return createFetchLand(landColors);
    }

    private static FetchLand createFetchLand(Colors landColors) {
        return new FetchLand("FETCH-" + landColors.toString(), landColors);
    }

    @Test
    public void fetchSingleColorBasic() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        FetchLand fetch = createFetchLand(COLOR);
        Hand hand = createStartingHand(fetch);

        initialize(fetch, CheckerTestUtils.createBasicLand(COLOR));

        assertPlayableInTurn(spell, hand, 1);
    }

    @Test
    public void fetchSingleCorrectBasic() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        FetchLand fetch = createFetchLand(COLOR);
        Hand hand = createStartingHand(fetch);

        initialize(fetch, CheckerTestUtils.createBasicLand(CheckerTestUtils.getDifferentColor(COLOR)), CheckerTestUtils.createBasicLand(COLOR));

        assertPlayableInTurn(spell, hand, 1);
    }

    @Test
    public void onlyOneFetchable() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);

        FetchLand fetch = createFetchLand(COLOR);
        initialize(fetch, CheckerTestUtils.createBasicLand(COLOR));

        Hand hand = createStartingHand(fetch, fetch);

        assertNotPlayableInTurn(spell, hand, 1);
    }

    @Test
    public void onlyUnusedFetchLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);

        FetchLand fetch = createFetchLand(differentColor);
        initialize(fetch, CheckerTestUtils.createBasicLand(differentColor));

        Hand hand = createDrawingHand(fetch, CheckerTestUtils.createBasicLand(COLOR));

        assertPlayableInTurn(spell, hand, 3);
    }

    private static void initialize(FetchLand fetch, BasicLand... lands) {

        List<IdentifiedCardObject> objectList = IdentifiedCardObject.toCardObjects(Arrays.asList(lands), 1000);

        FetchLandInitializer computer = new FetchLandInitializer(objectList);
        computer.initializeFetchLands(Collections.singleton(fetch));
    }
}
