package me.emmy.hub.commands;

import me.emmy.hub.HubCore;
import me.emmy.hub.menu.ServerSelectorMenu;
import me.emmy.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerSelectorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        new ServerSelectorMenu().openMenu(player);
        return false;
    }
}