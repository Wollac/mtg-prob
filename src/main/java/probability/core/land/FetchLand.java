package probability.core.land;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;
import probability.core.IdentifiedCardObject;

/**
 * Models Fetch Lands such as <a href="http://magiccards.info/query?q=!Flooded+Strand">Flooded
 * Strand</a>.
 * <p>
 * Fetch lands can produce the colors of all {@linkplain BasicLand}s that are not yet played or
 * drawn. They never enter the battlefield tapped.
 */
public class FetchLand extends AbstractLand {

    private List<IdentifiedCardObject> _fetchableBasicLandObjects;

    private Colors _producibleColors;

    public FetchLand(String name, Colors colors) {

        super(name, colors);

        _fetchableBasicLandObjects = Collections.emptyList();
        _producibleColors = new Colors();
    }

    public void setFetchableBasicLandObjects(Collection<IdentifiedCardObject> basicLandObjects) {

        _fetchableBasicLandObjects = new ArrayList<>(basicLandObjects);

        Set<Color> colors = Color.emptyEnumSet();

        for (IdentifiedCardObject basicLandObject : _fetchableBasicLandObjects) {
            colors.addAll(((Land) basicLandObject.get()).producibleColors());
        }

        _producibleColors = new Colors(colors);
    }

    @Override
    public Set<Color> producibleColors() {
        return _producibleColors.getColors();
    }

    @Override
    public Iterable<Color> producesColors() {

        if (_fetchableBasicLandObjects.isEmpty()) {
            return Collections.emptySet();
        }

        return () -> new ColorIterator(_fetchableBasicLandObjects);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }


    /**
     * An iterator that iterates over fetchable colors and at the same time marks the corresponding
     * card objects played or not.
     */
    private static class ColorIterator implements Iterator<Color> {

        private final Iterator<Map.Entry<Color, IdentifiedCardObject>> _it;

        private IdentifiedCardObject _lastLandObject = null;

        ColorIterator(List<IdentifiedCardObject> landObjects) {

            EnumMap<Color, IdentifiedCardObject> objectsByColor = new EnumMap<>(Color.class);

            for (IdentifiedCardObject landObject : landObjects) {
                if (!landObject.isPlayed()) {

                    for (Color color : ((Land) landObject.get()).producibleColors()) {
                        objectsByColor.putIfAbsent(color, landObject);
                    }

                }
            }

            _it = objectsByColor.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {

            final boolean hasNext = _it.hasNext();

            // TODO probably an AutoCloseable is more java-like
            // we abuse the hasNext to get something like a destructor
            if (!hasNext) {
                markLastNotPlayed();
                _lastLandObject = null;
            }

            return hasNext;
        }

        @Override
        public Color next() {

            Map.Entry<Color, IdentifiedCardObject> entry = _it.next();

            markLastNotPlayed();
            markLandObjectPlayed(entry.getValue());

            return entry.getKey();
        }

        private void markLandObjectPlayed(IdentifiedCardObject landObject) {

            assert !landObject.isPlayed();
            landObject.markPlayed();
            _lastLandObject = landObject;
        }

        private void markLastNotPlayed() {

            if (_lastLandObject != null) {
                assert _lastLandObject.isPlayed();
                _lastLandObject.markNotPlayed();
            }
        }
    }

}
