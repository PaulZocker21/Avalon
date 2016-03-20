package de.avalon.utils.reflection.minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import de.avalon.utils.reflection.resolver.minecraft.OBCClassResolver;
import de.avalon.utils.reflection.util.AccessUtil;

public class Minecraft {
	public static final Version VERSION;
	private static OBCClassResolver obcClassResolver = new OBCClassResolver();
	private static Class<?> CraftEntity;

	static {
		VERSION = Version.getVersion();
		System.out.println("[ReflectionHelper] Version is " + VERSION);
		try {
			CraftEntity = obcClassResolver.resolve(new String[] { "entity.CraftEntity" });
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getVersion() {
		return VERSION.name() + ".";
	}

	public static Object getHandle(Object object) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method;
		try {
			method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getHandle", new Class[0]));
		} catch (ReflectiveOperationException e) {
			method = AccessUtil.setAccessible(CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
		}
		return method.invoke(object, new Object[0]);
	}

	public static Entity getBukkitEntity(Object object) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method;
		try {
			method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getBukkitEntity", new Class[0]));
		} catch (ReflectiveOperationException e) {
			method = AccessUtil.setAccessible(CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
		}
		return (Entity) method.invoke(object, new Object[0]);
	}

	public static Object getHandleSilent(Object object) {
		try {
			return getHandle(object);
		} catch (Exception e) {
		}
		return null;
	}

	public static enum Version {
		UNKNOWN(-1),

		v1_7_R1(10701), v1_7_R2(10702), v1_7_R3(10703), v1_7_R4(10704),

		v1_8_R1(10801), v1_8_R2(10802), v1_8_R3(10803),

		v1_8_R4(10804),

		v1_9_R1(109001);

		private int version;

		private Version(int version) {
			this.version = version;
		}

		public int version() {
			return this.version;
		}

		public boolean olderThan(Version version) {
			return version() < version.version();
		}

		public boolean newerThan(Version version) {
			return version() >= version.version();
		}

		public boolean inRange(Version oldVersion, Version newVersion) {
			return (newerThan(oldVersion)) && (olderThan(newVersion));
		}

		public boolean matchesPackageName(String packageName) {
			return packageName.toLowerCase().contains(name().toLowerCase());
		}

		public static Version getVersion() {
			String name = Bukkit.getServer().getClass().getPackage().getName();
			String versionPackage = name.substring(name.lastIndexOf('.') + 1) + ".";
			for (Version version : values()) {
				if (version.matchesPackageName(versionPackage))
					return version;
			}
			System.err.println("[ReflectionHelper] Failed to find version enum for '" + name + "'/'" + versionPackage + "'");
			return UNKNOWN;
		}

		public String toString() {
			return name() + " (" + version() + ")";
		}
	}
}