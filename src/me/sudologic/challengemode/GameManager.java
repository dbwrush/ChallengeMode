package me.sudologic.challengemode;

import me.sudologic.challengemode.modes.BorderShrink;
import me.sudologic.challengemode.modes.GameType;
import me.sudologic.challengemode.modes.SupplyDrop;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.logging.Level;

public class GameManager {
    private static GameType[] gameTypes;
    private HashMap<String, Boolean> isRunning;
    public GameManager() {
        isRunning = new HashMap<String, Boolean>();
        gameTypes = new GameType[]{new BorderShrink(), new SupplyDrop()};
    }

    public void init() {
        for(GameType gameType : gameTypes) {
            gameType.init();
        }
    }

    public void setRunning(String name, boolean running) {
        isRunning.put(name, running);
    }

    public boolean getRunning(String name) {
        return isRunning.get(name);
    }

    public static GameType[] getGameTypes() {
        return gameTypes;
    }

    public static GameType getGameType(String typeName) {
        for(GameType type : gameTypes) {
            if(type.getClass().getSimpleName().equals(typeName)) {
                return type;
            }
        }
        //if for whatever reason this type is missing from gameTypes:
        return null;
    }
}
