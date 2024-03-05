package me.emmy.hub;

import dev.hely.tab.Tab;
import me.emmy.hub.commands.HubCoreCommand;
import me.emmy.hub.scoreboard.ScoreboardLayout;
import me.emmy.hub.listener.DoubleJumpListener;
import me.emmy.hub.scoreboard.TablistAdapter;
import me.emmy.hub.utils.CC;
import me.emmy.hub.utils.assemble.Assemble;
import me.emmy.hub.utils.assemble.AssembleStyle;
import me.emmy.hub.commands.ServerSelectorCommand;
import me.emmy.hub.commands.SetSpawnCommand;
import me.emmy.hub.listener.EnderButtListener;
import me.emmy.hub.listener.PlayerListener;
import me.emmy.hub.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HubCore extends JavaPlugin {

	public static HubCore instance;
	private Location spawnLocation;
	public FileConfiguration hotbarConfig;
	public FileConfiguration menusConfig;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		saveResource("hotbar.yml", false);
		hotbarConfig = getConfig("hotbar.yml");
		saveResource("menus.yml", false);
		menusConfig = getConfig("menus.yml");

		loadSpawnLocation();
		loadConfigurations();

		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new EnderButtListener(), this);
		getServer().getPluginManager().registerEvents(new DoubleJumpListener(this), this);
		getCommand("hubcore").setExecutor(new HubCoreCommand());
		getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
		getCommand("serverselector").setExecutor(new ServerSelectorCommand());

		if (getConfig().getBoolean("scoreboard.enabled")) {
			Assemble assemble = new Assemble(this, new ScoreboardLayout());
			assemble.setAssembleStyle(AssembleStyle.MODERN);
			assemble.setTicks(2);
		}

		if (HubCore.instance.getConfig().getBoolean("tablist.enable")) {
			new Tab(this, new TablistAdapter());
		}

		if(!getDescription().getAuthors().contains("Emmy")) {
			Bukkit.getPluginManager().disablePlugin(this);
		}

		if (!getDescription().getName().contains("HubCore")) {
			Bukkit.getPluginManager().disablePlugin(this);
		}

		Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
		Bukkit.getConsoleSender().sendMessage(CC.translate(" &d&lMinemen Club &7| &eHubCore replica by Emmy!"));
		Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
	}

	@Override
	public void onDisable() {
	}

	public static HubCore get() {
		return instance;
	}

	private void loadSpawnLocation() {
		FileConfiguration config = getConfig();
		boolean enableSpawnTeleport = config.getBoolean("spawn-teleport", true);

		if (enableSpawnTeleport && config.contains("spawnLocation.world")) {
			World world = Bukkit.getWorld(config.getString("spawnLocation.world"));
			double x = config.getDouble("spawnLocation.x");
			double y = config.getDouble("spawnLocation.y");
			double z = config.getDouble("spawnLocation.z");
			float yaw = (float) config.getDouble("spawnLocation.yaw");
			float pitch = (float) config.getDouble("spawnLocation.pitch");

			spawnLocation = new Location(world, x, y, z, yaw, pitch);
		}
	}

	private void loadConfigurations() {
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location location) {
		this.spawnLocation = location;

		getConfig().set("spawnLocation.world", location.getWorld().getName());
		getConfig().set("spawnLocation.x", location.getX());
		getConfig().set("spawnLocation.y", location.getY());
		getConfig().set("spawnLocation.z", location.getZ());
		getConfig().set("spawnLocation.yaw", location.getYaw());
		getConfig().set("spawnLocation.pitch", location.getPitch());

		saveConfig();
	}

	public void reloadAllConfigs() {
		hotbarConfig = getConfig("hotbar.yml");
		menusConfig = getConfig("menus.yml");
	}

	public FileConfiguration getConfig(String fileName) {
		File configFile = new File(getDataFolder(), fileName);
		return YamlConfiguration.loadConfiguration(configFile);
	}
}