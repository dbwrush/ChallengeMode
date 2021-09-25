package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class GameType {
    public String requiredPermission, toggleCommandExtension;
    public String[] dependencies;
    public void init() {
        setRunning(false);
    }

    public void toggle(World world, CommandSender commandSender) {
        if(getRunning()) {
            commandSender.sendMessage("[ChallengeMode] You deactivated " + toggleCommandExtension + " mode!");
            end();
        } else {
            commandSender.sendMessage("[ChallengeMode] You activated " + toggleCommandExtension + " mode!");
            start(world);
        }
    }

    public abstract void startAsDependency(World world, String[] params);

    public void start(World world) {
        for(String dependency : dependencies) {
            for(GameType gameType : Main.gameManager.getGameTypes()) {
                if(gameType.toggleCommandExtension.equals(dependency) && !gameType.getRunning()) {
                    gameType.startAsDependency(world, new String[]{""});
                }
            }
        }
    }

    public void end() {
        for(String dependency : dependencies) {
            for(GameType gameType : Main.gameManager.getGameTypes()) {
                if(gameType.toggleCommandExtension.equals(dependency) && gameType.getRunning()) {
                    gameType.end();
                }
            }
        }
    }

    public abstract void setConfigs(FileConfiguration config);

    public Boolean getRunning() {
        return Main.gameManager.getRunning(toggleCommandExtension);
    }

    public void setRunning(boolean running) {
        Main.gameManager.setRunning(toggleCommandExtension, running);
    }
}
