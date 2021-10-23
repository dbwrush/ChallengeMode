package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class GameType {
    public String requiredPermission, toggleCommandExtension;
    public String[] defaultParams;
    public int numParams;
    public void init() {
        setRunning(false);
    }

    public void toggle(World world, CommandSender commandSender, String[] params, boolean autoDefaultParams) {
        if(getRunning()) {
            commandSender.sendMessage("[ChallengeMode] You deactivated " + toggleCommandExtension + " mode!");
            end();
        } else {
            if(params.length == defaultParams.length) {
                commandSender.sendMessage("[ChallengeMode] You activated " + toggleCommandExtension + " mode!");
                start(world, params);
            } if (autoDefaultParams) {
                commandSender.sendMessage("[ChallengeMode] Too many/few paramters. " + toggleCommandExtension + " will use config parameters.");
                start(world, defaultParams);
            } else {
                commandSender.sendMessage("[ChallengeMode] Too many/few parameters. " + toggleCommandExtension + " was not enabled. To use config parameters, enable autoDefaultParams in the config.");
            }
        }
    }

    public void start(World world, String[] params) {

        startDependencies(world, params);
    }

    public void end() {
        endDependencies();
    }

    public abstract void setConfigs(FileConfiguration config);

    public Boolean getRunning() {
        return Main.gameManager.getRunning(toggleCommandExtension);
    }

    public void setRunning(boolean running) {
        Main.gameManager.setRunning(toggleCommandExtension, running);
    }
    public abstract void startDependencies(World world, String[] params);

    public abstract void endDependencies();
}
