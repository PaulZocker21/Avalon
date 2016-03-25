package de.avalon.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import de.avalon.utils.BossBar;
import de.avalon.utils.BossBar.Color;
import de.avalon.utils.BossBar.Style;

public class GUI {

	public static final int BOSS_BAR_LEVEL = 1;
	public static final int BOSS_BAR_COMBAT = 2;

	public static final long COMBAT_TIME = 5 * 1000L;

	private Hero hero;
	private BossBar bossbar;
	private Scoreboard scoreboard;
	private int bossBarState;
	private BukkitTask combatTask;

	public GUI(Hero hero) {
		this.hero = hero;
	}

	public BukkitTask getCombatTask() {
		return combatTask;
	}

	public void setCombatTask(BukkitTask combatTask) {
		this.combatTask = combatTask;
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
			this.bossbar = new BossBar(text, color, style, progress);
			bossbar.addPlayer(hero.getBukkitPlayer());
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

			bossbar.setMessage(text);
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
				float progress = (float) ((float) entity.getHealth() / (float) entity.getMaxHealth());
				
				bossbar.setMessage(text);
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
