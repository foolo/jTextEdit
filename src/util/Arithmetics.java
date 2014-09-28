package util;

public class Arithmetics {

	public static int UpperBound(int value, int bound) {
		if (value > bound) {
			return bound;
		}
		return value;
	}

	public static int LowerBound(int value, int bound) {
		if (value < bound) {
			return bound;
		}
		return value;
	}
}
