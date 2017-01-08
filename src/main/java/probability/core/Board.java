package probability.core;

import probability.core.land.BasicLand;
import probability.core.land.Land;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

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

  /** Updates the board by playing the given card object. */
  public void play(CardObject cardObject) {

    _playedObjects.push(cardObject);
    if (cardObject.isLand()) {
      _numPlayedLands++;
    }
  }

  /** Undoes the last played card object. */
  public CardObject pop() {

    CardObject cardObject = _playedObjects.pop();
    if (cardObject.isLand()) {
      _numPlayedLands--;
      assert _numPlayedLands >= 0;
    }

    return cardObject;
  }

  /** Returns whether a basic land was played that produces one of the given colors. */
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
