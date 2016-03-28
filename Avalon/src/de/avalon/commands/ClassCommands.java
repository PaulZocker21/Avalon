package de.avalon.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.avalon.menu.Menu;
import de.avalon.menu.MenuItem;
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
			final Hero hero = Hero.getHero(player);
			if (args.length == 0) {
				player.sendMessage(hero.getSelectetClass() == null ? "§cDu hast keine Klasse ausgewhlt!" : "§7Deine akltuell ausgewählte Klass ist §6" + hero.getSelectetClass().getName());
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					hero.getClasses().values().forEach(clazz -> {
						player.sendMessage("§7Name:§6 " + clazz.getName() + " §7Level: §6" + clazz.getLevel() + " §7Exp:§6 " + clazz.getExp());
					});
				} else if (args[0].equalsIgnoreCase("select")) {
					Menu menu = new Menu(hero.getName() + "_classSelect", "§7Wähle eine §6Klasse§7 aus", 3);
					for (Clazz clazz : hero.getClasses().values()) {
						byte data = 14;
						if (hero.getSelectetClass() != null && hero.getSelectetClass().getName().equals(clazz.getName())) {
							data = 13;
						}
						ItemStack itemStack = new ItemStack(Material.WOOL, 1, data);
						ItemMeta meta = itemStack.getItemMeta();
						meta.setDisplayName("§3" + clazz.getName());
						List<String> lore = new ArrayList<String>();
						lore.add("§7Klassenname: §6" + clazz.getName());
						lore.add("§7Level: §6" + clazz.getLevel());
						lore.add("§7Exp: §6" + clazz.getExp() + "/" + clazz.calculateMaxExp(clazz.getLevel() + 1));
						meta.setLore(lore);
						itemStack.setItemMeta(meta);
						MenuItem item = menu.addItem(itemStack, 1 * 8 + 2);
						item.setAutoClose(true);
						item.addClickListener(e -> {
							String name = e.getCurrentItem().getItemMeta().getDisplayName();
							Clazz c = hero.getClass(ChatColor.stripColor(name));
							if (c != null) {
								System.out.println(ChatColor.stripColor(name));
								((Player) e.getWhoClicked()).performCommand("class change " + c.getName());
							}
						});
					}
					menu.openInventory(player);
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
