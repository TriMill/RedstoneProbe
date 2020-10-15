package io.github.trimill.redstoneprobe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RedstoneProbe extends JavaPlugin {
	public static final String CHAT_PREFIX = ChatColor.WHITE + "[" + ChatColor.AQUA + "Probe" + ChatColor.WHITE + "] " + ChatColor.AQUA;
	public static Map<Location, Set<Player>> activeProbes = new HashMap<>();
	public static Set<Player> ignoringMessages = new HashSet<>();
	public static Server SERVER;

	@Override
	public void onEnable() {
		SERVER = getServer();
		getCommand("probe").setExecutor(new CommandProbe());
		getCommand("probectl").setExecutor(new CommandProbeCTL());
		getServer().getPluginManager().registerEvents(new ProbeListener(), this);
	}

	@Override
	public void onDisable() {}

	public static boolean existsProbe(Player player, Location loc) {
		if(!activeProbes.containsKey(loc)) return false;
		return activeProbes.get(loc).contains(player);
	}

	public static void addProbe(Player player, Location loc) {
		if(activeProbes.containsKey(loc)) {
			activeProbes.get(loc).add(player);
		} else {
			Set<Player> s = new HashSet<>();
			s.add(player);
			activeProbes.put(loc, s);
		}
	}

	public static boolean removeProbe(Player player, Location loc) {
		if(activeProbes.containsKey(loc)) {
			Set<Player> players = activeProbes.get(loc);
			boolean ret = players.remove(player);
			if(players.size() == 0) {
				activeProbes.remove(loc);
				return true;
			}
			return ret;
		}
		return false;
	}

	public static boolean removeAllProbes(Player player) {
		boolean ret = false;
		Set<Location> staticProbes = new HashSet<>(activeProbes.keySet());
		for(Location loc: staticProbes) {
			Set<Player> players = activeProbes.get(loc);
			boolean result = players.remove(player);
			if(players.size() == 0) {
				activeProbes.remove(loc);
				result = true;
			}
			ret = ret || result;
		}
		return ret;
	}

	public static void resetProbeList() {
		activeProbes.clear();
	}
}
