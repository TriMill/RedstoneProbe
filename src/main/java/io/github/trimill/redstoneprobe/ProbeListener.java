package io.github.trimill.redstoneprobe;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class ProbeListener implements Listener {

	public static void sendAddMessage(Player player, Location loc) {
		String message = String.format(
				"Probe set for block " + ChatColor.WHITE + "(%d,%d,%d).",
				loc.getBlockX(),
				loc.getBlockY(),
				loc.getBlockZ()
		);
		player.sendMessage(RedstoneProbe.CHAT_PREFIX + message);
		player.spawnParticle(Particle.FLAME, loc.toCenterLocation(), 12, 0, 0.5, 0, 0.01);
	}

	public static void sendRemoveMessage(Player player, Location loc) {
		player.sendMessage(RedstoneProbe.CHAT_PREFIX + "Removed probe for block.");
		player.spawnParticle(Particle.SMOKE_NORMAL, loc.toCenterLocation(), 12, 0, 0.5, 0, 0.01);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Block block = e.getClickedBlock();
		if(block != null &&
				item != null && item.getType().equals(Material.SOUL_TORCH) && item.hasItemFlag(ItemFlag.HIDE_PLACED_ON)
		) {
			Player player = e.getPlayer();
			UUID uuid = player.getUniqueId();
			Location loc = e.getClickedBlock().getLocation();
			if(RedstoneProbe.existsProbe(player, loc)) {
				RedstoneProbe.removeProbe(player, loc);
				sendRemoveMessage(player, loc);
			} else {
				RedstoneProbe.addProbe(player, loc);
				sendAddMessage(player, loc);
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRedstoneEvent(BlockRedstoneEvent e) {
		if(e.getOldCurrent() > 0 && e.getNewCurrent() > 0) return;
		Location loc = e.getBlock().getLocation();
		if(RedstoneProbe.activeProbes.containsKey(loc)) {
			Set<Player> players = RedstoneProbe.activeProbes.get(loc);
			String change = (e.getOldCurrent() == 0) ? ChatColor.GREEN + "on" + ChatColor.RESET : ChatColor.RED + "off" + ChatColor.RESET ;
			String fmessage = "%s(%d,%d,%d)%s %s%s turned %s (tick %s%d%s)";
			fmessage = String.format(fmessage,
					ChatColor.AQUA, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
					ChatColor.YELLOW, e.getBlock().getType().name(), ChatColor.RESET,
					change,
					ChatColor.YELLOW, RedstoneProbe.SERVER.getCurrentTick() % 20, ChatColor.RESET);
			for(Player player: players) {
				if(!RedstoneProbe.ignoringMessages.contains(player)) {
					player.sendMessage(fmessage);
				}
			}
		}
	}
}
