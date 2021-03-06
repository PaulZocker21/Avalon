package de.avalon;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.avalon.commands.ClassCommands;
import de.avalon.commands.HeroCommands;
import de.avalon.listener.HeroListener;
import de.avalon.listener.MenuListener;
import de.avalon.listener.SkillListener;
import de.avalon.mmo.Chest_Avalon;
import de.avalon.player.Hero;
import de.avalon.utils.BossBar;

public class Avalon extends JavaPlugin {

	private static Avalon instance;
	private final File player_file = new File(getDataFolder(), "/players.yml");

	public static HashMap<Location, Chest_Avalon> chests = new HashMap<Location, Chest_Avalon>();
	public static Random random = new Random();

	@Override
	public void onEnable() {
		instance = this;

		getServer().getPluginManager().registerEvents(new HeroListener(), this);
		getServer().getPluginManager().registerEvents(new SkillListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);

		getCommand("class").setExecutor(new ClassCommands());
		getCommand("hero").setExecutor(new HeroCommands());

		Hero.loadAll(player_file);
	}

	@Override
	public void onDisable() {

		Hero.saveAll(player_file);

		BossBar.clearBossBars();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	public static Avalon getPlguin() {
		return instance;
	}

}
