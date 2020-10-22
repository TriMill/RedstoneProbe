package io.github.trimill.redstoneprobe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandProbeCTL implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("redstoneprobe.command.probectl")) {
			sender.sendMessage(Utils.NO_PERMS);
			return true;
		}
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("dump")) {
				sender.getServer().getConsoleSender().sendMessage(stringifyData());
				sender.sendMessage(ChatColor.GREEN + "Data dumped to console.");
				return true;
			} else if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("cl")) {
				if(args.length > 1) {
					Player player = sender.getServer().getPlayer(args[1]);
					if(player == null) {
						sender.sendMessage(ChatColor.RED + "No player found.");
					} else {
						Utils.removeAllProbes(player);
						sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " cleared.");
					}
				} else {
					Utils.resetProbeList();
					sender.sendMessage(ChatColor.GREEN + "Reset all probes.");
				}
				return true;
			} else if(args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
				// Show help message
				sender.sendMessage(Utils.PROBECTL_HELP);
				return true;
			}
		}
		return false;
	}

	private String stringifyData() {
		String res = "Probe data:\n";
		for(Location loc: Utils.activeProbes.keySet()) {
			res += "{" + loc.toString() + ", " + Utils.activeProbes.get(loc).toString() + "}\n";
		}
		res += "Probe timing data:\n";
		for(Location loc: Utils.timeSinceChange.keySet()) {
			res += "{" + loc.toString() + ", " + Utils.timeSinceChange.get(loc).toString() + "}\n";
		}
		return res;
	}
}
