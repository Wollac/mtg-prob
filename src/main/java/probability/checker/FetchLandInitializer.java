package probability.checker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import probability.core.CardUtils;
import probability.core.Color;
import probability.core.IdentifiedCardObject;
import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;
import probability.utils.Suppliers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

final class FetchLandInitializer {

    private Iterable<IdentifiedCardObject> _cardObjectsToFetch;

    private final Supplier<Collection<IdentifiedCardObject>> _basicLandObjectsToFetch =
            Suppliers.memoize(this::retainBasicLandObjectsToFetch);

    private final Set<FetchLand> _initializedIdentities;

    FetchLandInitializer(Iterable<IdentifiedCardObject> cardObjectsToFetch) {

        _cardObjectsToFetch = Preconditions.checkNotNull(cardObjectsToFetch);

        _initializedIdentities = Sets.newIdentityHashSet();
    }

    void initializeFetchLands(Iterable<? extends Land> lands) {

        for (Land land : lands) {

            if (CardUtils.isFetchLand(land)) {
                initializeFetchLand((FetchLand) land);
            }
        }
    }

    void initializeFetchLand(FetchLand fetchLand) {

        if (_initializedIdentities.add(fetchLand)) {
            fetchLand.setFetchableBasicLandObjects(getFetchableLandObjects(fetchLand.colors()));
        }
    }

    private Collection<IdentifiedCardObject> getFetchableLandObjects(Set<Color> colors) {

        Collection<IdentifiedCardObject> result = new ArrayList<>();

        for (IdentifiedCardObject landObject : _basicLandObjectsToFetch.get()) {

            if (!Collections.disjoint(((BasicLand) landObject.get()).colors(), colors)) {
                result.add(landObject);
            }
        }

        return result;
    }

    private Collection<IdentifiedCardObject> retainBasicLandObjectsToFetch() {

        Collection<IdentifiedCardObject> result = new ArrayList<>();

        for (IdentifiedCardObject cardObject : _cardObjectsToFetch) {
            if (CardUtils.isBasicLand(cardObject.get())) {
                result.add(cardObject);
            }
        }
        // no longer needed
        _cardObjectsToFetch = null;

        return result;
    }

}
