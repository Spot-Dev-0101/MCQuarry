package com.itzbradmc.mcquarry;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    int countX = 0;
    int countY = 0;
    int countZ = 0;

    private Location currentLocation;

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

        checkForChest();

        Bukkit.getScheduler().runTaskTimer(mcq, new Runnable() {
            @Override
            public void run() {

                if(currentLocation.getY() < 3){
                    active = false;
                }

                if(active == true) {
                    work();
                }
            }
        }, 0, 10);

    }

    private void checkForChest(){
        Block leftBlock = new Location(world, controller.getX()+1, controller.getY(), controller.getZ()).getBlock();
        Block rightBlock = new Location(world, controller.getX()-1, controller.getY(), controller.getZ()).getBlock();
        Block northBlock = new Location(world, controller.getX(), controller.getY(), controller.getZ()+1).getBlock();
        Block southBlock = new Location(world, controller.getX(), controller.getY(), controller.getZ()-1).getBlock();

        if(leftBlock.getType().name() == "CHEST"){
            chest = (Chest) leftBlock.getState();
            player.sendMessage("Found chest");
        } else if(rightBlock.getType().name() == "CHEST"){
            chest = (Chest) rightBlock.getState();
            player.sendMessage("Found chest");
        } else if(northBlock.getType().name() == "CHEST"){
            chest = (Chest) northBlock.getState();
            player.sendMessage("Found chest");
        } else if(southBlock.getType().name() == "CHEST"){
            chest = (Chest) southBlock.getState();
            player.sendMessage("Found chest");
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

        currentLocation =  new Location(world,torch1.getX()+distX + countX, torch1.getY()-1 + countY, torch1.getZ()+distZ + countZ);

        //
        if(chest != null){
            //chest.getBlockInventory().addItem(new ItemStack(currentLocation.getBlock().getType()));
            chest.getInventory().addItem(new ItemStack(currentLocation.getBlock().getType()));
            //chest.update(true);
        } else{
            Location dropLocation = new Location(world, controller.getX(), controller.getY() + 1, controller.getZ());
            world.dropItem(controller.getLocation(), new ItemStack(currentLocation.getBlock().getType()));
        }



        currentLocation.getBlock().setType(Material.AIR);

        distX = torch3.getX() - torch1.getX();
        distZ = torch3.getZ() - torch1.getZ();


        if(distX > 0){
            player.sendMessage("X is getting higher " + distX + " " + countX);
            countX++;

            if(countX > distX-2){
                if(distZ > 0){
                    player.sendMessage("Z is getting higher");
                    countX = 0;
                    countZ++;
                }

                if(distZ < 0){
                    player.sendMessage("Z is getting lower");
                    countX = 0;
                    countZ--;
                }
            }
        }

        if(distX < 0){
            player.sendMessage("X is getting lower" + distX + " " + countX);
            countX--;

            if(countX < distX+2){
                if(distZ > 0){
                    player.sendMessage("Z  is getting higher" + distZ + " " + countZ);
                    countX = 0;
                    countZ++;
                }

                if(distZ < 0){
                    player.sendMessage("Z is getting lower" + distZ + " " + countZ);
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



    }

}
