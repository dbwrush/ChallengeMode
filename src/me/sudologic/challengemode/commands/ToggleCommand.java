package me.sudologic.challengemode.commands;

import me.sudologic.challengemode.GameManager;
import me.sudologic.challengemode.Main;
import me.sudologic.challengemode.modes.GameType;
import me.sudologic.challengemode.modes.SupplyDrop;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {
    GameManager gameManager = Main.gameManager;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length > 0) {
            for(GameType gameType : gameManager.getGameTypes()) {
                if(args[0].equals(gameType.toggleCommandExtension)) {
                    if(commandSender.hasPermission(gameType.requiredPermission) || commandSender.hasPermission("challengemode.toggle.*")) {
                        if(commandSender instanceof Player) {
                            gameType.toggle(((Player)commandSender).getWorld(), commandSender);
                        } else if(commandSender instanceof BlockCommandSender) {
                            gameType.toggle(((BlockCommandSender)commandSender).getBlock().getWorld(), commandSender);
                        } else if(commandSender instanceof ConsoleCommandSender) {
                            gameType.toggle(Bukkit.getWorlds().get(0), commandSender);
                        }
                        return true;
                    } else {
                        commandSender.sendMessage("[ChallengeMode] You do not have permission to use this command!");
                        return false;
                    }
                }
            }
        } else {
            String typeList = "";
            for(GameType gameType : gameManager.getGameTypes()) {
                typeList = typeList + gameType.toggleCommandExtension + " ";
            }
            commandSender.sendMessage("[ChallengeMode] You didn't specify which mode to toggle! Here is the list of modes:" + typeList);
        }
        return false;
    }
}
