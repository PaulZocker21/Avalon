package de.avalon.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Scoreboard;

import de.avalon.bossbar.BossBar;
import de.avalon.bossbar.BossBarAPI;
import de.avalon.bossbar.BossBarAPI.Color;
import de.avalon.bossbar.BossBarAPI.Style;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class GUI {

	private static HashMap<Entity, ArrayList<Hero>> attackers = new HashMap<>();

	public static HashMap<Entity, ArrayList<Hero>> getAttackers() {
		return attackers;
	}

	public static void addAttacker(Entity entity, Hero hero) {
		ArrayList<Hero> attackers = getAttackers(entity);
		if (attackers == null) {
			attackers = new ArrayList<Hero>();
		}
		attackers.add(hero);
		GUI.attackers.put(entity, attackers);
	}

	public static void remove(Entity entity) {
		attackers.remove(entity);
	}

	public static ArrayList<Hero> getAttackers(Entity entity) {
		return attackers.get(entity);
	}

	public static final int BOSS_BAR_LEVEL = 1;
	public static final int BOSS_BAR_COMBAT = 2;

	private Hero hero;
	private BossBar bossbar;
	private Scoreboard scoreboard;
	private int bossBarState;

	public GUI(Hero hero) {
		this.hero = hero;
	}

	public void init() {
		if (hero.getBukkitPlayer() != null) {
			String text = "";
			Color color = null;
			Style style = null;
			float progress = 0;
			if (bossBarState == BOSS_BAR_LEVEL) {
				int level = hero.getSelectetClass().getLevel();
				int exp = hero.getSelectetClass().getExp();
				int maxExp = hero.getSelectetClass().calculateMaxExp(level + 1);
				progress = (float) ((float) exp / (float) maxExp);
				text = "Level: §6" + level + "§f Experience: §6" + exp + "§f/§c" + maxExp;
				color = Color.BLUE;
				style = Style.PROGRESS;
			} else {
				setBossBar(BOSS_BAR_LEVEL);
				return;
			}
			this.bossbar = BossBarAPI.addBar(hero.getBukkitPlayer(), new TextComponent(text), color, style, progress);
		}
	}

	public void update(Object... obj) {
		setBossBar(bossBarState, obj);
	}

	public void setBossBar(int state, Object... obj) {
		bossBarState = state;
		if (bossbar == null) {
			init();
		}
		if (state == BOSS_BAR_LEVEL) {
			int level = hero.getSelectetClass().getLevel();
			int exp = hero.getSelectetClass().getExp();
			int maxExp = hero.getSelectetClass().calculateMaxExp(level + 1);

			String text = "Level: §6" + level + "§f Experience: §6" + exp + "§f/§c" + maxExp;
			Color color = Color.BLUE;
			Style style = Style.PROGRESS;
			float progress = (float) ((float) exp / (float) maxExp);

			bossbar.setMessage(ComponentSerializer.toString(new TextComponent(text)));
			bossbar.setColor(color);
			bossbar.setStyle(style);
			bossbar.setProgress(progress);
		} else if (state == BOSS_BAR_COMBAT) {
			if (obj.length == 0)
				return;
			if (obj[0] instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) obj[0];

				String text = entity.getCustomName() == null ? entity.getName() : entity.getCustomName();
				Color color = Color.RED;
				Style style = Style.NOTCHED_20;
				float progress = (float) (entity.getHealth() / entity.getMaxHealth());

				bossbar.setMessage(ComponentSerializer.toString(new TextComponent(text)));
				bossbar.setColor(color);
				bossbar.setStyle(style);
				bossbar.setProgress(progress);
			}
		}
	}

	public BossBar getBossbar() {
		return bossbar;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

}
