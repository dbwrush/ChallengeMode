package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.logging.Level;

public class SupplyDrop extends GameType{
    BorderShrink borderShrink;
    int typeCount;
    Material[] materials;
    double[] values;
    double totalDropWorth = 1.0;
    double lowestValue;
    public SupplyDrop() {
        defaultParams = Main.getCustomConfig().getConfigurationSection("supplyDrop");
        requiredPermission = "challengemode.toggle.supplydrop";
        toggleCommandExtension = "supplydrop";
        parseLootTable();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start(World world) {
        super.start(world);
        setRunning(true);
        int minTime = defaultParams.getInt("minTime");
        int maxTime = defaultParams.getInt("maxTime");
        int lengthInMinutes = defaultParams.getInt("lengthInMinutes");
        int noticeInMinutes = defaultParams.getInt("noticeInMinutes");
        int maxSlotsFull = defaultParams.getInt("maxSlotsFull");
        totalDropWorth = defaultParams.getDouble("totalDropWorth");
        Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Starting SupplyDrop cycle!");
        if(minTime <= 1) {
            Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] WARNING minTime should be more than 1 minute! You may have issues otherwise.");
        }
        new BukkitRunnable() {
            int minutes = 0;
            int timeSinceLastItem = 0;
            int timeToNextItem = (int)(Math.random() * (maxTime - minTime)) + minTime;
            double borderSize = world.getWorldBorder().getSize();
            Location dropLocation = new Location(world, (Math.random() * borderSize - 0.5 * borderSize), 255.0, (Math.random() * borderSize - 0.5 * borderSize));
            @Override
            public void run() {
                timeSinceLastItem++;
                minutes++;
                Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Next drop in " + (timeToNextItem - timeSinceLastItem) + " minutes!");
                double borderSize = world.getWorldBorder().getSize();
                if(timeSinceLastItem + noticeInMinutes == timeToNextItem) {
                    dropLocation = new Location(world, (Math.random() * borderSize - 0.5 * borderSize), 255.0, (Math.random() * borderSize - 0.5 * borderSize));
                    announceDrop(dropLocation, false, noticeInMinutes);
                }
                if(timeSinceLastItem >= timeToNextItem) {
                    announceDrop(dropLocation, true, noticeInMinutes);
                    drop(dropLocation, maxSlotsFull);
                    timeSinceLastItem = 0;
                    timeToNextItem = (int)(Math.random() * (maxTime - minTime)) + minTime;
                    dropLocation = new Location(world, (Math.random() * borderSize - 0.5 * borderSize), 255.0, (Math.random() * borderSize - 0.5 * borderSize));
                }
                if(minutes > lengthInMinutes) {
                    end();
                }
                if(!getRunning()) {
                    Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Ending SupplyDrop cycle!");
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1200, 1200);
    }

    @Override
    public void end() {
        super.end();
        setRunning(false);
    }

    @Override
    public void startDependencies(World world, ConfigurationSection params) {
        borderShrink = (BorderShrink)Main.gameManager.getGameType(BorderShrink.class.getSimpleName());
        borderShrink.start(world);
    }

    public void drop(Location dropLocation, int maxSlotsFull) {
        int y = dropLocation.getWorld().getHighestBlockYAt(dropLocation) + 1;
        Location fullLocation = new Location(dropLocation.getWorld(), dropLocation.getX(), (double)y, dropLocation.getZ());
        fullLocation.getChunk().setForceLoaded(true);
        Block block = fullLocation.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest)block.getState();
        genLoot(chest.getBlockInventory(), maxSlotsFull);
        fullLocation.getChunk().setForceLoaded(false);
    }

    public void announceDrop(Location dropLocation, boolean hasDropped, int minutes) {
        if(hasDropped) {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("[SupplyDrop] Supply Drop has landed at X: " + (int)dropLocation.getX() + " Z: " + (int)dropLocation.getZ());
            }
        } else {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("[SupplyDrop] Supply Drop landing at X: " + (int)dropLocation.getX() + " Z: " + (int)dropLocation.getZ() + " in " + (minutes) + " minutes!");
            }
        }
    }

    public void parseLootTable() {
        ConfigurationSection lootTable = defaultParams.getConfigurationSection("supplyLoot");

        typeCount = lootTable.getInt("typeCount");
        materials = new Material[typeCount];
        values = new double[typeCount];
        lootTable = lootTable.getConfigurationSection("types");
        lowestValue = Double.MAX_VALUE;
        for(int i = 1; i < typeCount + 1; i++) {
            if(lootTable.contains("type" + i)) {
                ConfigurationSection typeConfig = lootTable.getConfigurationSection("type" + i);
                materials[i - 1] = Material.getMaterial(typeConfig.getString("name"));
                values[i - 1] = typeConfig.getDouble("value");
                Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Adding material " + materials[i - 1]);
                Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] With value " + values[i - 1]);
                if(values[i - 1] < lowestValue) {
                    lowestValue = values[i - 1];
                }
            } else {
                materials[i - 1] = Material.AIR;
                values[i - 1] = 1000;
            }
        }
        Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] lowestValue was " + lowestValue);
    }

    public Inventory genLoot(Inventory inventory, int maxSlotsFull) {
        Bukkit.getLogger().log(Level.INFO, "Starting to generate chest!");
        double variability = 0.2 * totalDropWorth;
        double remainingValue = totalDropWorth + (Math.random() * variability - 0.5 * variability);
        int filledSlots = 0;
        Bukkit.getLogger().log(Level.INFO, "Value of chest: " + remainingValue);

        while(remainingValue > lowestValue && filledSlots < maxSlotsFull) {
            Bukkit.getLogger().log(Level.INFO, "Remaining value " + remainingValue);
            ArrayList<Integer> candidates = new ArrayList<>();
            for(int i = 0; i < values.length; i++) {
                if(values[i] < remainingValue) {
                    candidates.add(i);
                }
            }
            ItemStack itemStack;
            int selected = candidates.get((int)(Math.random() * candidates.size()));

            int count = materials[selected].getMaxStackSize();
            while(count * values[selected] > remainingValue) {
                count--;
            }
            count = (int)(Math.random() * count);
            if(count <= 0) {
                count = 1;
            }
            remainingValue -= values[selected] * count;
            itemStack = new ItemStack(materials[selected]);
            itemStack.setAmount(count);
            inventory.addItem(itemStack);
        }

        /*OLD WHILE LOOP STUPIDITY
        while(remainingValue > 0 && filledSlots < maxSlotsFull) {
            Bukkit.getLogger().log(Level.INFO, "Remaining value: " + remainingValue);
            if(remainingValue < lowestValue) {
                break;
            }
            ItemStack itemStack;
            int selected = materials.length;
            Bukkit.getLogger().log(Level.INFO, "Selecting material!");
            while(selected >= materials.length) {
                int rand = (int)(Math.random() * materials.length);
                if(values[rand] < remainingValue) {
                    Bukkit.getLogger().log(Level.INFO, "Selected!");
                    selected = rand;
                } else {
                    Bukkit.getLogger().log(Level.INFO, "Too high!");
                }
            }
            int count = (int)(Math.random() * materials[selected].getMaxStackSize());
            while(count * values[selected] > remainingValue) {
                count = (int)(Math.random() * materials[selected].getMaxStackSize());
            }
            remainingValue -= values[selected] * count;
            itemStack = new ItemStack(materials[selected]);
            itemStack.setAmount(count);
            inventory.addItem(itemStack);
        }*/
        return inventory;
    }

    @Override
    public void endDependencies() {
        borderShrink.end();
    }
}
