package probability.checker;


import probability.core.CardUtils;
import probability.core.Color;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;


class FetchableColorComputer {

    private final Collection<IdentifiedCardObject> _fetchableBasicLandObjects;

    public FetchableColorComputer(Iterable<IdentifiedCardObject> cardObjectsToFetch) {

        _fetchableBasicLandObjects = new ArrayList<>();

        for (IdentifiedCardObject cardObject : cardObjectsToFetch) {
            if (CardUtils.isBasicLand(cardObject.get())) {
                _fetchableBasicLandObjects.add(cardObject);
            }
        }

    }

    public void initializeFetchLands(Iterable<Land> lands) {

        for (Land land : lands) {
            if (CardUtils.isFetchLand(land)) {

                FetchLand fetch = (FetchLand) land;

                fetch.setfetchableBasicLandObjects(getFetchableLandObjects(fetch.colors()));
            }
        }
    }

    private Collection<IdentifiedCardObject> getFetchableLandObjects(Set<Color> colors) {

        Collection<IdentifiedCardObject> result = new ArrayList<>();

        for (IdentifiedCardObject landObject : _fetchableBasicLandObjects) {

            if (!Collections.disjoint(((BasicLand) landObject.get()).colors(), colors)) {
                result.add(landObject);
            }
        }

        return result;
    }


}
