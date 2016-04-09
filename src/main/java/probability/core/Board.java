package probability.core;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

import probability.core.land.BasicLand;
import probability.core.land.Land;

public class Board {

    private final Stack<CardObject> _playedObjects;

    private int _numPlayedLands;

    public Board() {
        _playedObjects = new Stack<>();
        _numPlayedLands = 0;
    }


    public int getNumPlayedLands() {
        return _numPlayedLands;
    }

    public void play(CardObject cardObject) {

        _playedObjects.push(cardObject);
        if (cardObject.isLand()) {
            _numPlayedLands++;
        }
    }

    public CardObject pop() {

        CardObject cardObject = _playedObjects.pop();
        if (cardObject.isLand()) {
            _numPlayedLands--;
            assert _numPlayedLands >= 0;
        }

        return cardObject;
    }

    public boolean isBasicColorPlayed(Set<Color> colors) {

        for (CardObject o : _playedObjects) {

            if (o.isLand()) {
                Land land = (Land) o.get();
                if (land instanceof BasicLand && !Collections.disjoint(colors, land.colors())) {
                    return true;
                }
            }
        }

        return false;
    }

}
