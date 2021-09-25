package me.sudologic.challengemode;

import me.sudologic.challengemode.modes.BorderShrink;
import me.sudologic.challengemode.modes.GameType;
import me.sudologic.challengemode.modes.SupplyDrop;

import java.util.HashMap;

public class GameManager {
    private static GameType[] gameTypes;
    private HashMap<String, Boolean> isRunning;
    public GameManager() {
        isRunning = new HashMap<String, Boolean>();
        gameTypes = new GameType[]{new BorderShrink(), new SupplyDrop()};
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
}
