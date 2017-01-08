package probability.checker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import probability.core.CardObject;
import probability.core.Color;
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

  private final Set<FetchLand> _initializedIdentities;

  private Iterable<CardObject> _cardObjectsToFetch;

  private final Supplier<Collection<CardObject>> _basicLandObjectsToFetch =
      Suppliers.memoize(this::retainBasicLandObjectsToFetch);

  FetchLandInitializer(Iterable<CardObject> cardObjectsToFetch) {

    _cardObjectsToFetch = Preconditions.checkNotNull(cardObjectsToFetch);

    _initializedIdentities = Sets.newIdentityHashSet();
  }

  void initializeFetchLands(Iterable<CardObject> landObjects) {

    for (CardObject o : landObjects) {
      Land land = (Land) o.get();
      if (land instanceof FetchLand) {
        initializeFetchLand((FetchLand) land);
      }
    }
  }

  void initializeFetchLand(FetchLand fetchLand) {

    if (_initializedIdentities.add(fetchLand)) {
      fetchLand.setFetchableBasicLandObjects(getFetchableLandObjects(fetchLand.colors()));
    }
  }

  private Collection<CardObject> getFetchableLandObjects(Set<Color> colors) {

    Collection<CardObject> result = new ArrayList<>();

    for (CardObject landObject : _basicLandObjectsToFetch.get()) {

      if (!Collections.disjoint(((BasicLand) landObject.get()).colors(), colors)) {
        result.add(landObject);
      }
    }

    return result;
  }

  private Collection<CardObject> retainBasicLandObjectsToFetch() {

    Collection<CardObject> result = new ArrayList<>();

    for (CardObject cardObject : _cardObjectsToFetch) {
      if (cardObject.get() instanceof BasicLand) {
        result.add(cardObject);
      }
    }
    // no longer needed
    _cardObjectsToFetch = null;

    return result;
  }

}
