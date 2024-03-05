package me.emmy.hub.listener;

import me.emmy.hub.HubCore;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;

public class DoubleJumpListener implements Listener {

    private final Plugin plugin;
    private boolean enabled;
    private boolean soundEnabled;
    private Sound sound;
    private double velocityMultiplier;

    public DoubleJumpListener(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        ConfigurationSection configSection = HubCore.instance.getConfig().getConfigurationSection("double_jump");

        if (configSection != null) {
            enabled = configSection.getBoolean("enabled", true);
            soundEnabled = configSection.getBoolean("sound_enabled", true);
            sound = Sound.valueOf(configSection.getString("sound", "FIREWORK_LAUNCH"));
            velocityMultiplier = configSection.getDouble("velocity_multiplier", 1.5);
        }
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (enabled && player.getGameMode().name().equalsIgnoreCase("SURVIVAL") && !player.isFlying()) {
            event.setCancelled(true);

            player.setFlying(false);
            player.setAllowFlight(false);

            player.setVelocity(player.getLocation().getDirection().multiply(velocityMultiplier).setY(1));

            if (soundEnabled) {
                float volume = 0.5F;
                float pitch = 2.0F;
                player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
            }

            player.setAllowFlight(true);
        }
    }
}
