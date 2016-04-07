package probability.checker;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import probability.core.Color;
import probability.core.Colors;
import probability.core.IdentifiedCardObject;
import probability.core.Spell;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;

public class FetchLandPlayableTest extends AbstractSingleSpellPlayableTest {

    private static FetchLand createFetchLand(Color... colors) {
        Colors landColors = new Colors(colors);
        return createFetchLand(landColors);
    }

    private static FetchLand createFetchLand(Colors landColors) {
        return new FetchLand("FETCH-" + landColors.toString(), landColors);
    }

    private static void initializeFetchLand(FetchLand fetch, BasicLand... lands) {

        List<IdentifiedCardObject> objectList = IdentifiedCardObject.toCardObjects(Arrays.asList(lands), 1000);

        FetchLandInitializer computer = new FetchLandInitializer(objectList);
        computer.initializeFetchLand(fetch);
    }

    @Override
    Land createLand(Colors colors) {
        return createFetchLand(colors);
    }


    // Spell: G
    // Starting Hand: Fetch(G)
    // Fetchable Lands: Basic(G)
    // Expected: playable in turn one, as the correct basic can be fetched
    @Test
    public void fetchSingleColorBasic() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        FetchLand fetch = createFetchLand(COLOR);
        Hand hand = createStartingHand(fetch);

        initializeFetchLand(fetch, CheckerTestUtils.createBasicLand(COLOR));

        assertPlayableInTurn(spell, hand, 1);
    }

    // Spell: G
    // Starting Hand: Fetch(WG)
    // Fetchable Lands: Basic(W) Basic(G)
    // Expected: playable in turn one, as the correct basic can be fetched
    @Test
    public void fetchSingleCorrectBasic() {

        final Color COLOR = Color.Green;
        Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);

        Spell spell = createSpell(COLOR);

        FetchLand fetch = createFetchLand(COLOR, differentColor);
        Hand hand = createStartingHand(fetch);

        BasicLand wrongBasic = CheckerTestUtils.createBasicLand(differentColor);
        BasicLand basic = CheckerTestUtils.createBasicLand(COLOR);
        initializeFetchLand(fetch, wrongBasic, basic);

        assertPlayableInTurn(spell, hand, 1);
    }

    // Spell: G
    // Starting Hand: Fetch(W)
    // Fetchable Lands: Basic(W) Basic(WURGB)
    // Expected: playable in turn one, as the correct basic can be fetched
    @Test
    public void fetchMultiColorBasic() {

        final Color COLOR = Color.Green;
        Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);

        Spell spell = createSpell(COLOR);

        FetchLand fetch = createFetchLand(differentColor);
        Hand hand = createStartingHand(fetch);

        BasicLand wrongBasic = CheckerTestUtils.createBasicLand(differentColor);
        BasicLand basic = CheckerTestUtils.createBasicLand(Color.values());
        initializeFetchLand(fetch, wrongBasic, basic);

        assertPlayableInTurn(spell, hand, 1);
    }

    // Spell: GG
    // Starting Hand: Fetch(G) Fetch(G)
    // Fetchable Lands: Basic(G)
    // Expected: never playable as there is only one land that can be fetched
    @Test
    public void onlyOneFetchable() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR, COLOR);

        FetchLand fetch = createFetchLand(COLOR);
        initializeFetchLand(fetch, CheckerTestUtils.createBasicLand(COLOR));

        Hand hand = createStartingHand(fetch, fetch);

        assertNotPlayableInTurn(spell, hand, MAX_TURN);
    }

    // Spell: G
    // Starting Hand: []
    // Draws: 2->Fetch(W) 3->Basic(G)
    // Fetchable Lands: Basic(W)
    // Expected: never playable as there is only one land that can be fetched
    @Test
    public void onlyUnusedFetchLand() {

        final Color COLOR = Color.Green;

        Spell spell = createSpell(COLOR);

        Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);
        FetchLand fetch = createFetchLand(differentColor);
        BasicLand basic = CheckerTestUtils.createBasicLand(COLOR);
        Hand hand = createDrawingHand(fetch, basic);

        initializeFetchLand(fetch, CheckerTestUtils.createBasicLand(differentColor));

        assertNotPlayableInTurn(spell, hand, 2);
    }

    // Spell: 1G
    // Starting Hand: []
    // Draws: 2->Fetch(WG) 3->Fetch(W)
    // Fetchable Lands: Basic(W) Basic(W) Basic(G)
    // Expected: playable in turn three
    @Test
    public void testBacktracking() {

        Color[] allColors = Color.values();
        final Color COLOR = allColors[allColors.length - 1];
        Color differentColor = CheckerTestUtils.getDifferentColor(COLOR);

        Spell spell = createSpell("1" + COLOR);

        FetchLand fetch1 = createFetchLand(differentColor, COLOR);
        FetchLand fetch2 = createFetchLand(differentColor);
        Hand hand = createDrawingHand(fetch1, fetch2);

        BasicLand basic1 = CheckerTestUtils.createBasicLand(differentColor);
        BasicLand basic2 = CheckerTestUtils.createBasicLand(COLOR);

        BasicLand[] toFetch = new BasicLand[]{basic1, basic1, basic2};

        initializeFetchLand(fetch1, toFetch);
        initializeFetchLand(fetch2, toFetch);

        assertPlayableInTurn(spell, hand, 3);
    }
}
