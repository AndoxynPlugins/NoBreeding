package net.daboross.bukkitdev.nobreeding;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoBreeding extends JavaPlugin implements Listener {

    private boolean breedingEnabled;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Configuration c = getConfig();
        c.addDefault("NoBreeding-last-enabled", true);
        saveDefaultConfig();
        breedingEnabled = c.getBoolean("NoBreeding-last-enabled");
    }

    @Override
    public void onDisable() {
        Configuration c = getConfig();
        c.set("NoBreeding-last-enabled", breedingEnabled);
        saveConfig();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent evt) {
        if (breedingEnabled && evt.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            evt.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nobreed")) {
            if (args.length == 0) {
                if (breedingEnabled) {
                    sender.sendMessage("NoBreeding is currently enabled. Animals can not breed.");
                    sender.sendMessage("To change this setting, use:");
                    sender.sendMessage("/nobreed disable");
                } else {
                    sender.sendMessage("NoBreeding is currently disabled. Animals can breed.");
                    sender.sendMessage("To change this setting, use:");
                    sender.sendMessage("/nobreed enable");
                }
            } else if (args.length > 1) {
                sender.sendMessage("To many arguments!");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("enable")) {
                    breedingEnabled = true;
                    sender.sendMessage("NoBreeding is now enabled. Animals can no longer breed.");
                } else if (args[0].equalsIgnoreCase("disable")) {
                    breedingEnabled = false;
                    sender.sendMessage("NoBreeding is now disabled. Animals can now breed.");
                } else {
                    sender.sendMessage(args[0] + " is not 'enable' or 'disable'");
                    return false;
                }
            }
        } else {
            sender.sendMessage("Command " + command.getName() + " unknown to NoBreeding");
        }
        return true;
    }
}
