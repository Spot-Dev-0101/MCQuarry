package com.itzbradmc.mcquarry.events;

import com.itzbradmc.mcquarry.Filler;
import com.itzbradmc.mcquarry.MCQuarry;
import com.itzbradmc.mcquarry.Quarry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockPlaceListen implements Listener {

    MCQuarry mcq;

    public BlockPlaceListen(MCQuarry mcq){
        this.mcq = mcq;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemMeta im = player.getItemInHand().getItemMeta();

        Block leftBlock = new Location(player.getWorld(), event.getBlock().getX() + 1, event.getBlock().getY(), event.getBlock().getZ()).getBlock();
        Block rightBlock = new Location(player.getWorld(), event.getBlock().getX() - 1, event.getBlock().getY(), event.getBlock().getZ()).getBlock();
        Block northBlock = new Location(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ() + 1).getBlock();
        Block southBlock = new Location(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ() - 1).getBlock();

        if(im.getDisplayName().equals(ChatColor.YELLOW + "Quarry")) {

            if (leftBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, leftBlock, 1, 0, event.getBlock(), false);
            } else if (rightBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, rightBlock, -1, 0, event.getBlock(), false);
            } else if (northBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, northBlock, 0, 1, event.getBlock(), false);
            } else if (southBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, southBlock, 0, -1, event.getBlock(), false);
            }

        } else if(im.getDisplayName().equals(ChatColor.YELLOW + "Filler")){
            if (leftBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, leftBlock, 1, 0, event.getBlock(), true);
            } else if (rightBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, rightBlock, -1, 0, event.getBlock(), true);
            } else if (northBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, northBlock, 0, 1, event.getBlock(), true);
            } else if (southBlock.getType().name() == "REDSTONE_TORCH") {
                checkForTorches(player, southBlock, 0, -1, event.getBlock(), true);
            }
        }

    }

    private void checkForTorches(Player player, Block torch, int x, int z, Block placed, boolean filler){
        boolean foundSecond = false;
        boolean foundThird = false;
        boolean foundForth = false;

        int size = MCQuarry.config.getInt("maxSize");

        Block secondTorch = torch;
        Block thrirdTorch = torch;
        Block forthTorch = torch;

        int secondX = z;
        int secondZ = x;

        //player.sendMessage("First at "  + "   " + torch.getX() + " " + torch.getZ());

        for(int i = 1; i < size+1; i++){
            Block currentBlock = new Location(player.getWorld(), torch.getX()+(x*i), torch.getY(), torch.getZ()+(z*i)).getBlock();
            //player.sendMessage("2" + currentBlock.getType().name());
            if(currentBlock.getType().name() == "REDSTONE_TORCH"){
                //player.sendMessage("Found second  "  + "   " + currentBlock.getX() + " " + currentBlock.getZ());
                secondTorch = currentBlock;
                foundSecond = true;
                break;
            }
        }



        if(foundSecond == true){
            for(int i = 1; i < size+1; i++){
                Block currentBlockLeft = new Location(player.getWorld(), secondTorch.getX()+(secondX*i), secondTorch.getY(), secondTorch.getZ()+(secondZ*i)).getBlock(); //left

                //player.sendMessage("3" + currentBlockLeft.getType().name() + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                if(currentBlockLeft.getType().name() == "REDSTONE_TORCH"){
                    //player.sendMessage("Found third  " + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                    thrirdTorch = currentBlockLeft;
                    foundThird = true;
                    break;
                }

                Block currentBlockRight = new Location(player.getWorld(), secondTorch.getX()-(secondX*i), secondTorch.getY(), secondTorch.getZ()-(secondZ*i)).getBlock(); //right

                //player.sendMessage("3 " + currentBlockRight.getType().name() + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                if(currentBlockRight.getType().name() == "REDSTONE_TORCH"){
                    //player.sendMessage("Found third " + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                    thrirdTorch = currentBlockRight;
                    foundThird = true;
                    break;
                }
            }
        }

        int thirdX = secondZ;
        int thirdY = secondX;

        if(foundThird == true){
            for(int i = 1; i < size+1; i++){
                Block currentBlockLeft = new Location(player.getWorld(), thrirdTorch.getX()+(thirdX*i), thrirdTorch.getY(), thrirdTorch.getZ()+(thirdY*i)).getBlock(); //left

                //player.sendMessage("4" + currentBlockLeft.getType().name() + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                if(currentBlockLeft.getType().name() == "REDSTONE_TORCH"){
                    //player.sendMessage("Found forth  " + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                    forthTorch = currentBlockLeft;
                    foundForth = true;
                    break;
                }

                Block currentBlockRight = new Location(player.getWorld(), thrirdTorch.getX()-(thirdX*i), thrirdTorch.getY(), thrirdTorch.getZ()-(thirdY*i)).getBlock(); //right

                //player.sendMessage("4 " + currentBlockRight.getType().name() + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                if(currentBlockRight.getType().name() == "REDSTONE_TORCH"){
                    //player.sendMessage("Found forth " + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                    forthTorch = currentBlockRight;
                    foundForth = true;
                    break;
                }
            }
        }

        if(foundForth && foundSecond && foundThird){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("foundLayout")));

            if(filler == true){
                findAboveTorches(torch, secondTorch, thrirdTorch, forthTorch, player, x, z, placed);
            } else {
                MCQuarry.quarryList.put(placed.getLocation(), new Quarry(torch, secondTorch, thrirdTorch, forthTorch, placed, mcq, x, z, player.getWorld(), player, false));
            }

        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("failedLayout")));
        }

    }

    private void findAboveTorches(Block torch1, Block torch2, Block torch3, Block torch4, Player player, int x, int z, Block placed){
        player.sendMessage("Searching for above filler torches");
        int size = 10;
        int height = 0;

        Block firstTorch = torch1;
        Block secondTorch = torch1;
        Block thirdTorch = torch1;
        Block forthTorch = torch1;

        for(int i = 1; i < size+1; i++){
            Block block = new Location(torch1.getWorld(), torch1.getX(), torch1.getY()+i, torch1.getZ()).getBlock();
            if(block.getType() == Material.REDSTONE_WALL_TORCH){
                height = i;
                firstTorch = block;
            }
        }
        if(height == 0){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("failedLayout")));
            //return false;
        }

        Location loc = new Location(player.getWorld(), torch2.getX(), torch2.getY()+height, torch2.getZ());
        if(loc.getBlock().getType() == Material.REDSTONE_WALL_TORCH){
            secondTorch = loc.getBlock();
        }

        loc = new Location(player.getWorld(), torch3.getX(), torch3.getY()+height, torch3.getZ());
        if(loc.getBlock().getType() == Material.REDSTONE_WALL_TORCH){
            thirdTorch = loc.getBlock();
        }

        loc = new Location(player.getWorld(), torch4.getX(), torch4.getY()+height, torch4.getZ());
        if(loc.getBlock().getType() == Material.REDSTONE_WALL_TORCH){
            forthTorch = loc.getBlock();
        }

        if(firstTorch != torch1 && secondTorch != torch1 && thirdTorch != torch1 && forthTorch != torch1) {
            MCQuarry.fillerList.put(placed.getLocation(), new Filler(torch1, torch2, torch3, torch4, firstTorch, secondTorch, thirdTorch, forthTorch, placed, mcq, x, z, player.getWorld(), player, false));
            player.sendMessage("created a filler");
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MCQuarry.config.getString("failedLayout")));
        }
        //return true;
    }

}
