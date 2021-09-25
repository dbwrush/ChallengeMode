package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class GameType {
    public String requiredPermission, toggleCommandExtension;
    public String[] defaultParams;
    public void init() {
        setRunning(false);
    }

    public void toggle(World world, CommandSender commandSender) {
        if(getRunning()) {
            commandSender.sendMessage("[ChallengeMode] You deactivated " + toggleCommandExtension + " mode!");
            end();
        } else {
            commandSender.sendMessage("[ChallengeMode] You activated " + toggleCommandExtension + " mode!");
            start(world, new String[]{""});
        }
    }

    public void start(World world, String[] params) {
        if(params[0].equals("")) {
            params = defaultParams;
        }
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
