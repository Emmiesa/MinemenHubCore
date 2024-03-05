package me.emmy.hub.listener;

import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EnderButtListener implements Listener {

	@EventHandler
	public void onPearl(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {
				event.setCancelled(true);
				if (player.isInsideVehicle()) {
					player.getVehicle().remove();
				}
				event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
				event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
				EnderPearl pearl = player.launchProjectile(EnderPearl.class);
				pearl.setPassenger(player);
				pearl.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5F));
				player.spigot().setCollidesWithEntities(false);
				player.updateInventory();
			}
		}
	}

	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		event.getDismounted().remove();
	}
}