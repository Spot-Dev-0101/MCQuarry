package com.itzbradmc.mcquarry;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
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

    public int minedCount = 0;

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


        checkForChest();
        if(chest == null){
            player.sendMessage("A chest is required for a filler");
        }

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mcq, new Runnable() {
            @Override
            public void run() {

                if(active == true) {
                    work();
                }

            }
        }, 0, delay);

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
            if(chestInv.getItem(x) != null){
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
