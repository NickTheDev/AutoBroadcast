package net.nikdev.autobroadcast;

import net.nikdev.autobroadcast.broadcast.BroadCastManager;
import net.nikdev.autobroadcast.command.BroadCastCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoBroadcast extends JavaPlugin {

    private BroadCastManager broadCastManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Created by NikDev, " + getDescription().getWebsite());

        broadCastManager = new BroadCastManager(this);

        getCommand("broadcast").setExecutor(new BroadCastCommand(this));

    }

    public BroadCastManager getBroadCastManager() {
        return broadCastManager;
    }

}
