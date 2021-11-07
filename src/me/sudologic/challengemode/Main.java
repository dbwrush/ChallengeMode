package me.sudologic.challengemode;

import me.sudologic.challengemode.commands.ToggleCommand;
import me.sudologic.challengemode.modes.GameType;
import me.sudologic.challengemode.modes.SupplyDrop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static File customConfigFile;
    public static FileConfiguration customConfig;

    private static Main plugin;
    public Main() {}
    public void Plugin(Main plugin) {Main.plugin = plugin;}
    public static Main getPlugin() {return plugin;}

    public static GameManager gameManager;

    public void onEnable() {
        Plugin(this);
        Bukkit.getLogger().log(Level.INFO, "[ChallengeMode] Starting ChallengeMode by sudologic!");
        createCustomConfig();
        createConfigs();
        gameManager = new GameManager();
        gameManager.init();

        registerListeners();
        registerCommands();
    }

    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "[ChallengeMode] Shutting down!");
    }

    public static FileConfiguration getCustomConfig() {return customConfig;}

    public void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try{
            customConfig.load(customConfigFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createConfigs() {
        this.saveDefaultConfig();
        this.getConfig();
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
    }

    public void registerCommands() {
        this.getCommand("challengemode").setExecutor(new ToggleCommand());
    }
}