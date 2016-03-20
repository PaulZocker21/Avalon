package de.avalon.bossbar.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AccessUtil {
	public static Field setAccessible(Field f) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		/* 42 */ f.setAccessible(true);
		/* 43 */ Field modifiersField = Field.class.getDeclaredField("modifiers");
		/* 44 */ modifiersField.setAccessible(true);
		/* 45 */ modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		/* 46 */ return f;
	}

	public static Method setAccessible(Method m) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		/* 55 */ m.setAccessible(true);
		/* 56 */ return m;
	}
}