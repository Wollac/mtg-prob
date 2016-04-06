package probability.checker;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import probability.core.CardUtils;
import probability.core.Color;
import probability.core.IdentifiedCardObject;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;


class FetchLandInitializer {

    private final List<FetchLand> _initialized = new ArrayList<>();
    private Iterable<IdentifiedCardObject> _cardObjectsToFetch;
    private Collection<IdentifiedCardObject> _fetchableBasicLandObjects = null;

    FetchLandInitializer(Iterable<IdentifiedCardObject> cardObjectsToFetch) {

        _cardObjectsToFetch = cardObjectsToFetch;
    }

    private Collection<IdentifiedCardObject> getAllFetchableLandObjects() {

        Collection<IdentifiedCardObject> _fetchableBasicLandObjects;
        _fetchableBasicLandObjects = new ArrayList<>();

        for (IdentifiedCardObject cardObject : _cardObjectsToFetch) {
            if (CardUtils.isBasicLand(cardObject.get())) {
                _fetchableBasicLandObjects.add(cardObject);
            }
        }
        _cardObjectsToFetch = null;

        return _fetchableBasicLandObjects;
    }

    void initializeFetchLands(Iterable<Land> lands) {

        for (Land land : lands) {
            if (CardUtils.isFetchLand(land)) {

                FetchLand fetch = (FetchLand) land;

                if (!isInitialized(fetch)) {
                    fetch.setFetchableBasicLandObjects(getFetchableLandObjects(fetch.colors()));
                    _initialized.add(fetch);
                }
            }
        }
    }

    private boolean isInitialized(FetchLand land) {

        for (FetchLand initializedLand : _initialized) {
            if (initializedLand == land) {
                return true;
            }
        }

        return false;
    }

    private Collection<IdentifiedCardObject> getFetchableLandObjects(Set<Color> colors) {

        if (_fetchableBasicLandObjects == null) {
            _fetchableBasicLandObjects = getAllFetchableLandObjects();
        }

        Collection<IdentifiedCardObject> result = new ArrayList<>();

        for (IdentifiedCardObject landObject : _fetchableBasicLandObjects) {

            if (!Collections.disjoint(((BasicLand) landObject.get()).colors(), colors)) {
                result.add(landObject);
            }
        }

        return result;
    }

}
