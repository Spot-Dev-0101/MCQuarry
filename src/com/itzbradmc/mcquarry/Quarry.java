package com.itzbradmc.mcquarry;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Quarry {

    private Block torch1;
    private Block torch2;
    private Block torch3;
    private Block torch4;

    private Block controller;

    private World world;

    private int x = 0;
    private int z = 0;

    int countX = 0;
    int countY = 0;
    int countZ = 0;

    private Location currentLocation;

    public Quarry(Block torch1, Block torch2, Block torch3, Block torch4, Block controller, MCQuarry mcq, int x, int z, World world){
        this.torch1 = torch1;
        this.torch2 = torch2;
        this.torch3 = torch3;
        this.torch4 = torch4;
        this.controller = controller;
        this.x = x;
        this.z = z;
        this.world = world;

        currentLocation = torch1.getLocation();

        Bukkit.getScheduler().runTaskTimer(mcq, new Runnable() {
            @Override
            public void run() {
                work();
            }
        }, 0, 10);

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
        currentLocation.getBlock().setType(Material.AIR);

        distX = (torch3.getX() - torch1.getX());
        distZ = (torch3.getZ() - torch1.getZ());

        //if(distX < x){
            ///distX = -distX;
        //}
       // if(distZ < z){
            //distZ = -distZ;
        //}

        int tempCountZ = countZ;
        int tempCountX = countX;

        if(tempCountZ < 0){
            tempCountZ = -tempCountZ+2;
        } else{
            tempCountZ -= 2;
        }
        if(tempCountX < 0){
            tempCountX = -tempCountX-2;
        } else{
            tempCountX += 2;
        }

        MCQuarry.logger.info(countZ + " " + countX + " " + countY + " " + distX + " " + distZ);
        if(!(tempCountZ > distZ && tempCountZ < -distZ)){
            countX++;
            countZ = 0;
        } else {
            //countX += x;
            countZ += z;
        }

        if(tempCountX > distX){
            countY--;
            countX = 0;
            countZ = 0;
        }

    }

}
