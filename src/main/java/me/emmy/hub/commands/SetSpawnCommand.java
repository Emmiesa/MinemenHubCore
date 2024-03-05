package me.emmy.hub.commands;

import me.emmy.hub.HubCore;
import me.emmy.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final HubCore plugin;

    public SetSpawnCommand(HubCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean enableSpawnTeleport = plugin.getConfig().getBoolean("spawn-teleport", true);
            plugin.setSpawnLocation(player.getLocation());

            player.sendMessage(CC.translate("&aSpawn set!"));

            if (!enableSpawnTeleport) {
                player.sendMessage(CC.translate("&cSpawn teleport is disabled"));
                System.out.println(" ");
                System.out.println(" ");
            }
            return true;
        } else {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
    }
}