package io.github.trimill.redstoneprobe;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Utils {
	public static final String CHAT_PREFIX = ChatColor.WHITE + "[" + ChatColor.AQUA + "Probe" + ChatColor.WHITE + "] " + ChatColor.AQUA;
	public static final String NO_PERMS = CHAT_PREFIX + ChatColor.RED + "Insufficient permissions!";
	public static final String NO_PROBE_ITEM = CHAT_PREFIX + ChatColor.RED + "Probe item disabled. Use "
			+ ChatColor.GREEN + "/probe add " + ChatColor.RED + "or "
			+ ChatColor.GREEN + "/probe remove " + ChatColor.RED + "instead.";
	public static Server server;
	public static Map<Location, Set<Player>> activeProbes = new HashMap<>();
	public static Set<Player> ignoringMessages = new HashSet<>();

	private Utils(){}

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

	public static void addWithMessage(Player player, Location loc) {
		addProbe(player, loc);
		String message = String.format(
				"Probe set for block " + ChatColor.WHITE + "(%d,%d,%d).",
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ()
		);
		player.sendMessage(CHAT_PREFIX + message);
		player.spawnParticle(Particle.FLAME, loc.toCenterLocation(), 12, 0, 0.5, 0, 0.01);
	}

	public static void removeWithMessage(Player player, Location loc) {
		removeProbe(player, loc);
		player.sendMessage(CHAT_PREFIX + "Removed probe for block.");
		player.spawnParticle(Particle.SMOKE_NORMAL, loc.toCenterLocation(), 12, 0, 0.5, 0, 0.01);
	}
}