package de.avalon.utils.reflection.resolver.minecraft;

import de.avalon.utils.reflection.minecraft.Minecraft;
import de.avalon.utils.reflection.resolver.ClassResolver;

public class OBCClassResolver extends ClassResolver {
	public Class<?> resolve(String... names) throws ClassNotFoundException {
		for (int i = 0; i < names.length; i++) {
			if (!names[i].startsWith("org.bukkit.craftbukkit")) {
				names[i] = ("org.bukkit.craftbukkit." + Minecraft.getVersion() + names[i]);
			}
		}
		return super.resolve(names);
	}
}