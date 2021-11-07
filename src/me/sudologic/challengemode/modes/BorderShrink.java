package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class BorderShrink extends GameType{
    World world;
    public BorderShrink() {
        defaultParams = Main.getCustomConfig().getConfigurationSection("borderShrink");
        requiredPermission = "challengemode.toggle.bordershrink";
        toggleCommandExtension = "bordershrink";
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void startDependencies(World world, ConfigurationSection params) {

    }

    @Override
    public void start(World world) {
        super.start(world);
        this.world = world;
        setRunning(true);
        int startingBorder = defaultParams.getInt("startingBorder");
        int endingBorder = defaultParams.getInt("endingBorder");
        int minutes = defaultParams.getInt("minutes");
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(startingBorder);
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Setting border to " + startingBorder);
        world.getWorldBorder().setSize(endingBorder, minutes * 60);
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Border will shrink to " + endingBorder + " over " + minutes + " minutes");
        Bukkit.getLogger().log(Level.INFO, "[BorderShrink] Border is now shrinking!");
    }

    @Override
    public void end() {
        super.end();
        if(!world.equals(null)) {
            world.getWorldBorder().setSize(30000000);
        }
        setRunning(false);
    }

    @Override
    public void endDependencies() {
    }
}
