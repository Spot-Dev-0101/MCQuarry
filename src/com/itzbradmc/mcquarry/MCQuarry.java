package com.itzbradmc.mcquarry;

import com.itzbradmc.mcquarry.commands.mqCommand;
import com.itzbradmc.mcquarry.events.BlockInteractionListen;
import com.itzbradmc.mcquarry.events.BlockPlaceListen;
import com.itzbradmc.mcquarry.events.MenuInteractionListen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
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
        loadQuarryFiles();

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

    private void loadQuarryFiles(){
        File dataFolder = new File("plugins/MCQuarry/quarry/");
        File[] files = dataFolder.listFiles();

        for (File file : files){
            //logger.info(file.getName());
            YamlConfiguration ymlFile = new YamlConfiguration();
            if(file.getName().contains(".yml")) {
                try {
                    ymlFile.load(file);


                    String[] torch1String = ymlFile.getString("torch1").split(",");
                    Block torch1 = new Location(Bukkit.getWorld(torch1String[3]), Double.valueOf(torch1String[0]), Double.valueOf(torch1String[1]), Double.valueOf(torch1String[2])).getBlock();
                    //logger.info("" + torch1.getType());

                    String[] torch2String = ymlFile.getString("torch2").split(",");
                    Block torch2 = new Location(Bukkit.getWorld(torch2String[3]), Double.valueOf(torch2String[0]), Double.valueOf(torch2String[1]), Double.valueOf(torch2String[2])).getBlock();
                    //logger.info("" + torch2.getType());

                    String[] torch3String = ymlFile.getString("torch3").split(",");
                    Block torch3 = new Location(Bukkit.getWorld(torch3String[3]), Double.valueOf(torch3String[0]), Double.valueOf(torch3String[1]), Double.valueOf(torch3String[2])).getBlock();
                    //logger.info("" + torch3.getType());

                    String[] torch4String = ymlFile.getString("torch4").split(",");
                    Block torch4 = new Location(Bukkit.getWorld(torch4String[3]), Double.valueOf(torch4String[0]), Double.valueOf(torch4String[1]), Double.valueOf(torch4String[2])).getBlock();
                    //logger.info("" + torch4.getType());


                    String[] controllerString = ymlFile.getString("controller").split(",");
                    Block controller = new Location(Bukkit.getWorld(controllerString[3]), Double.valueOf(controllerString[0]), Double.valueOf(controllerString[1]), Double.valueOf(controllerString[2])).getBlock();
                    //logger.info("" + controller.getType());


                    int x = ymlFile.getInt("x");
                    int z = ymlFile.getInt("z");

                    World world = Bukkit.getWorld(ymlFile.getString("world"));

                    Player player = Bukkit.getPlayer(UUID.fromString(ymlFile.getString("player")));

                    Quarry quarry = new Quarry(torch1, torch2, torch3, torch4, controller, this, x, z, world, player, true);

                    if(ymlFile.contains("countX")){
                        quarry.countX = ymlFile.getInt("countX");
                        quarry.countY = ymlFile.getInt("countY");
                        quarry.countZ = ymlFile.getInt("countZ");
                        quarry.minedCount = ymlFile.getInt("minedCount");
                        quarry.delay = ymlFile.getInt("delay");
                        quarry.doubleDrops = ymlFile.getBoolean("doubleDrops");
                        quarry.randomDiamond = ymlFile.getBoolean("randomDiamond");
                    }

                    quarryList.put(controller.getLocation(), quarry);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }

        }
        logger.info("Loaded all quarry files");

    }
}

