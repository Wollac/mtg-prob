package probability.checker;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import probability.core.Board;
import probability.core.land.Land;

class PlayableCache {

    private static int _cacheHits;
    private final List<Set<Stack<Land>>> _list;

    public PlayableCache(int n) {

        _list = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            _list.add(new HashSet<>());
        }
    }

    public static int getCacheHits() {
        return _cacheHits;
    }

    public void add(Board board, int remainingTurns) {
        @SuppressWarnings("unchecked")
        Stack<Land> stack = (Stack<Land>) board.getPlayedLands().clone();
        _list.get(remainingTurns).add(stack);
    }

    public boolean contains(Board board, int remainingTurns) {

        boolean contained = _list.get(remainingTurns).contains(board.getPlayedLands());

        if (contained) {
            _cacheHits++;
        }

        return contained;
    }

    public int size() {

        int size = 0;
        for (Set<Stack<Land>> set : _list) {
            size += set.size();
        }

        return size;
    }
}
