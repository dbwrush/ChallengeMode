package me.sudologic.challengemode.modes;

import me.sudologic.challengemode.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SupplyDrop extends GameType{
    private int secondsPerDrop = 360, startingBorder = 5000, endingBorder = 50, lengthInSeconds = 3600, noticeInSeconds = 60, maxSlotsFull = 28;
    //private int ticksPerDrop = 3600, startingBorder = 5000, endingBorder = 50, lengthInSeconds = 1209600;
    public SupplyDrop() {
        requiredPermission = "challengemode.toggle.supplydrop";
        toggleCommandExtension = "supplydrop";
    }

    @Override
    public void start(World world) {
        isRunning = true;
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(startingBorder);
        world.getWorldBorder().setSize(endingBorder, lengthInSeconds);
        Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Starting SupplyDrop cycle!");
        new BukkitRunnable() {
            int seconds = 0;
            int timeSinceLastItem = 0;
            Location dropLocation = new Location(world, (Math.random() * startingBorder - 0.5 * startingBorder), 255.0, (Math.random() * startingBorder - 0.5 * startingBorder));
            @Override
            public void run() {
                double borderSize = world.getWorldBorder().getSize();
                if(timeSinceLastItem + noticeInSeconds == secondsPerDrop) {//WARN OF DROP
                    dropLocation = new Location(world, (Math.random() * borderSize - 0.5 * borderSize), 255.0, (Math.random() * borderSize - 0.5 * borderSize));
                    announceDrop(dropLocation, false);
                }
                if(timeSinceLastItem >= secondsPerDrop) {
                    announceDrop(dropLocation, true);
                    drop(dropLocation);
                    timeSinceLastItem = 0;
                }
                if(seconds > lengthInSeconds) {
                    end();
                }
                timeSinceLastItem++;
                seconds++;
                if(!isRunning) {
                    Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Ending SupplyDrop cycle!");
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 20, 20);
    }

    @Override
    public void end() {
        isRunning = false;
    }

    @Override
    public void setConfigs(FileConfiguration config) {
        secondsPerDrop = config.getInt("secondsPerDrop");
        startingBorder = config.getInt("startingBorder");
        endingBorder = config.getInt("endingBorder");
        lengthInSeconds = config.getInt("lengthInSeconds");
        noticeInSeconds = config.getInt("noticeInSeconds");
        maxSlotsFull = config.getInt("maxSlotsFull");
    }

    public void drop(Location dropLocation) {
        int y = dropLocation.getWorld().getHighestBlockYAt(dropLocation);
        Location fullLocation = new Location(dropLocation.getWorld(), dropLocation.getX(), (double)y, dropLocation.getZ());
        fullLocation.getChunk().setForceLoaded(true);
        Block block = fullLocation.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest)block.getState();
        Inventory inventory = chest.getBlockInventory();
        int numSlots = (int)(maxSlotsFull * Math.random());
        for(int i = 0; i < numSlots; i++) {
            inventory.addItem(genLoot());
        }
        fullLocation.getChunk().setForceLoaded(false);
    }

    public void announceDrop(Location dropLocation, boolean hasDropped) {
        if(hasDropped) {
            Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] SupplyDrop has landed at " + (int)dropLocation.getX() + ", " + (int)dropLocation.getZ());
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("[SupplyDrop] Supply Drop has landed at X: " + (int)dropLocation.getX() + " Z: " + (int)dropLocation.getZ());
            }
        } else {
            Bukkit.getLogger().log(Level.INFO, "[SupplyDrop] Announcing SupplyDrop at " + (int)dropLocation.getX() + ", " + (int)dropLocation.getZ());
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("[SupplyDrop] Supply Drop landing at X: " + (int)dropLocation.getX() + " Z: " + (int)dropLocation.getZ() + " in " + (noticeInSeconds / 60) + " minutes!");
            }
        }
    }

    public ItemStack genLoot() {
        double seed = Math.random();
        ItemStack itemStack;
        if(seed > 0.999) {
            itemStack = new ItemStack(Material.END_CRYSTAL);
        } else if(seed > 0.99) {
            itemStack = new ItemStack(Material.NETHERITE_INGOT);
        } else if(seed > 0.95) {
            itemStack = new ItemStack(Material.DIAMOND);
        } else if(seed > 0.90) {
            itemStack = new ItemStack(Material.TNT);
        } else if(seed > 0.85) {
            itemStack = new ItemStack(Material.LAVA_BUCKET);
        } else if(seed > 0.80) {
            itemStack = new ItemStack(Material.IRON_INGOT);
        } else if(seed > 0.75) {
            itemStack = new ItemStack(Material.GOLD_INGOT);
        } else if(seed > 0.40) {
            itemStack = new ItemStack(Material.COOKED_BEEF);
        } else if(seed > 0.35) {
            itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
        } else if(seed > 0.20) {
            itemStack = new ItemStack(Material.GOLDEN_CARROT);
        } else if(seed > 0.199) {
            itemStack = new ItemStack(Material.MUSIC_DISC_PIGSTEP);
        } else if(seed > 0.19) {
            itemStack = new ItemStack(Material.ELYTRA);
        } else if(seed > 0.15) {
            itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        } else if(seed > 0.10) {
            itemStack = new ItemStack(Material.ANVIL);
        } else if(seed > 0.5) {
            itemStack = new ItemStack(Material.FIRE_CHARGE);
        } else {
            itemStack = new ItemStack(Material.APPLE);
        }
        itemStack.setAmount((int)(Math.random() * itemStack.getMaxStackSize()) + 1);
        return itemStack;
    }
}
