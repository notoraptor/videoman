package videoman.core;

import videoman.Utils;

import java.util.LinkedList;

public enum Notation {
	ZERO, ONE, TWO, THREE, FOUR, FIVE;
	//static public final String diam = "\u2666";
	//static public final String diam = "\u2605";
	//static public final String diam = "\ud83c\udf11";
	static public final String diam = "\u2716";
	//static public final String star = "\u2606";
	//static public final String star = "\ud83c\udf15";
	static public final String star = "\u2714";
	@Override
	public String toString() {
		int countPositive = ordinal();
		int countNegative = 5 - countPositive;
		LinkedList<String> elements = new LinkedList<>();
		for(int i = 0; i < countPositive; ++i)
			elements.add(star);
		for(int i = 0; i < countNegative; ++i)
			elements.add(diam);
		return Utils.implode(" ", elements).toString();
	}
}
