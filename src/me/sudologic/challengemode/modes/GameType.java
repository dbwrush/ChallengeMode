package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class GameType {
    public String requiredPermission, toggleCommandExtension;
    public ConfigurationSection defaultParams;
    public void init() {
        setRunning(false);
    }

    public void toggle(World world, CommandSender commandSender, boolean autoDefaultParams) {
        if(getRunning()) {
            commandSender.sendMessage("[ChallengeMode] You deactivated " + toggleCommandExtension + " mode!");
            end();
        } else {
            commandSender.sendMessage("[ChallengeMode] You activated " + toggleCommandExtension + " mode!");
            start(world);
        }
    }

    public void start(World world) {

        startDependencies(world, defaultParams);
    }

    public void end() {
        endDependencies();
    }

    public Boolean getRunning() {
        return Main.gameManager.getRunning(toggleCommandExtension);
    }

    public void setRunning(boolean running) {
        Main.gameManager.setRunning(toggleCommandExtension, running);
    }
    public abstract void startDependencies(World world, ConfigurationSection params);

    public abstract void endDependencies();
}
