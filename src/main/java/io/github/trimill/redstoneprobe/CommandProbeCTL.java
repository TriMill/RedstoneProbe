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
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("dump")) {
				sender.getServer().getConsoleSender().sendMessage(stringifyData());
				sender.sendMessage(ChatColor.GREEN + "Data dumped to console.");
				return true;
			} else if(args[0].equalsIgnoreCase("clear")) {
				if(args.length > 1) {
					Player player = sender.getServer().getPlayer(args[1]);
					if(player == null) {
						sender.sendMessage(ChatColor.RED + "No player found.");
						return true;
					} else {
						RedstoneProbe.removeAllProbes(player);
						sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " cleared.");
						return true;
					}
				} else {
					RedstoneProbe.resetProbeList();
					sender.sendMessage(ChatColor.GREEN + "Reset all probes.");
					return true;
				}
			}
		}
		return false;
	}

	private String stringifyData() {
		String res = "RedstoneProbe data:\n";
		for(Location loc: RedstoneProbe.activeProbes.keySet()) {
			res = res + "{" + loc.toString() + ": ";
			res += RedstoneProbe.activeProbes.get(loc).toString();
			res += "}\n";
		}
		return res;
	}
}