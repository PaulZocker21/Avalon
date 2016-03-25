package de.avalon.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class BossBar {

	public static Class<?> PacketPlayOutBoss = ReflectionUtils.getClass(ReflectionUtils.PackageType.MINECRAFT_SERVER + ".PacketPlayOutBoss");
	public static Class<?> BossBattle = ReflectionUtils.getClass(ReflectionUtils.PackageType.MINECRAFT_SERVER + ".BossBattle");
	public static Class<?> BarColor = ReflectionUtils.getClass(BossBattle.getName() + "$BarColor");
	public static Class<?> BarStyle = ReflectionUtils.getClass(BossBattle.getName() + "$BarStyle");
	public static Class<?> PacketAction = ReflectionUtils.getClass(PacketPlayOutBoss.getName() + "$Action");
	public static Class<?> IChatBaseComponent = ReflectionUtils.getClass(ReflectionUtils.PackageType.MINECRAFT_SERVER + ".IChatBaseComponent");
	public static Class<?> ChatSerializer = ReflectionUtils.getClass(IChatBaseComponent.getName() + "$ChatSerializer");

	public static ArrayList<BossBar> bossbars = new ArrayList<>();

	public static void clearBossBars() {
		bossbars.forEach(bar -> bar.update(Action.REMOVE));
	}

	private ArrayList<Player> players;

	private String text;
	private Color color;
	private Style style;
	private float progress;
	private boolean alreadyAdd;
	private UUID id;

	public BossBar() {
		this.players = new ArrayList<>();
		
		bossbars.add(this);
	}

	public BossBar(String text, Color color, Style style, float progress) {
		this.players = new ArrayList<>();
		this.text = text;
		this.color = color;
		this.style = style;
		this.progress = progress;
		this.id = UUID.randomUUID();

		bossbars.add(this);
	}

	public void update(Action action) {
		if (action == Action.ADD && alreadyAdd)
			return;
		try {
			Object packet = createPacket(action);
			if (action == Action.ADD)
				alreadyAdd = true;
			if (action == Action.REMOVE && !alreadyAdd) {
				return;
			}
			players.forEach(player -> ReflectionUtils.sendPacket(player, packet));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		if (!alreadyAdd) {
			update(Action.ADD);
		}
		update(Action.UPDATE_HEALTH);
		update(Action.UPDATE_NAME);
		update(Action.UPDATE_PROPERTIES);
		update(Action.UPDATE_STYLE);
	}

	public String getText() {
		return text;
	}

	public void setMessage(String text) {
		this.text = text;
		update(Action.UPDATE_NAME);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		update(Action.UPDATE_STYLE);
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
		update(Action.UPDATE_STYLE);
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
		update(Action.UPDATE_HEALTH);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		getPlayers().add(player);
		update();
	}

	private Object createPacket(Action action) {
		try {
			Object packet = PacketPlayOutBoss.newInstance();
			ReflectionUtils.setValue(packet, true, "a", id);
			ReflectionUtils.setValue(packet, true, "b", PacketAction.getEnumConstants()[action.ordinal()]);
			ReflectionUtils.setValue(packet, true, "c", ChatSerializer.getMethod("a", new Class[] { String.class }).invoke(ChatSerializer.newInstance(), new Object[] { "{\"text\":\"" + text + "\"}" }));
			ReflectionUtils.setValue(packet, true, "d", progress);
			ReflectionUtils.setValue(packet, true, "e", BarColor.getEnumConstants()[color.ordinal()]);
			ReflectionUtils.setValue(packet, true, "f", BarStyle.getEnumConstants()[style.ordinal()]);
			return packet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public enum Action {
		ADD("ADD"), REMOVE("REMOVE"), UPDATE_HEALTH("UPDATE_PCT"), UPDATE_NAME("UPDATE_NAME"), UPDATE_STYLE("UPDATE_STYLE"), UPDATE_PROPERTIES("UPDATE_PROPERTIES");
		private String name;

		Action(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum Style {
		PROGRESS(0, "PROGRESS"), NOTCHED_6(1, "NOTCHED_6"), NOTCHED_10(2, "NOTCHED_10"), NOTCHED_12(3, "NOTCHED_12"), NOTCHED_20(4, "NOTCHED_20");

		private int id;
		private String name;

		Style(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public enum Color {
		PINK(0, "PINK"), BLUE(1, "BLUE"), RED(2, "RED"), GREEN(3, "GREEN"), YELLOW(4, "YELLOW"), PURPLE(5, "PURPLE"), WHITE(6, "WHITE");

		private int id;
		private String name;

		Color(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

}
