package io.github.trimill.redstoneprobe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class CommandProbe implements CommandExecutor {

	public static Location argsToLocation(Player player, String sx, String sy, String sz) {
		if(player == null) return null;
		try {
			double dx = Double.parseDouble(sx);
			double dy = Double.parseDouble(sy);
			double dz = Double.parseDouble(sz);
			return new Location(player.getWorld(), dx, dy, dz);
		} catch(NumberFormatException ex) {
			player.sendMessage(ChatColor.RED + "Incorrect arguments: " + sx + " "  + sy + " " + sz);
			return null;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("redstoneprobe.command.probe")) {
				player.sendMessage(Utils.NO_PERMS);
				return true;
			}
			if(args.length == 0) {
				if(!player.hasPermission("redstoneprobe.probe")) {
					player.sendMessage(Utils.NO_PROBE_ITEM);
					return true;
				}
				ItemStack probe = new ItemStack(Material.SOUL_TORCH);
				ItemMeta meta = probe.getItemMeta();
				meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Probe");
				meta.setLore(new ArrayList<>(Collections.singleton("Use on redstone dust")));
				meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
				probe.setItemMeta(meta);
				player.getInventory().addItem(probe);
			} else if(args.length == 1 || args.length == 2) {
				if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls")) {
					// List all probes
					Set<Location> probes = Utils.getAllProbes(player);
					if(probes.isEmpty()) {
						player.sendMessage(Utils.CHAT_PREFIX + ChatColor.RED + "No probes set.");
						return true;
					} else {
						player.sendMessage(Utils.CHAT_PREFIX + String.format("%d probes:", probes.size()));
						String fstring = ChatColor.YELLOW + "%s" + ChatColor.RESET + " at " + ChatColor.AQUA + "(%d,%d,%d)";
						for(Location loc: probes) {
							player.sendMessage(String.format(fstring,
									loc.getBlock().getType().name().toLowerCase(),
									loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
							));
						}
					}
				}
				else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("cl")) {
						// Clear all probes
						Utils.removeAllProbes(player);
						player.sendMessage(Utils.CHAT_PREFIX + "Cleared all probes.");
					} else if(args[0].equalsIgnoreCase("hide") || args[0].equalsIgnoreCase("h")) {
						// Hide chat messages
						Utils.ignoringMessages.add(player.getUniqueId());
						player.sendMessage(Utils.CHAT_PREFIX + "Probe messages disabled.");
					} else if(args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("s")) {
						// Show chat messages
						Utils.ignoringMessages.remove(player.getUniqueId());
						player.sendMessage(Utils.CHAT_PREFIX + "Probe messages enabled.");
					} else if(args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
						// Show help message
						player.sendMessage(Utils.PROBE_HELP);
					} else return false;
				} else return false;
			} else if(args.length == 4) {
				if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
					// Add probe by coordinates
					Location loc = argsToLocation(player, args[1], args[2], args[3]);
					if(loc != null) {
						Utils.addWithMessage(player, loc);
						return true;
					}
				} else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
					// Remove probe by coordinates
					Location loc = argsToLocation(player, args[1], args[2], args[3]);
					if(loc != null) {
						Utils.removeWithMessage(player, loc);
						return true;
					}
				} else return false;
			} else return false;
		} else {
			sender.sendMessage(ChatColor.RED + "This command must be run by a player.");
			return false;
		}
		return true;
	}
}
