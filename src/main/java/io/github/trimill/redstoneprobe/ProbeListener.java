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

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Block block = e.getClickedBlock();
		if(block != null &&
				item != null && item.getType().equals(Material.SOUL_TORCH) && item.hasItemFlag(ItemFlag.HIDE_PLACED_ON)
		) {
			e.setCancelled(true);
			Player player = e.getPlayer();
			if(!player.hasPermission("redstoneprobe.probe")) {
				if(player.hasPermission("redstoneprobe.command.probe"))
					player.sendMessage(Utils.NO_PROBE_ITEM);
				else
					player.sendMessage(Utils.NO_PERMS);
				return;
			}
			UUID uuid = player.getUniqueId();
			Location loc = e.getClickedBlock().getLocation();
			if(Utils.existsProbe(player, loc)) {
				Utils.removeWithMessage(player, loc);
			} else {
				Utils.addWithMessage(player, loc);
			}
		}
	}

	@EventHandler
	public void onRedstoneEvent(BlockRedstoneEvent e) {
		if(e.getOldCurrent() > 0 && e.getNewCurrent() > 0) return;
		Location loc = e.getBlock().getLocation();
		if(Utils.activeProbes.containsKey(loc)) {
			Set<Player> players = Utils.activeProbes.get(loc);
			String change = (e.getOldCurrent() == 0) ? ChatColor.GREEN + "on" + ChatColor.RESET : ChatColor.RED + "off" + ChatColor.RESET ;
			String fmessage = "%s(%d,%d,%d)%s %s%s turned %s (tick %s%d%s)";
			fmessage = String.format(fmessage,
					ChatColor.AQUA, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
					ChatColor.YELLOW, e.getBlock().getType().name(), ChatColor.RESET,
					change,
					ChatColor.YELLOW, Utils.server.getCurrentTick() % 20, ChatColor.RESET);
			for(Player player: players) {
				if(!Utils.ignoringMessages.contains(player)) {
					player.sendMessage(fmessage);
				}
			}
		}
	}
}
