package de.avalon.utils.reflection.resolver.minecraft;

import de.avalon.utils.reflection.minecraft.Minecraft;
import de.avalon.utils.reflection.resolver.ClassResolver;

public class NMSClassResolver extends ClassResolver {
	public Class<?> resolve(String... names) throws ClassNotFoundException {
		for (int i = 0; i < names.length; i++) {
			if (!names[i].startsWith("net.minecraft.server")) {
				names[i] = ("net.minecraft.server." + Minecraft.getVersion() + names[i]);
			}
		}
		return super.resolve(names);
	}
}