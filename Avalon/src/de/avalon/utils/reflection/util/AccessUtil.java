package de.avalon.utils.reflection.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AccessUtil {
	public static Field setAccessible(Field f) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		/* 44 */ f.setAccessible(true);
		/* 45 */ Field modifiersField = Field.class.getDeclaredField("modifiers");
		/* 46 */ modifiersField.setAccessible(true);
		/* 47 */ modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		/* 48 */ return f;
	}

	public static Method setAccessible(Method m) throws SecurityException, IllegalArgumentException, IllegalAccessException {
		/* 55 */ m.setAccessible(true);
		/* 56 */ return m;
	}

	public static Constructor<?> setAccessible(Constructor<?> c) throws SecurityException, IllegalArgumentException, IllegalAccessException {
		/* 63 */ c.setAccessible(true);
		/* 64 */ return c;
	}
}
