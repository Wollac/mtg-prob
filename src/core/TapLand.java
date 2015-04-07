package core;

public class TapLand extends AbstractLand implements Land {

	public TapLand(String name, Color... colors) {
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
