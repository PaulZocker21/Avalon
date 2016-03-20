package de.avalon.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.avalon.Avalon;
import de.avalon.bossbar.reflection.Reflection;
import de.avalon.utils.reflection.minecraft.Minecraft;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BossBarAPI implements Listener {
	/* 57 */ protected static final Map<UUID, Collection<BossBar>> barMap = new ConcurrentHashMap<>();

	/* 59 */ public static boolean is1_9 = Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1);

	public static enum Color {
		/* 62 */ PINK, /* 63 */ BLUE, /* 64 */ RED, /* 65 */ GREEN, /* 66 */ YELLOW, /* 67 */ PURPLE, /* 68 */ WHITE;

		private Color() {
		}
	}

	/* 72 */ public static enum Style {
		PROGRESS, /* 73 */ NOTCHED_6, /* 74 */ NOTCHED_10, /* 75 */ NOTCHED_12, /* 76 */ NOTCHED_20;

		private Style() {
		}
	}

	/* 80 */ public static enum Property {
		DARKEN_SKY, /* 81 */ PLAY_MUSIC,

		/* 83 */ CREATE_FOG;

		private Property() {
		}
	}

	public static BossBar addBar(Collection<Player> players, String message, Color color, Style style, float progress, Property... properties) {
		/* 98 */ validate1_9();
		/* 99 */ BossBar bossBar = new PacketBossBar(message, color, style, progress, properties);
		/* 100 */ for (Player player : players) {
			/* 101 */ addBarForPlayer(player, bossBar);
		}
		/* 103 */ return bossBar;
	}

	public static BossBar addBar(Collection<Player> players, BaseComponent component, Color color, Style style, float progress, Property... properties) {
		/* 118 */ validate1_9();
		/* 119 */ BossBar bossBar = new PacketBossBar(component, color, style, progress, properties);
		/* 120 */ for (Player player : players) {
			/* 121 */ addBarForPlayer(player, bossBar);
		}
		/* 123 */ return bossBar;
	}

	public static BossBar addBar(Collection<Player> players, BaseComponent component, Color color, Style style, float progress, int timeout, long interval, Property... properties) {
		/* 140 */ validate1_9();
		/* 141 */ BossBar bossBar = addBar(players, component, color, style, progress, properties);
		/* 142 */ new BossBarTimer((PacketBossBar) bossBar, progress, timeout).runTaskTimer(Avalon.getPlguin(), interval, interval);
		/* 143 */ return bossBar;
	}

	public static BossBar addBar(Player player, BaseComponent component, Color color, Style style, float progress, Property... properties) {
		if (is1_9) {
			BossBar bossBar = new PacketBossBar(component, color, style, progress, properties);
			addBarForPlayer(player, bossBar);
			return bossBar;
		}
		setMessage(player, component.toLegacyText(), progress * 100.0F);
		return getBossBar(player);
	}

	public static BossBar addBar(Player player, BaseComponent component, Color color, Style style, float progress, int timeout, long interval, Property... properties) {
		/* 183 */ if (is1_9) {
			/* 184 */ BossBar bossBar = addBar(player, component, color, style, progress, properties);
			/* 185 */ new BossBarTimer((PacketBossBar) bossBar, progress, timeout).runTaskTimer(Avalon.getPlguin(), interval, interval);
			/* 186 */ return bossBar;
		}
		/* 188 */ setMessage(player, component.toLegacyText(), progress * 100.0F, timeout);
		/* 189 */ return getBossBar(player);
	}

	public static BossBar addBar(BaseComponent component, Color color, Style style, float progress, Property... properties) {
		/* 206 */ validate1_9();
		/* 207 */ return new PacketBossBar(component, color, style, progress, properties);
	}

	public static Collection<BossBar> getBossBars(Player player) {
		/* 211 */ if (!barMap.containsKey(player.getUniqueId()))
			return new ArrayList<>();
		/* 212 */ return new ArrayList<>((Collection<BossBar>) barMap.get(player.getUniqueId()));
	}

	protected static void addBarForPlayer(Player player, BossBar bossBar) {
		/* 216 */ bossBar.addPlayer(player);

		/* 218 */ Collection<BossBar> collection = (Collection<BossBar>) barMap.get(player.getUniqueId());
		/* 219 */ if (collection == null)
			collection = new ArrayList<>();
		/* 220 */ collection.add(bossBar);
		/* 221 */ barMap.put(player.getUniqueId(), collection);
	}

	protected static void removeBarForPlayer(Player player, BossBar bossBar) {
		/* 225 */ bossBar.removePlayer(player);

		/* 227 */ Collection<BossBar> collection = (Collection<BossBar>) barMap.get(player.getUniqueId());
		/* 228 */ if (collection != null) {
			/* 229 */ collection.remove(bossBar);
			/* 230 */ if (!collection.isEmpty()) {
				/* 231 */ barMap.put(player.getUniqueId(), collection);
			} else {
				/* 233 */ barMap.remove(player.getUniqueId());
			}
		}
	}

	public static void removeAllBars(Player player) {
		/* 239 */ for (BossBar bossBar : getBossBars(player)) {
			/* 240 */ removeBarForPlayer(player, bossBar);
		}
	}

	public static void setMessage(Player player, String message) {
		/* 254 */ setMessage(player, message, 100.0F);
	}

	public static void setMessage(Player player, String message, float percentage) {
		setMessage(player, message, percentage, 0);
	}

	public static void setMessage(Player player, String message, float percentage, int timeout) {
		setMessage(player, message, percentage, timeout, 100.0F);
	}

	public static void setMessage(Player player, String message, float percentage, int timeout, float minHealth) {
		if (is1_9) {
			removeAllBars(player);
			addBar(player, new TextComponent(message), Color.PURPLE, Style.PROGRESS, percentage / 100.0F, new Property[0]);
		} else {
			if (!barMap.containsKey(player.getUniqueId())) {
				ArrayList<BossBar> list = new ArrayList<>();
				list.add(new EntityBossBar(player, message, percentage, timeout, minHealth));
				barMap.put(player.getUniqueId(), list);
			}
			BossBar bar = (BossBar) ((List<?>) barMap.get(player.getUniqueId())).get(0);
			if (!bar.getMessage().equals(message)) {
				bar.setMessage(message);
			}
			float newHealth = percentage / 100.0F * bar.getMaxHealth();
			if (bar.getHealth() != newHealth) {
				bar.setHealth(percentage);
			}
			if (!bar.isVisible()) {
				bar.setVisible(true);
			}
		}
	}

	public static String getMessage(Player player) {
		/* 322 */ BossBar bar = getBossBar(player);
		/* 323 */ if (bar == null)
			return null;
		/* 324 */ return bar.getMessage();
	}

	public static boolean hasBar(@Nonnull Player player) {
		/* 333 */ return barMap.containsKey(player.getUniqueId());
	}

	public static void removeBar(@Nonnull Player player) {
		/* 343 */ BossBar bar = getBossBar(player);
		/* 344 */ if (bar != null)
			bar.setVisible(false);
		/* 345 */ removeAllBars(player);
	}

	public static void setHealth(Player player, float percentage) {
		/* 356 */ BossBar bar = getBossBar(player);
		/* 357 */ if (bar == null)
			return;
		/* 358 */ bar.setHealth(percentage);
	}

	public static float getHealth(@Nonnull Player player) {
		/* 367 */ BossBar bar = getBossBar(player);
		/* 368 */ if (bar == null)
			return -1.0F;
		/* 369 */ return bar.getHealth();
	}

	@Nullable
	public static BossBar getBossBar(@Nonnull Player player) {
		/* 381 */ if (player == null)
			return null;
		/* 382 */ List<BossBar> list = (List<BossBar>) barMap.get(player.getUniqueId());
		/* 383 */ return list != null ? (BossBar) list.get(0) : null;
	}

	public static Collection<BossBar> getBossBars() {
		/* 391 */ List<BossBar> list = new ArrayList<>();
		/* 392 */ for (Collection<BossBar> collection : barMap.values()) {
			/* 393 */ list.add((BossBar) ((List<BossBar>) collection).get(0));
		}
		/* 395 */ return list;
	}

	protected static void sendPacket(Player p, Object packet) {
		/* 399 */ if ((p == null) || (packet == null))
			throw new IllegalArgumentException("player and packet cannot be null");
		try {
			/* 401 */ Object handle = Reflection.getHandle(p);
			/* 402 */ Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
			/* 403 */ Reflection.getMethod(connection.getClass(), "sendPacket", new Class[] { Reflection.getNMSClass("Packet") }).invoke(connection, new Object[] { packet });
		} catch (Exception e) {
		}
	}

	static void validate1_9() {
		/* 409 */ if (!is1_9) {
			/* 410 */ throw new RuntimeException(new UnsupportedOperationException("This method is not compatible with versions < 1.9"));
		}
	}

	public void load() {
	}

	public void init(Plugin plugin) {
		Avalon.getPlguin().getServer().getPluginManager().registerEvents(this, Avalon.getPlguin());
		for (Player player : Bukkit.getOnlinePlayers()) {
			removeAllBars(player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		/* 453 */ removeBar(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent e) {
		/* 458 */ removeBar(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent e) {
		/* 463 */ if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			/* 464 */ handlePlayerTeleport(e.getPlayer(), e.getFrom(), e.getTo());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent e) {
		/* 470 */ if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			/* 471 */ handlePlayerTeleport(e.getPlayer(), e.getPlayer().getLocation(), e.getRespawnLocation());
		}
	}

	protected void handlePlayerTeleport(Player player, Location from, Location to) {
		if (!hasBar(player))
			return;
		final BossBar bar = getBossBar(player);
		bar.setVisible(false);
		new BukkitRunnable() {
			public void run() {
				bar.setVisible(true);
			}
		}.runTaskLater(Avalon.getPlguin(), 2L);
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			final BossBar bar = getBossBar(e.getPlayer());
			if (bar != null) {
				new BukkitRunnable() {
					public void run() {
						if (!e.getPlayer().isOnline())
							return;
						bar.updateMovement();
					}
				}.runTaskLater(Avalon.getPlguin(), 0L);
			}
		}
	}
}