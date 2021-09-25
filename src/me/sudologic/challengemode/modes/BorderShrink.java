package me.sudologic.challengemode.modes;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class BorderShrink extends GameType{
    private int startingBorder, endingBorder, lengthInSeconds;
    public BorderShrink() {
        requiredPermission = "challengemode.toggle.bordershrink";
        toggleCommandExtension = "bordershrink";
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setConfigs(FileConfiguration config) {
        startingBorder = config.getInt("startingBorder");
        endingBorder = config.getInt("endingBorder");
        lengthInSeconds = config.getInt("borderLengthInSeconds");
    }

    @Override
    public void start(World world) {
        super.start(world);
        setRunning(true);
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(startingBorder);
        world.getWorldBorder().setSize(endingBorder, lengthInSeconds);
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Border is now shrinking!");
    }

    @Override
    public void startAsDependency(World world, String[] params) {
        super.start(world);
        setRunning(true);
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(Integer.parseInt(params[0]));
        world.getWorldBorder().setSize(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Border is now shrinking!");
    }
}
