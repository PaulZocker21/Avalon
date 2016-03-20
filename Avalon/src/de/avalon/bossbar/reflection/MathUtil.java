package de.avalon.bossbar.reflection;

public abstract class MathUtil {
	public static int floor(double d1) {
		/* 34 */ int i = (int) d1;
		/* 35 */ return d1 >= i ? i : i - 1;
	}

	public static int d(float f1) {
		/* 39 */ int i = (int) f1;
		/* 40 */ return f1 >= i ? i : i - 1;
	}
}