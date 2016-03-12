package probability.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import probability.core.land.Land;

public class Board {

	private final Stack<Land> _playedLands;

	public Board() {
		_playedLands = new Stack<>();
	}

	public void playLand(Land land) {
		_playedLands.push(land);
	}

	public Land popLand() {
		return _playedLands.pop();
	}

	public Collection<Land> getPlayedLands() {
		return Collections.unmodifiableCollection(_playedLands);
	}

	public int getNumPlayedLands() {
		return _playedLands.size();
	}
	
	public Set<Color> getPlayedLandProducibleColors() {
		Set<Color> colors = new HashSet<>();

		for (Land land : _playedLands) {
				colors.addAll(land.producibleColors());
		}

		return colors;
	}

	public boolean isBasicColorPlayed(Color color) {
		return getPlayedBasicLandColors().contains(color);
	}

	public boolean isBasicColorPlayed(Set<Color> colors) {
		return !Collections.disjoint(colors, getPlayedBasicLandColors());
	}

	private Set<Color> getPlayedBasicLandColors() {

		Set<Color> colors = Color.emptyEnumSet();

		for (Land land : _playedLands) {
			if (CardUtils.isBasicLand(land)) {
				colors.addAll(land.colors());
			}
		}

		return colors;
	}

}
