package com.github.dsh105.echopet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.dsh105.echopet.EchoPet;

public enum HelpPage {
	NONE(0,			ChatColor.RED + "No help page found."),
	
	GENERAL(1,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " <type>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Changes your current pet.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " <type>:[data],[data];[name <mount>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Spawns a pet by your side with the specified mount.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type> and echopet.pet.type.<mount>",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " name <name>",
					ChatColor.YELLOW + "    - Set the name tag of your pet.",
					ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.name",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " remove",
					ChatColor.YELLOW + "    - Remove your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),
	
	MOUNT(2, 		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " mount <type>:[data],[data];[name]",
					ChatColor.YELLOW + "    - Changes the mount type of your current pet.",
					ChatColor.YELLOW + "    - Each data value is separated by a comma.",
					ChatColor.YELLOW + "    - Pet names can be entered using a semi-colon.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.type.<type>",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " name mount <name>",
					ChatColor.YELLOW + "    - Set the name tag of your pet's mount.",
					ChatColor.YELLOW + "    - Names can be more than one word, but no longer than 64 characters.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.name",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " mount remove",
					ChatColor.YELLOW + "    - Remove your pet's current mount.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.remove"),
	
	DEFAULT(3,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " list",
					ChatColor.YELLOW + "    - Lists available pet types.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.list",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " info",
					ChatColor.YELLOW + "    - Provides info on your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.info",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " default set <type>:[data],[data] [mount]:[data],[data]",
					ChatColor.YELLOW + "    - Set the default pet for when you log in.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.type.<type> and echopet.pet.default.set.type.<mount>",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " default set current",
					ChatColor.YELLOW + "    - Set the default pet to your current pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.set.current",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " default remove",
					ChatColor.YELLOW + "    - Remove your default pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.default.remove"),
					
	SKILLS(4,		ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " ride",
					ChatColor.YELLOW + "    - Ride your pet.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.ride",
					
					ChatColor.GOLD + "/" + EchoPet.getPluginInstance().cmdString + " hat",
					ChatColor.YELLOW + "    - Have your pet ride on your head.",
					ChatColor.YELLOW + "    - Appears higher to the owner to prevent sight obstruction.",
					ChatColor.DARK_RED + "    - Permission: echopet.pet.hat");
	private int id;
	private String[] lines;
	HelpPage(int id, String... lines) {
		this.id = id;
		this.lines = lines;
	}
	
	public String[] getLines() {
		return this.lines;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static String[] getHelpPage(int i) {
		for (HelpPage hp : HelpPage.values()) {
			if (hp.getId() == i) {
				return hp.getLines();
			}
		}
		return HelpPage.NONE.getLines();
	}
	
	public static boolean sendRelevantHelpMessage(CommandSender sender, String[] args) {
		if (args[0].equalsIgnoreCase("default")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
			for (String s : HelpPage.DEFAULT.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("name")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
			for (String s : HelpPage.GENERAL.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("mount")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
			for (String s : HelpPage.MOUNT.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage(ChatColor.RED + "------------ EchoPet Help ------------");
			for (String s : HelpPage.GENERAL.getLines()) {
				sender.sendMessage(s);
			}
			return true;
		}
		return false;
	}
}