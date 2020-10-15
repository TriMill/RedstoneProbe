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
			// UUID uuid = player.getUniqueId();
			if(args.length == 0) {
				ItemStack probe = new ItemStack(Material.SOUL_TORCH);
				ItemMeta meta = probe.getItemMeta();
				meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Probe");
				meta.setLore(new ArrayList<>(Collections.singleton("Use on redstone dust")));
				meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
				probe.setItemMeta(meta);
				player.getInventory().addItem(probe);
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("clear")) {
					// Clear all probes
					RedstoneProbe.removeAllProbes(player);
					player.sendMessage(RedstoneProbe.CHAT_PREFIX + "Cleared all probes.");
				} else if(args[0].equalsIgnoreCase("hide")) {
					// Hide chat messages
					RedstoneProbe.ignoringMessages.add(player);
					player.sendMessage(RedstoneProbe.CHAT_PREFIX + "Probe messages disabled.");
				} else if(args[0].equalsIgnoreCase("show")) {
					// Show chat messages
					RedstoneProbe.ignoringMessages.remove(player);
					player.sendMessage(RedstoneProbe.CHAT_PREFIX + "Probe messages enabled.");
				} else return false;
			} else if(args.length == 4) {
				if(args[0].equalsIgnoreCase("add")) {
					// Add probe by coordinates
					Location loc = argsToLocation(player, args[1], args[2], args[3]);
					if(loc != null) {
						RedstoneProbe.addProbe(player, loc);
						ProbeListener.sendAddMessage(player, loc);
						return true;
					}
				} else if(args[0].equalsIgnoreCase("remove")) {
					// Remove probe by coordinates
					Location loc = argsToLocation(player, args[1], args[2], args[3]);
					if(loc != null) {
						RedstoneProbe.removeProbe(player, loc);
						ProbeListener.sendRemoveMessage(player, loc);
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
