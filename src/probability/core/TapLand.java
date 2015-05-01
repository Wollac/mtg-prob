package probability.core;

public class TapLand extends AbstractLand implements Land {

	public TapLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped() {
		return true;
	}

	@Override
	public boolean isFetchable(Color color) {
		return false;
	}

}
