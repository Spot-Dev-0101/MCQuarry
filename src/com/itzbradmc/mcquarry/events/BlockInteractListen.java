package com.itzbradmc.mcquarry.events;

import com.itzbradmc.mcquarry.MCQuarry;
import com.itzbradmc.mcquarry.Quarry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockInteractListen implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType() == Material.IRON_BLOCK || event.getClickedBlock().getType() == Material.GOLD_BLOCK || event.getClickedBlock().getType() == Material.DIAMOND_BLOCK){
                if(MCQuarry.quarryList.containsKey(event.getClickedBlock().getLocation())){
                    player.sendMessage("This is a quarry block");
                    openMenu(MCQuarry.quarryList.get(event.getClickedBlock().getLocation()), player);
                }
            }
        }
    }

    private void openMenu(Quarry quarry, Player player){
        Inventory menu = Bukkit.createInventory(null, 54, "Quarry menu");

        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        for(int x = 0; x < 54; x++){
            menu.setItem(x, item);
        }

        item = new ItemStack(Material.CLOCK);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Total mined: " + quarry.minedCount);
        item.setItemMeta(itemMeta);
        menu.setItem(13, item);

        int count = 0;

        for (Material block: quarry.eachBlockCount.keySet()) {

            item = new ItemStack(block);
            itemMeta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Mined: &e" + quarry.eachBlockCount.get(block)));

            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);

            if(19+count == 26){
                count += 2;
            } else if(19+count == 35){
                count += 2;
            } else if(19+count == 44){
                count += 2;
            }
            if(19+count < 44 ) {
                menu.setItem(19 + count, item);
            }
            count++;
        }

        player.openInventory(menu);
    }

}
