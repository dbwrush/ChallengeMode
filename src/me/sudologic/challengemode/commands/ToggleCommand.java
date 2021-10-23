package me.sudologic.challengemode.commands;

import me.sudologic.challengemode.GameManager;
import me.sudologic.challengemode.Main;
import me.sudologic.challengemode.modes.GameType;
import me.sudologic.challengemode.modes.SupplyDrop;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ToggleCommand implements CommandExecutor {
    boolean autoDefaultParams;
    GameManager gameManager = Main.gameManager;

    public ToggleCommand(FileConfiguration config) {
        autoDefaultParams = config.getBoolean("autoDefaultParams");
        if(autoDefaultParams) {
            Bukkit.getLogger().log(Level.INFO, "[ChallengeMode] Will use default parameters if parameters are not supplied.");
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length > 0) {
            for(GameType gameType : gameManager.getGameTypes()) {
                if(args[0].equals(gameType.toggleCommandExtension)) {
                    if(commandSender.hasPermission(gameType.requiredPermission) || commandSender.hasPermission("challengemode.toggle.*")) {
                        if(commandSender instanceof Player) {
                            gameType.toggle(((Player)commandSender).getWorld(), commandSender, args, autoDefaultParams);
                        } else if(commandSender instanceof BlockCommandSender) {
                            gameType.toggle(((BlockCommandSender)commandSender).getBlock().getWorld(), commandSender, args, autoDefaultParams);
                        } else if(commandSender instanceof ConsoleCommandSender) {
                            gameType.toggle(Bukkit.getWorlds().get(0), commandSender, args, autoDefaultParams);
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
