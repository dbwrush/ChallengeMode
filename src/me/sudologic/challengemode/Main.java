package me.sudologic.challengemode;

import me.sudologic.challengemode.commands.ToggleCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Main plugin;
    public Main() {}
    public void Plugin(Main plugin) {Main.plugin = plugin;}
    public static Main getPlugin() {return plugin;}

    public void onEnable() {
        Plugin(this);
        Bukkit.getLogger().log(Level.INFO, "[ChallengeMode] Starting ChallengeMode by sudologic!");
        registerListeners();
        registerCommands();
    }

    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "[ChallengeMode] Shutting down!");
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
    }

    public void registerCommands() {
        this.getCommand("challengemode").setExecutor(new ToggleCommand());
    }
}