package com.itzbradmc.mcquarry.events;

import com.itzbradmc.mcquarry.MCQuarry;
import com.itzbradmc.mcquarry.Quarry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListen implements Listener {

    MCQuarry mcq;

    public BlockPlaceListen(MCQuarry mcq){
        this.mcq = mcq;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        Block leftBlock = new Location(player.getWorld(), event.getBlock().getX()+1, event.getBlock().getY(), event.getBlock().getZ()).getBlock();
        Block rightBlock = new Location(player.getWorld(), event.getBlock().getX()-1, event.getBlock().getY(), event.getBlock().getZ()).getBlock();
        Block northBlock = new Location(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()+1).getBlock();
        Block southBlock = new Location(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()-1).getBlock();


        if(leftBlock.getType().name() == "REDSTONE_TORCH"){
            checkForTorches(player, leftBlock, 1, 0, event.getBlock());
        } else if(rightBlock.getType().name() == "REDSTONE_TORCH"){
            checkForTorches(player, rightBlock, -1, 0, event.getBlock());
        } else if(northBlock.getType().name() == "REDSTONE_TORCH"){
            checkForTorches(player, northBlock, 0, 1, event.getBlock());
        } else if(southBlock.getType().name() == "REDSTONE_TORCH"){
            checkForTorches(player, southBlock, 0, -1, event.getBlock());
        }

    }

    private void checkForTorches(Player player, Block torch, int x, int z, Block placed){
        boolean foundSecond = false;
        boolean foundThird = false;
        boolean foundForth = false;

        Block secondTorch = torch;
        Block thrirdTorch = torch;
        Block forthTorch = torch;

        int secondX = z;
        int secondZ = x;

        player.sendMessage("First at "  + "   " + torch.getX() + " " + torch.getZ());

        for(int i = 1; i < 11; i++){
            Block currentBlock = new Location(player.getWorld(), torch.getX()+(x*i), torch.getY(), torch.getZ()+(z*i)).getBlock();
            //player.sendMessage("2" + currentBlock.getType().name());
            if(currentBlock.getType().name() == "REDSTONE_TORCH"){
                player.sendMessage("Found second  "  + "   " + currentBlock.getX() + " " + currentBlock.getZ());
                secondTorch = currentBlock;
                foundSecond = true;
                break;
            }
        }



        if(foundSecond == true){
            for(int i = 1; i < 11; i++){
                Block currentBlockLeft = new Location(player.getWorld(), secondTorch.getX()+(secondX*i), secondTorch.getY(), secondTorch.getZ()+(secondZ*i)).getBlock(); //left

                //player.sendMessage("3" + currentBlockLeft.getType().name() + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                if(currentBlockLeft.getType().name() == "REDSTONE_TORCH"){
                    player.sendMessage("Found third  " + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                    thrirdTorch = currentBlockLeft;
                    foundThird = true;
                    break;
                }

                Block currentBlockRight = new Location(player.getWorld(), secondTorch.getX()-(secondX*i), secondTorch.getY(), secondTorch.getZ()-(secondZ*i)).getBlock(); //right

                //player.sendMessage("3 " + currentBlockRight.getType().name() + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                if(currentBlockRight.getType().name() == "REDSTONE_TORCH"){
                    player.sendMessage("Found third " + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                    thrirdTorch = currentBlockRight;
                    foundThird = true;
                    break;
                }
            }
        }

        int thirdX = secondZ;
        int thirdY = secondX;

        if(foundThird == true){
            for(int i = 1; i < 11; i++){
                Block currentBlockLeft = new Location(player.getWorld(), thrirdTorch.getX()+(thirdX*i), thrirdTorch.getY(), thrirdTorch.getZ()+(thirdY*i)).getBlock(); //left

                //player.sendMessage("4" + currentBlockLeft.getType().name() + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                if(currentBlockLeft.getType().name() == "REDSTONE_TORCH"){
                    player.sendMessage("Found forth  " + "   " + currentBlockLeft.getX() + " " + currentBlockLeft.getZ());
                    forthTorch = currentBlockLeft;
                    foundForth = true;
                    break;
                }

                Block currentBlockRight = new Location(player.getWorld(), thrirdTorch.getX()-(thirdX*i), thrirdTorch.getY(), thrirdTorch.getZ()-(thirdY*i)).getBlock(); //right

                //player.sendMessage("4 " + currentBlockRight.getType().name() + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                if(currentBlockRight.getType().name() == "REDSTONE_TORCH"){
                    player.sendMessage("Found forth " + "   " + currentBlockRight.getX() + " " + currentBlockRight.getZ());
                    forthTorch = currentBlockRight;
                    foundForth = true;
                    break;
                }
            }
        }

        if(foundForth && foundSecond && foundThird){
            player.sendMessage("Found a successful quarry layout");

            MCQuarry.quarryList.add(new Quarry(torch, secondTorch, thrirdTorch, forthTorch, placed, mcq, x, z, player.getWorld(), player));

        }

    }

}
