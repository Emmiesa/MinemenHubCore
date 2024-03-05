package me.emmy.hub.scoreboard;

import dev.hely.tab.TabColumn;
import dev.hely.tab.TabLayout;
import dev.hely.tab.TabProvider;
import dev.hely.tab.skin.Skin;
import dev.hely.tab.utils.TablistUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.hub.HubCore;
import me.emmy.hub.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TablistAdapter implements TabProvider {
    private final HubCore plugin = HubCore.get();

    @Override
    public Set<TabLayout> getProvider(Player player) {
        Set<TabLayout> layoutSet = new HashSet<>();
        String tablistType = HubCore.instance.getConfig().getString("tablist.type");
        List<UUID> sorted = Bukkit.getOnlinePlayers().stream().map(
                        Player::getUniqueId)
                .collect(Collectors.toList());

        for (int i = 1; i <= 20; i++) {
            if (tablistType.equals("VANILLA")) {
                int playerSize = 0;
                int column = 0;
                int row = 1;

                for (UUID uuid : sorted) {
                    Player online = Bukkit.getPlayer(uuid);
                    playerSize++;
                    if (playerSize >= 60) break;

                    String path = plugin.getConfig().getString("tablist.player_prefix");
                    String prefix = path;

                    layoutSet.add(new TabLayout(TabColumn.getColumn(column++), row)
                            .setText(prefix + online.getName())
                            .setSkin(Skin.getSkin(online))
                            .setPing(TablistUtil.getPing(online)));

                    if (column == 4) {
                        column = 0;
                        row++;
                    }
                }
            } else if (tablistType.equals("CUSTOM")) {
                layoutSet.add(new TabLayout(TabColumn.LEFT, i)
                        .setText(CC.translate(applyPlaceholders(player, getLines("left", i, "text"))))
                        .setSkin(getSkin(player, getLines("left", i, "head")))
                        .setPing(HubCore.instance.getConfig().getInt("tablist.ping")));
                layoutSet.add(new TabLayout(TabColumn.MIDDLE, i)
                        .setText(CC.translate(applyPlaceholders(player, getLines("middle", i, "text"))))
                        .setSkin(getSkin(player, getLines("middle", i, "head")))
                        .setPing(HubCore.instance.getConfig().getInt("tablist.ping")));
                layoutSet.add(new TabLayout(TabColumn.RIGHT, i)
                        .setText(CC.translate(applyPlaceholders(player, getLines("right", i, "text"))))
                        .setSkin(getSkin(player, getLines("right", i, "head")))
                        .setPing(HubCore.instance.getConfig().getInt("tablist.ping")));
                layoutSet.add(new TabLayout(TabColumn.FAR_RIGHT, i)
                        .setText(CC.translate(applyPlaceholders(player, getLines("far_right", i, "text"))))
                        .setSkin(getSkin(player, getLines("far_right", i, "head")))
                        .setPing(HubCore.instance.getConfig().getInt("tablist.ping")));
            }
        }

        return layoutSet;
    }

    @Override
    public List<String> getFooter(Player player) {
        return headerFooterList(HubCore.instance.getConfig().getStringList("tablist.footer"), player);
    }

    @Override
    public List<String> getHeader(Player player) {
        List<String> headerList = HubCore.instance.getConfig().getStringList("tablist.header");

        headerList.replaceAll(line -> CC.translate(line).replace("%version%", HubCore.instance.getDescription().getVersion()));

        return headerFooterList(headerList, player);
    }

    public Skin getSkin(Player player, String skinTab) {
        Skin skinDefault = Skin.DEFAULT;

        if (skinTab.contains("%PLAYER%")) {
            skinDefault = Skin.getSkin(player);
        }
        if (skinTab.contains("%DISCORD%")) {
            skinDefault = Skin.DISCORD_SKIN;
        }
        if (skinTab.contains("%YOUTUBE%")) {
            skinDefault = Skin.YOUTUBE_SKIN;
        }
        if (skinTab.contains("%TWITTER%")) {
            skinDefault = Skin.TWITTER_SKIN;
        }
        if (skinTab.contains("%FACEBOOK%")) {
            skinDefault = Skin.FACEBOOK_SKIN;
        }
        if (skinTab.contains("%STORE%")) {
            skinDefault = Skin.STORE_SKIN;
        }
        return skinDefault;
    }

    private List<String> headerFooterList(List<String> path, Player player) {
        List<String> list = new ArrayList<>();

        for (String str : path) {
            list.add(CC.translate(str));
        }
        return list;
    }

    private String getLines(String column, int position, String textOrHead) {
        return HubCore.instance.getConfig().getString("tablist.lines." + column + "." + position + "." + textOrHead);
    }

    private String applyPlaceholders(Player player, String line) {
        return PlaceholderAPI.setPlaceholders(player, CC.translate(line));
    }

}
