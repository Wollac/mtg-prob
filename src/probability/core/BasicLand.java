package probability.core;

public class BasicLand extends AbstractLand {

	public BasicLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped() {
		return false;
	}

	@Override
	public boolean isFetchable(Color color) {
		if (color != Color.Colorless && colors().contains(color)) {
			return true;
		}

		return false;
	}

}
