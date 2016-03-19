package de.avalon.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.avalon.Avalon;

public class Utils {
	
	static Plugin plugin = Avalon.getPlguin();
	
	

	public static void spawnAnimatedHologram(final Entity player, String text) {
		final Hologram hologram = new Hologram(text, player.getLocation());
		
		for(double y = 0; y <= 0.5; y = y + 0.1){
				
			hologram.setVelocity(new Vector(0, 1-y, 0));
		
			
		}
		


		if(true){ //Config_Variables.Hologram_FollowPlayer_enabled
			plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
				
				@Override
				public void run() {
					hologram.setLocation(player.getLocation());
				}
			}, (long) 1, (long) 1);
		}
		
		
		
		
		
		
		
		if(true){ //Config_Variables.Hologram_Delete_enabled
			new BukkitRunnable() {
	
				@Override
				public void run() {
					hologram.remove();
					cancel();
				}
			}.runTaskLater(plugin, (long) (5 * 20)); //Config_Variables.Hologram_Delete_seconds
		}
		
	}
	
	

	private static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	private static void sendPacket(Player player, Object packet) {
		try {
			Object nmsPlayer = getNMSPlayer(player);
			Field field = nmsPlayer.getClass().getDeclaredField("playerConnection");
			field.setAccessible(true);
			Object con = field.get(nmsPlayer);
			con.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(con, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getNMSPlayer(Player player) {
		try {
			return player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static void sendActionBar(Player player, String message) {
		try {
			Object cbc = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}");
			Object packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class).newInstance(cbc, (byte) 2);
			sendPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendTitle(Player player, double fadeIn, double stay, double fadeOut, String title, String subtitle) {
		try {
			Class<?> enumtitle = getNMSClass("PacketPlayOutTitle$EnumTitleAction");
			Object packetTimes = getNMSClass("PacketPlayOutTitle").getConstructor(enumtitle, getNMSClass("IChatBaseComponent"), int.class, int.class, int.class).newInstance(enumtitle.getField("TIMES").get(enumtitle), null, (int) fadeIn * 20, (int) stay * 20, (int) fadeOut * 20);
			sendPacket(player, packetTimes);

			Object titleSub = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
			Object packetSubTitle = getNMSClass("PacketPlayOutTitle").getConstructor(enumtitle, getNMSClass("IChatBaseComponent")).newInstance(enumtitle.getField("SUBTITLE").get(enumtitle), titleSub);
			sendPacket(player, packetSubTitle);

			Object titleMain = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
			Object packetTitle = getNMSClass("PacketPlayOutTitle").getConstructor(enumtitle, getNMSClass("IChatBaseComponent")).newInstance(enumtitle.getField("TITLE").get(enumtitle), titleMain);
			sendPacket(player, packetTitle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void CreateFolder(String foldername) {
		File file;
		try {
			file = new File(plugin.getDataFolder() + File.separator + foldername);
			if (!file.exists())
				file.mkdirs();
		} catch (SecurityException e) {
			return;
		}
	}



}
