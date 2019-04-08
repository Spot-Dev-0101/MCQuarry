package com.itzbradmc.mcquarry;

import com.itzbradmc.mcquarry.commands.mqCommand;
import com.itzbradmc.mcquarry.events.BlockInteractionListen;
import com.itzbradmc.mcquarry.events.BlockPlaceListen;
import com.itzbradmc.mcquarry.events.MenuInteractionListen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class MCQuarry extends JavaPlugin {

    public static Logger logger = Bukkit.getLogger();

    public static FileConfiguration config;

    //public static ArrayList<Quarry> quarryList = new ArrayList<>();
    public static HashMap<Location, Quarry> quarryList = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        logger.info("Enabled");

        loadConfig();
        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        super.onDisable();
        logger.info("Disabled");
    }

    private void loadConfig(){
        saveDefaultConfig();
        config = getConfig();
    }


    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new BlockPlaceListen(this), this);
        getServer().getPluginManager().registerEvents(new BlockInteractionListen(), this);
        getServer().getPluginManager().registerEvents(new MenuInteractionListen(), this);
    }

    private void registerCommands(){
        getCommand("mq").setExecutor(new mqCommand());
        getCommand("mcquarry").setExecutor(new mqCommand());
    }
}

