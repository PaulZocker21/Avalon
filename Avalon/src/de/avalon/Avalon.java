package de.avalon;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.avalon.listener.HeroListener;
import de.avalon.listener.SkillListener;
import de.avalon.player.Hero;

public class Avalon extends JavaPlugin {

	private static Avalon instance;
	private final File player_file = new File(getDataFolder(), "/players.yml");

	@Override
	public void onEnable() {
		instance = this;

		getServer().getPluginManager().registerEvents(new HeroListener(), this);
		getServer().getPluginManager().registerEvents(new SkillListener(), this);
		
		Hero.loadAll(player_file);
	}

	@Override
	public void onDisable() {

		Hero.saveAll(player_file);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	public static Avalon getPlguin() {
		return instance;
	}

}
