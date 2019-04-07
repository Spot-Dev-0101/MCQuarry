package com.itzbradmc.mcquarry;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Quarry {

    private Block torch1;
    private Block torch2;
    private Block torch3;
    private Block torch4;

    private Block controller;

    private World world;

    private int x = 0;
    private int z = 0;

    private Player player;

    private Chest chest;

    public boolean active = true;

    public int minedCount = 0;

    public HashMap<Material, Integer> eachBlockCount = new HashMap<>();

    int countX = 0;
    int countY = 0;
    int countZ = 0;

    int delay = 10;

    private int taskID = 0;

    private Location currentLocation;
    private List<Location> indicatorLocation = new ArrayList<>();

    public Quarry(Block torch1, Block torch2, Block torch3, Block torch4, Block controller, MCQuarry mcq, int x, int z, World world, Player player){
        this.torch1 = torch1;
        this.torch2 = torch2;
        this.torch3 = torch3;
        this.torch4 = torch4;
        this.controller = controller;
        this.x = x;
        this.z = z;
        this.world = world;
        this.player = player;

        currentLocation = torch1.getLocation();
        //indicatorLocation.add(new Location(world, currentLocation.getX(), currentLocation.getY()+1, currentLocation.getZ()));

        if(controller.getType() == Material.IRON_BLOCK){
            delay = 15;
        } else if(controller.getType() == Material.GOLD_BLOCK){
            delay = 10;
        } else if(controller.getType() == Material.DIAMOND_BLOCK){
            delay = 5;
        }

        checkForChest();

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mcq, new Runnable() {
            @Override
            public void run() {

                if(!controllerExists()){
                    active = false;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("destroyed")));
                    for (Location loc: indicatorLocation) {
                        loc.getBlock().setType(Material.AIR);
                    }
                    //indicatorLocation.getBlock().setType(Material.AIR);
                    MCQuarry.quarryList.remove(controller.getLocation());
                    Bukkit.getScheduler().cancelTask(taskID);
                }

                if(currentLocation.getY() < 3){
                    active = false;
                }



                if(active == true) {
                    checkForChest();
                    work();
                }
            }
        }, 0, delay);

    }

    private boolean controllerExists(){
        if(controller.getLocation().getBlock().getType() == Material.IRON_BLOCK || controller.getLocation().getBlock().getType() == Material.GOLD_BLOCK || controller.getLocation().getBlock().getType() == Material.DIAMOND_BLOCK){
            return true;
        } else{
            return false;
        }
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

    private void work(){

        int distX = torch3.getX() - torch1.getX();
        int distZ = torch3.getZ() - torch1.getZ();

        if(distX > 0){
            distX = 1;
        }
        if(distX < 0){
            distX = -1;
        }

        if(distZ > 0){
            distZ = 1;
        }
        if(distZ < 0){
            distZ = -1;
        }

        for (Location loc: indicatorLocation) {
            loc.getBlock().setType(Material.AIR);
        }
        indicatorLocation.clear();
        currentLocation =  new Location(world,torch1.getX()+distX + countX, torch1.getY()-1 + countY, torch1.getZ()+distZ + countZ);
        //indicatorLocation = new Location(world, currentLocation.getX(), currentLocation.getY()+1, currentLocation.getZ());
        for(int i = 0; i < -countY+1; i++){
            indicatorLocation.add(new Location(world, currentLocation.getX(), currentLocation.getY()+i+1, currentLocation.getZ()));
        }

        Location topindicator = indicatorLocation.get(indicatorLocation.size()-1);







        int xDist = (topindicator.getBlockX()-torch1.getX());
        player.sendMessage("" + xDist);
        if(xDist < 0){
            for(int i = 0; i < -xDist; i++){
                //player.sendMessage("" + (topindicator.getX()-i) + " " + i + " " + xDist);
                indicatorLocation.add(new Location(world, topindicator.getX()-i, topindicator.getY(), topindicator.getZ()));
            }
            int secondxDist = (topindicator.getBlockX()-torch3.getX());
            for(int i = 0; i < secondxDist; i++){
                //player.sendMessage("s " + (topindicator.getX()+i) + " " + i + " " + secondxDist);
                indicatorLocation.add(new Location(world, topindicator.getX()+i, topindicator.getY(), topindicator.getZ()));
            }
        }
        if(xDist > 0){
            for(int i = 0; i < xDist; i++){
                //player.sendMessage("" + (topindicator.getX()-i) + " " + i + " " + xDist);
                indicatorLocation.add(new Location(world, topindicator.getX()-i, topindicator.getY(), topindicator.getZ()));
            }
            int secondxDist = (topindicator.getBlockX()-torch3.getX());
            for(int i = 0; i < -secondxDist; i++){
                //player.sendMessage("s " + topindicator.getX()+i + " " + i + " " + secondxDist);
                indicatorLocation.add(new Location(world, topindicator.getX()+i, topindicator.getY(), topindicator.getZ()));
            }
        }

        int zDist = (topindicator.getBlockZ()-torch1.getZ());
        player.sendMessage("" + zDist);
        if(zDist < 0){
            for(int i = 0; i < -zDist; i++){
                player.sendMessage(" 2" + (topindicator.getZ()-i) + " " + i + " " + zDist);
                indicatorLocation.add(new Location(world, topindicator.getX(), topindicator.getY(), topindicator.getZ()+i));
            }
            int secondzDist = (topindicator.getBlockZ()-torch3.getZ());
            for(int i = 0; i < secondzDist; i++){
                player.sendMessage("s 2 " + (topindicator.getZ()+i) + " " + i + " " + secondzDist);
                indicatorLocation.add(new Location(world, topindicator.getX(), topindicator.getY(), topindicator.getZ()-i));
            }
        }
        if(zDist > 0){
            for(int i = 0; i < zDist; i++){
                player.sendMessage(" 2" + (topindicator.getZ()-i) + " " + i + " " + zDist);
                indicatorLocation.add(new Location(world, topindicator.getX(), topindicator.getY(), topindicator.getZ()+i));
            }
            int secondzDist = (topindicator.getBlockZ()-torch3.getZ());
            for(int i = 0; i < -secondzDist; i++){
                player.sendMessage("s 2 " + topindicator.getZ()+i + " " + i + " " + secondzDist);
                indicatorLocation.add(new Location(world, topindicator.getX(), topindicator.getY(), topindicator.getZ()-i));
            }
        }

        //







        if(currentLocation.getBlock().getType() != Material.AIR && currentLocation.getBlock().getType() != Material.BEDROCK) {
            Material itemMaterial = getMinedVerson(currentLocation.getBlock());
            if (chest != null) {
                //chest.getBlockInventory().addItem(new ItemStack(currentLocation.getBlock().getType()));
                chest.getInventory().addItem(new ItemStack(itemMaterial));
                //chest.update(true);
            } else {
                Location dropLocation = new Location(world, controller.getX(), controller.getY() + 1, controller.getZ());
                world.dropItem(controller.getLocation(), new ItemStack(itemMaterial));
            }

            if(!eachBlockCount.containsKey(itemMaterial)){
                eachBlockCount.put(itemMaterial, 1);
            } else{
                eachBlockCount.put(itemMaterial, eachBlockCount.get(itemMaterial)+1);
            }

        }


        if(currentLocation.getBlock().getType() != Material.BEDROCK) {
            currentLocation.getBlock().setType(Material.AIR);
            for (Location loc: indicatorLocation) {
                loc.getBlock().setType(Material.OAK_FENCE);
            }
            minedCount++;
        }
        distX = torch3.getX() - torch1.getX();
        distZ = torch3.getZ() - torch1.getZ();


        if(distX > 0){
            countX++;

            if(countX > distX-2){
                if(distZ > 0){
                    countX = 0;
                    countZ++;
                }

                if(distZ < 0){
                    countX = 0;
                    countZ--;
                }
            }
        }

        if(distX < 0){
            countX--;

            if(countX < distX+2){
                if(distZ > 0){
                    countX = 0;
                    countZ++;
                }

                if(distZ < 0){
                    countX = 0;
                    countZ--;
                }
            }
        }

        if(distZ > 0){

            if(countZ > distZ-2){
                countZ = 0;
                countY--;
            }

        }

        if(distZ < 0){

            if(countZ < distZ+2){
                countZ = 0;
                countY--;
            }
        }

        //indicatorLocation.add(new Location(world, currentLocation.getX(), currentLocation.getY()-countY+1, currentLocation.getZ()));

    }


    private Material getMinedVerson(Block origin){

        if(origin.getType() == Material.DIAMOND_ORE){
            return Material.DIAMOND;
        } else if(origin.getType() == Material.COAL_ORE){
            return Material.COAL;
        } else if(origin.getType() == Material.EMERALD_ORE){
            return Material.EMERALD;
        } else if(origin.getType() == Material.REDSTONE_ORE){
            return Material.REDSTONE;
        } else if(origin.getType() == Material.STONE){
            return Material.COBBLESTONE;
        } else if(origin.getType() == Material.GRASS){
            return Material.DIRT;
        }

        return origin.getType();
    }

}
