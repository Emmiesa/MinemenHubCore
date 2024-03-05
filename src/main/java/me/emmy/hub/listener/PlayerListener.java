package me.emmy.hub.listener;

import me.emmy.hub.HubCore;
import me.emmy.hub.menu.ServerSelectorMenu;
import me.emmy.hub.utils.CC;
import me.emmy.hub.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	private final HubCore plugin = HubCore.get();

	private final ItemStack enderbutt;
	private final ItemStack selector;

	public PlayerListener() {
		String endermaterial = plugin.getConfig("hotbar.yml").getString("items.enderbutt.material");
		String selectormaterial = plugin.getConfig("hotbar.yml").getString("items.server_selector.material");


		this.enderbutt = new ItemBuilder(Material.matchMaterial(endermaterial)).lore(plugin.getConfig("hotbar.yml").getStringList("items.enderbutt.lore")).name(HubCore.instance.getConfig("hotbar.yml").getString("items.enderbutt.name")).build();
		this.selector = new ItemBuilder(Material.matchMaterial(selectormaterial)).lore(plugin.getConfig("hotbar.yml").getStringList("items.server_selector.lore")).name(HubCore.instance.getConfig("hotbar.yml").getString("items.server_selector.name")).build();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.getInventory().clear();

		player.setGameMode(GameMode.SURVIVAL);

		String slottyyslotString = plugin.getConfig("hotbar.yml").getString("on-join_selected-slot");
		int slottyyslot;

		try {
			slottyyslot = Integer.parseInt(slottyyslotString);
		} catch (NumberFormatException e) {
			slottyyslot = 0;
		}

		player.getInventory().setHeldItemSlot(slottyyslot);

		if (HubCore.instance.getConfig().getBoolean("double_jump.enabled", true)) {
			player.setAllowFlight(true);
		}

		if (plugin.getConfig("hotbar.yml").getBoolean("items.enderbutt.enabled", true)) {
			player.getInventory().setItem(plugin.getConfig("hotbar.yml").getInt("items.enderbutt.slot"), enderbutt);
		}

		if (plugin.getConfig("hotbar.yml").getBoolean("items.server_selector.enabled", true)) {
			player.getInventory().setItem(plugin.getConfig("hotbar.yml").getInt("items.server_selector.slot"), selector);
		}

		if (plugin.getConfig().getBoolean("welcome_message.enabled", true)) {
			for (String message : plugin.getConfig().getStringList("welcome_message.message"))
				player.sendMessage(CC.translate(message));
		}

		Location spawnLocation = plugin.getSpawnLocation();

		if (spawnLocation != null) {
			event.getPlayer().teleport(spawnLocation);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (item != null && item.equals(selector)) {
				boolean commandEnabled = plugin.getConfig("hotbar.yml").getBoolean("items.server_selector.execute_command.enabled", true);

				if (commandEnabled) {
					String command = plugin.getConfig("hotbar.yml").getString("items.server_selector.execute_command.command");
					player.performCommand(command);
				} else {
					new ServerSelectorMenu().openMenu(player);
				}

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (player.getGameMode() == GameMode.SURVIVAL) {
				event.setCancelled(true);
				player.setFoodLevel(20);
			}
		}
	}

	@EventHandler
	public void onMoveItem(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {

			if (e.getWhoClicked().getGameMode() == GameMode.SURVIVAL) {
				e.setCancelled(true);
			}
		}
	}
}