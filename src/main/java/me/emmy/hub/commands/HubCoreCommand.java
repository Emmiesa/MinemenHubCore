package me.emmy.hub.commands;

import me.emmy.hub.HubCore;
import me.emmy.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HubCoreCommand implements CommandExecutor {

    private final HubCore plugin = HubCore.get();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(CC.translate(" &dMMC HubCore"));
            sender.sendMessage(CC.translate(" §e● Author: &d" + HubCore.instance.getDescription().getAuthors().get(0)));
            if (sender.hasPermission("MMC.OWNER")) {
                sender.sendMessage(CC.translate(" "));
                sender.sendMessage(CC.translate(" &dCommands:"));
                sender.sendMessage(CC.translate(" §e● /hubcore reload &7- &dreload the plugin"));
                sender.sendMessage(CC.translate(" §e● /setspawn &7- &dset spawn position"));
                sender.sendMessage(CC.translate(" §e● /serverselector &7- &dopen selector."));
            }
            sender.sendMessage(CC.translate(" "));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("MMC.OWNER")) {
                sender.sendMessage(CC.translate("&cNo permission."));
                return true;
            }

            sender.sendMessage(CC.translate("&cReloading..."));

            plugin.reloadConfig();
            plugin.reloadAllConfigs();

            sender.sendMessage(CC.translate("&aReload was successful!"));
        }
        return false;
    }
}