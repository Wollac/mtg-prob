package probability.core;

public class NonBasicLand extends AbstractLand implements Land {

	public NonBasicLand(String name, Color... colors) {
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
