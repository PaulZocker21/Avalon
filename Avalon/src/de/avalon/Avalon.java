package de.avalon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Avalon extends JavaPlugin {

	private static Avalon instance;

	@Override
	public void onEnable() {
		instance = this;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	public static Avalon getPlguin() {
		return instance;
	}

}
