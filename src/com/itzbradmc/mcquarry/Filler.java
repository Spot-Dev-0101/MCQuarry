package com.itzbradmc.mcquarry;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Filler {

    public Block torch1;
    public Block torch2;
    public Block torch3;
    public Block torch4;
    public Block torch5;
    public Block torch6;
    public Block torch7;
    public Block torch8;

    public Block controller;

    public MCQuarry mcq;

    public int x;
    public int z;

    public World world;

    public Player player;

    private Chest chest;

    public boolean active = true;

    public int placedCount = 0;

    public int countX = 0;
    public int countY = 0;
    public int countZ = 0;

    public int delay = 2;

    private int taskID = 0;
    private int fileUpdateTaskID = 0;

    private Location currentLocation;

    public Filler(Block torch1, Block torch2, Block torch3, Block torch4, Block torch5, Block torch6, Block torch7, Block torch8, Block controller, MCQuarry mcq, int x, int z, World world, Player player, boolean fromFile){
        player.sendMessage("Created a new filler");
        this.torch1 = torch1;
        this.torch2 = torch2;
        this.torch3 = torch3;
        this.torch4 = torch4;
        this.torch5 = torch5;
        this.torch6 = torch6;
        this.torch7 = torch7;
        this.torch8 = torch8;

        this.controller = controller;

        this.mcq = mcq;

        this.x = x;
        this.z = z;
        this.world = world;
        this.player = player;

        currentLocation = torch1.getLocation();

        if(controller.getType() == Material.IRON_BLOCK){
            delay = 15;
        } else if(controller.getType() == Material.GOLD_BLOCK){
            delay = 10;
        } else if(controller.getType() == Material.DIAMOND_BLOCK){
            delay = 5;
        }


        checkForChest();
        if(chest == null){
            player.sendMessage("A chest is required for a filler");
            active = false;
        }

        if(fromFile == false) {

            File f = new File("plugins/MCQuarry/quarry/" + controller.hashCode() + ".yml");
            YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

            yamlFile.set("type", "filler");

            yamlFile.set("torch1", torch1.getLocation().getX() + "," + torch1.getLocation().getY() + "," + torch1.getLocation().getZ() + "," + torch1.getWorld().getName());
            yamlFile.set("torch2", torch2.getLocation().getX() + "," + torch2.getLocation().getY() + "," + torch2.getLocation().getZ() + "," + torch2.getWorld().getName());
            yamlFile.set("torch3", torch3.getLocation().getX() + "," + torch3.getLocation().getY() + "," + torch3.getLocation().getZ() + "," + torch3.getWorld().getName());
            yamlFile.set("torch4", torch4.getLocation().getX() + "," + torch4.getLocation().getY() + "," + torch4.getLocation().getZ() + "," + torch4.getWorld().getName());
            yamlFile.set("torch5", torch5.getLocation().getX() + "," + torch5.getLocation().getY() + "," + torch5.getLocation().getZ() + "," + torch5.getWorld().getName());
            yamlFile.set("torch6", torch6.getLocation().getX() + "," + torch6.getLocation().getY() + "," + torch6.getLocation().getZ() + "," + torch6.getWorld().getName());
            yamlFile.set("torch7", torch7.getLocation().getX() + "," + torch7.getLocation().getY() + "," + torch7.getLocation().getZ() + "," + torch7.getWorld().getName());
            yamlFile.set("torch8", torch8.getLocation().getX() + "," + torch8.getLocation().getY() + "," + torch8.getLocation().getZ() + "," + torch8.getWorld().getName());

            yamlFile.set("controller", controller.getLocation().getX() + "," + controller.getLocation().getY() + "," + controller.getLocation().getZ() + "," + controller.getWorld().getName());

            yamlFile.set("x", x);
            yamlFile.set("z", z);

            yamlFile.set("world", world.getName());

            yamlFile.set("player", player.getUniqueId().toString());

            yamlFile.set("delay", delay);

            try {
                yamlFile.save(f);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mcq, new Runnable() {
            @Override
            public void run() {

                if(!controllerExists()){
                    active = false;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("destroyed")));
                    //indicatorLocation.getBlock().setType(Material.AIR);
                    MCQuarry.quarryList.remove(controller.getLocation());
                    File f = new File("plugins/MCQuarry/quarry/" + controller.hashCode() + ".yml");
                    f.delete();
                    Bukkit.getScheduler().cancelTask(taskID);
                    Bukkit.getScheduler().cancelTask(fileUpdateTaskID);
                }

                if(active == true) {
                    checkForChest();
                    work();
                }

            }
        }, 0, delay);

        fileUpdateTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mcq, new Runnable() {
            @Override
            public void run() {

                File f = new File("plugins/MCQuarry/quarry/" + controller.hashCode() + ".yml");
                YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);

                yamlFile.set("countX", countX);
                yamlFile.set("countY", countY);
                yamlFile.set("countZ", countZ);

                try {
                    yamlFile.save(f);
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }, 0, 20);

    }

    private boolean controllerExists(){
        if(controller.getLocation().getBlock().getType() == Material.IRON_BLOCK || controller.getLocation().getBlock().getType() == Material.GOLD_BLOCK || controller.getLocation().getBlock().getType() == Material.DIAMOND_BLOCK){
            return true;
        } else{
            return false;
        }
    }

    private void work(){

            int distX = torch3.getX() - torch1.getX();
            int distZ = torch3.getZ() - torch1.getZ();

            if (distX > 0) {
                distX = 1;
            }
            if (distX < 0) {
                distX = -1;
            }

            if (distZ > 0) {
                distZ = 1;
            }
            if (distZ < 0) {
                distZ = -1;
            }

            currentLocation = new Location(world, torch1.getX() + distX + countX, torch1.getY() + countY, torch1.getZ() + distZ + countZ);

            currentLocation.getBlock().setType(getNextBlockFromChest());

            distX = torch3.getX() - torch1.getX();
            distZ = torch3.getZ() - torch1.getZ();

            if (distX > 0) {
                countX++;

                if (countX > distX - 2) {
                    if (distZ > 0) {
                        countX = 0;
                        countZ++;
                    }

                    if (distZ < 0) {
                        countX = 0;
                        countZ--;
                    }
                }
            }

            if (distX < 0) {
                countX--;

                if (countX < distX + 2) {
                    if (distZ > 0) {
                        countX = 0;
                        countZ++;
                    }

                    if (distZ < 0) {
                        countX = 0;
                        countZ--;
                    }
                }
            }

            if (distZ > 0) {

                if (countZ > distZ - 2) {
                    countZ = 0;
                    countY++;
                }

            }

            if (distZ < 0) {

                if (countZ < distZ + 2) {
                    countZ = 0;
                    countY++;
                }
            }

            if(torch1.getY() + countY == torch8.getY()){
                active = false;
            }


    }

    private Material getNextBlockFromChest(){

        Inventory chestInv = chest.getBlockInventory();

        for(int x = 0; x < chestInv.getSize(); x++){
            if(chestInv.getItem(x) != null && chestInv.getItem(x).getType().isBlock()){
                ItemStack newIS = chestInv.getItem(x);
                newIS.setAmount(chestInv.getItem(x).getAmount() - 1);
                try {
                    chestInv.setItem(x, newIS);
                    return chestInv.getItem(x).getType();
                } catch (NullPointerException e){
                    //something went wrong. Ik what's going wrong, but I can't fix it so here is a little work around :)
                }

            }
        }

        return Material.AIR;

    }

    private void checkForChest(){
        Block leftBlock = new Location(world, controller.getX()+1, controller.getY(), controller.getZ()).getBlock();
        Block rightBlock = new Location(world, controller.getX()-1, controller.getY(), controller.getZ()).getBlock();
        Block northBlock = new Location(world, controller.getX(), controller.getY(), controller.getZ()+1).getBlock();
        Block southBlock = new Location(world, controller.getX(), controller.getY(), controller.getZ()-1).getBlock();

        if(leftBlock.getType().name() == "CHEST"){
            chest = (Chest) leftBlock.getState();
        } else if(rightBlock.getType().name() == "CHEST"){
            chest = (Chest) rightBlock.getState();
        } else if(northBlock.getType().name() == "CHEST"){
            chest = (Chest) northBlock.getState();
        } else if(southBlock.getType().name() == "CHEST"){
            chest = (Chest) southBlock.getState();
        }

    }

}
