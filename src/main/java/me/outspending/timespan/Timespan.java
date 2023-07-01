package me.outspending.timespan;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class Timespan extends JavaPlugin implements Listener {

    public ExpirableHashMap<Location, Material> something = new ExpirableHashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        something.put(block.getLocation(), block.getType(), 3);
    }


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Location, Material> expired = something.getExpired();
                if (expired.isEmpty()) return;

                expired.forEach((location, material) -> {
                    location.getBlock().setType(material);
                    something.remove(location);
                });
            }
        }.runTaskTimer(this, 5, 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
