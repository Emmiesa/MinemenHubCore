package me.emmy.hub.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.hub.HubCore;
import me.emmy.hub.utils.CC;
import me.emmy.hub.utils.assemble.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardLayout implements AssembleAdapter {

	private final HubCore plugin = HubCore.get();

	@Override
	public String getTitle(Player player) {
		String title = CC.translate(plugin.getConfig().getString("scoreboard.title"));
		return PlaceholderAPI.setPlaceholders(player, title);
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> lines = new ArrayList<>(plugin.getConfig().getStringList("scoreboard.lines")).stream()
				.map(CC::translate)
				.collect(Collectors.toList());
		return lines.stream()
				.map(line -> PlaceholderAPI.setPlaceholders(player, line))
				.collect(Collectors.toList());
	}
}