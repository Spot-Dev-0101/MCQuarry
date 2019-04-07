package com.itzbradmc.mcquarry;

import com.itzbradmc.mcquarry.commands.mqCommand;
import com.itzbradmc.mcquarry.events.BlockPlaceListen;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MCQuarry extends JavaPlugin {

    public static Logger logger = Bukkit.getLogger();

    public static ArrayList<Quarry> quarryList = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        logger.info("Enabled");

        registerEvents();

    }

    @Override
    public void onDisable() {
        super.onDisable();
        logger.info("Disabled");
    }


    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new BlockPlaceListen(this), this);
    }

    private void registerCommands(){
        getCommand("mq").setExecutor(new mqCommand());
        getCommand("mcquarry").setExecutor(new mqCommand());
    }
}

