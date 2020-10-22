package io.github.trimill.redstoneprobe;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneProbe extends JavaPlugin {

	@Override
	public void onEnable() {
		Utils.server = getServer();
		getCommand("probe").setExecutor(new CommandProbe());
		getCommand("probectl").setExecutor(new CommandProbeCTL());
		getServer().getPluginManager().registerEvents(new ProbeListener(), this);
	}

	@Override
	public void onDisable() {}
}
