package me.emmy.hub.menu;

import me.emmy.hub.HubCore;
import me.emmy.hub.menu.button.ServerSelectButton;
import me.emmy.hub.utils.CC;
import me.emmy.hub.utils.ItemBuilder;
import me.emmy.hub.utils.menu.Button;
import me.emmy.hub.utils.menu.Menu;
import me.emmy.hub.utils.menu.button.RefillGlassButton;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSelectorMenu extends Menu {

	private final RefillGlassButton refillGlassButton;

	public ServerSelectorMenu() {
		this.refillGlassButton = new RefillGlassButton(
				Material.STAINED_GLASS_PANE, 15
		);
	}

	@Override
	public String getTitle(Player player) {
		return CC.translate(HubCore.instance.getConfig("menus.yml").getString("menus.server_selector.title"));
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		ConfigurationSection serversSection = HubCore.instance.getConfig("menus.yml").getConfigurationSection("menus.server_selector.servers");

		if (serversSection != null) {
			for (String serverKey : serversSection.getKeys(false)) {
				ConfigurationSection serverSection = serversSection.getConfigurationSection(serverKey);

				if (serverSection != null) {
					int slot = serverSection.getInt("slot", 0);
					Material materialType = Material.matchMaterial(serverSection.getString("material", "STONE"));
					String name = CC.translate(serverSection.getString("name", "&c&lInvalid Server"));
					List<String> lore = CC.translate(serverSection.getStringList("lore"));
					int data = serverSection.getInt("data", 0);
					Material material = new MaterialData(materialType, (byte) data).toItemStack().getType();

					buttons.put(slot, new ServerSelectButton(material, name, lore, serverSection.getString("command")));
				}
			}
		}

		if (HubCore.instance.getConfig("menus.yml").getBoolean("menus.server_selector.refill-glass")) {
			for (int i = 0; i < getSize(); i++) {
				if (!buttons.containsKey(i)) {
					buttons.put(i, refillGlassButton);
				}
			}
		}

		return buttons;
	}

	@Override
	public int getSize() {
		ConfigurationSection menuSection = HubCore.instance.getConfig("menus.yml").getConfigurationSection("menus.server_selector");

		if (menuSection != null && menuSection.contains("size")) {
			return menuSection.getInt("size", 9 * 3);
		}

		return 9 * 3;
	}
}