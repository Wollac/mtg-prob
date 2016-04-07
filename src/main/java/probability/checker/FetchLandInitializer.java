package probability.checker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import probability.core.CardUtils;
import probability.core.Color;
import probability.core.IdentifiedCardObject;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

class FetchLandInitializer {

    private Iterable<IdentifiedCardObject> _cardObjectsToFetch;
    private Collection<IdentifiedCardObject> _fetchableBasicLandObjects;

    private final Set<FetchLand> _initializedIdentities;

    FetchLandInitializer(Iterable<IdentifiedCardObject> cardObjectsToFetch) {

        _cardObjectsToFetch = Preconditions.checkNotNull(cardObjectsToFetch);

        _initializedIdentities = Sets.newIdentityHashSet();
    }

    void initializeFetchLands(Iterable<Land> lands) {

        for (Land land : lands) {
            if (CardUtils.isFetchLand(land)) {

                FetchLand fetch = (FetchLand) land;
                if (_initializedIdentities.add(fetch)) {
                    fetch.setFetchableBasicLandObjects(getFetchableLandObjects(fetch.colors()));
                }
            }
        }
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

}
