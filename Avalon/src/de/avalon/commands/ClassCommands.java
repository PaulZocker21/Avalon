package de.avalon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.avalon.mmo.Clazz;
import de.avalon.mmo.PriestClazz;
import de.avalon.player.GUI;
import de.avalon.player.Hero;

public class ClassCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Du musst ein Spieler sein!");
		}
		if (command.getName().equalsIgnoreCase("class")) {
			Player player = (Player) sender;
			Hero hero = Hero.getHero(player);
			if (hero == null)
				hero = Hero.create(player);
			if (args.length == 0) {
				player.sendMessage(hero.getSelectetClass() == null ? "§cDu hast keine Klasse ausgewhlt!" : "§7Deine akltuell ausgewählte Klass ist §6" + hero.getSelectetClass().getName());
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					hero.getClasses().values().forEach(clazz -> {
						player.sendMessage("§7Name:§6 " + clazz.getName() + " §7Level: §6" + clazz.getLevel() + " §7Exp:§6 " + clazz.getExp());
					});
				} else {
					player.sendMessage("§cDieser Command existiert nicht!");
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("change")) {
					Clazz clazz = hero.getClass(args[1]);
					if (clazz == null) {
						player.sendMessage("§cDiese Klasse existiert nicht!");
						return true;
					}
					hero.setSelectetClass(clazz);
					hero.getGui().setBossBar(GUI.BOSS_BAR_LEVEL);
					player.sendMessage("§aDu hast die Klasse§6 " + clazz.getName() + " §aausgewählt!");
				} else if (args[0].equalsIgnoreCase("create")) {
					if (args[1].equalsIgnoreCase("Priester")) {
						PriestClazz priest = new PriestClazz(hero);
						if (hero.getClasses().containsKey(priest.getName())) {
							player.sendMessage("§cDu hast bereits eine Priesterklasse!");
							return true;
						}
						hero.addClass(priest);
						hero.setSelectetClass(priest);
						hero.getGui().setBossBar(GUI.BOSS_BAR_LEVEL);
						player.sendMessage("§aDu hast erfolgreich die Klasse §6" + priest.getName() + "§a erstellt!");
					} else {
						player.sendMessage("§cDiese Klasse existiert nicht!");
					}
				} else {
					player.sendMessage("§cDieses Command existiert nicht!");
				}
			} else {
				player.sendMessage("§cZu viele Argumente!");
			}
			return true;
		}
		return false;
	}

}
