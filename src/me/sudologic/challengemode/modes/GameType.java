package me.sudologic.challengemode.modes;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class GameType {
    public String requiredPermission, toggleCommandExtension;
    boolean isRunning;

    public void toggle(World world, CommandSender commandSender) {
        if(isRunning) {
            commandSender.sendMessage("[ChallengeMode] You deactivated " + toggleCommandExtension + " mode!");
            end();
        } else {
            commandSender.sendMessage("[ChallengeMode] You activated " + toggleCommandExtension + " mode!");
            start(world);
        }
    }

    public abstract void start(World world);
    public abstract void end();
    public abstract void setConfigs(FileConfiguration config);
}
