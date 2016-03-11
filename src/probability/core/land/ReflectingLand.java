package probability.core.land;

import java.util.Set;

import com.google.common.collect.Sets;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

public class ReflectingLand extends NonBasicLand {

	public ReflectingLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public Set<Color> producesColors(Board board) {
		return Sets.intersection(board.getPlayedLandProducibleColors(), colors());
	}

}
