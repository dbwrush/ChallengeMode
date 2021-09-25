package me.sudologic.challengemode.modes;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class BorderShrink extends GameType{
    World world;
    public BorderShrink() {
        defaultParams = new String[3];
        requiredPermission = "challengemode.toggle.bordershrink";
        toggleCommandExtension = "bordershrink";
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setConfigs(FileConfiguration config) {
        defaultParams[0] = Integer.toString(config.getInt("startingBorder"));
        defaultParams[1] = Integer.toString(config.getInt("endingBorder"));
        defaultParams[2] = Integer.toString(config.getInt("borderLengthInSeconds"));
    }

    @Override
    public void startDependencies(World world, String[] params) {

    }


    @Override
    public void start(World world, String[] params) {
        super.start(world, params);
        setRunning(true);
        int startingBorder = Integer.parseInt(params[0]);
        int endingBorder = Integer.parseInt(params[1]);
        int lengthInSeconds = Integer.parseInt(params[2]);
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(startingBorder);
        world.getWorldBorder().setSize(endingBorder, lengthInSeconds);
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Border is now shrinking!");
    }

    @Override
    public void end() {
        super.end();
        if(!world.equals(null)) {
            world.getWorldBorder().setSize(30000000);
        }
    }

    @Override
    public void endDependencies() {
    }
}
