package probability.checker;


import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import probability.core.Card;
import probability.core.CardUtils;
import probability.core.Color;
import probability.core.Colors;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;


class FetchableColorComputer {

    private final EnumMap<Color, Colors> _fetchableColors;

    private final Collection<Card> _cardsToFetch;

    private Set<BasicLand> _landsToFetch;

    public FetchableColorComputer(Collection<Card> cardsToFetch) {

        _fetchableColors = new EnumMap<>(Color.class);
        _cardsToFetch = cardsToFetch;
    }

    private Set<BasicLand> computeBasicLandTypesToFetch(Collection<Card> cardsToFetch) {

        Set<BasicLand> result = new HashSet<>();

        for (Card card : cardsToFetch) {
            if (card instanceof BasicLand) {
                result.add((BasicLand) card);
            }
        }

        return result;
    }


    public void initializeFetchLands(Iterable<Land> lands) {

        for (Land land : lands) {
            if (CardUtils.isFetchLand(land)) {

                FetchLand fetch = (FetchLand) land;
                fetch.setFetchableColors(getFetchableColors(fetch.colors()));
            }
        }
    }

    private Set<Color> getFetchableColors(Set<Color> colors) {

        if (_landsToFetch == null) {
            _landsToFetch = computeBasicLandTypesToFetch(_cardsToFetch);
        }

        Set<Color> fetchableColors = Color.emptyEnumSet();
        for (Color color : colors) {

            Colors currentFetchableColors = _fetchableColors.computeIfAbsent(color, this::computeFetchableColors);
            fetchableColors.addAll(currentFetchableColors.getColors());
        }

        return fetchableColors;
    }

    private Colors computeFetchableColors(Color color) {

        Set<Color> colors = Color.emptyEnumSet();

        for (BasicLand land : _landsToFetch) {
            if (land.colors().contains(color)) {
                colors.addAll(land.producibleColors());
            }
        }

        return new Colors(colors);
    }

}
