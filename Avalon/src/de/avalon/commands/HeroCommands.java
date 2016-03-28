package de.avalon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.avalon.player.Hero;

public class HeroCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Du musst ein Spieler sein!");
			return true;
		}
		if (command.getName().equalsIgnoreCase("hero")) {
			Player player = (Player) sender;
			Hero hero = Hero.getHero(player);
			if (hero == null)
				hero = Hero.create(player);
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("stats")) {
					player.sendMessage("§3Ausgewählte Klasse: " + (hero.getSelectetClass() == null ? "§ckeine ausgewählte Klasse" : "§6" + hero.getSelectetClass().getName()));
					player.sendMessage("§3Mining §7Level: §6" + hero.getMining().getLevel() + " §7Exp: §6" + hero.getMining().getExp() + "/" + hero.getMining().calculateMaxExp(hero.getMining().getLevel() + 1));
					player.sendMessage("§3Forest §7Level: §6" + hero.getForest().getLevel() + " §7Exp: §6" + hero.getForest().getExp() + "/" + hero.getForest().calculateMaxExp(hero.getForest().getLevel() + 1));
					player.sendMessage("§3Digging §7Level: §6" + hero.getDigging().getLevel() + " §7Exp: §6" + hero.getDigging().getExp() + "/" + hero.getDigging().calculateMaxExp(hero.getDigging().getLevel() + 1));
				} else {
					player.sendMessage("§cDieser Command existiert nicht!");
				}
			} else {
				player.sendMessage("§cFalsche Argumentszahl!");
			}
			return true;
		}
		return false;
	}

}
