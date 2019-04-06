package com.itzbradmc.mcquarry;

import com.itzbradmc.mcquarry.events.BlockPlaceListen;
import net.minecraft.server.v1_13_R2.BlockPosition;
import org.bukkit.Bukkit;
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


}

