package com.itzbradmc.mcquarry.events;

import com.itzbradmc.mcquarry.MCQuarry;
import com.itzbradmc.mcquarry.Quarry;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuInteractionListen implements Listener {

    @EventHandler
    public void onMenuPress(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory() != null) {

            if (event.getClickedInventory().getName().equals("Quarry menu")) {

                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cUpgrades"))) {
                    player.sendMessage("Open upgrades menu");

                    Quarry quarry = getQuarry(event.getClickedInventory(), 13, player.getWorld());

                    createUpgradeMenu(quarry, player);

                }

                event.setCancelled(true);
            } else if (event.getClickedInventory().getName().equals("Quarry upgrades")) {

                if (event.getSlot() == 40) {
                    player.sendMessage(ChatColor.stripColor(event.getCursor().getItemMeta().getDisplayName()) + "s");
                    Quarry quarry = getQuarry(event.getClickedInventory(), 4, player.getWorld());
                    if (ChatColor.stripColor(event.getCursor().getItemMeta().getDisplayName()).equals("Double drops")) {
                        quarry.doubleDrops = true;
                        player.sendMessage("Activated Double Drops upgrade");
                        //event.getClickedInventory().setItem(40, new ItemStack(Material.AIR));
                        //player.closeInventory();
                        event.getCursor().setType(Material.AIR);
                        createUpgradeMenu(quarry, player);
                    } else if (ChatColor.stripColor(event.getCursor().getItemMeta().getDisplayName()).equals("Random diamond")) {
                        quarry.doubleDrops = true;
                        player.sendMessage("Activated Random Diamond upgrade");
                        //event.getClickedInventory().setItem(40, new ItemStack(Material.AIR));
                        //player.closeInventory();
                        event.getCursor().setType(Material.AIR);
                        createUpgradeMenu(quarry, player);
                    } else {
                        player.sendMessage("Invalid upgrade item");
                        event.setCancelled(true);
                    }

                }

                if (event.getSlot() != 40) {
                    event.setCancelled(true);
                }
            }
        }

    }

    private void createUpgradeMenu(Quarry quarry, Player player){
        Inventory menu = Bukkit.createInventory(null, 54, "Quarry upgrades");

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

        List<String> lore = new ArrayList<>();
        lore.add(quarry.controller.getX()+"");
        lore.add(quarry.controller.getY()+"");
        lore.add(quarry.controller.getZ()+"");

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        menu.setItem(4, item);

        //10, 12, 14, 16
        item = new ItemStack(Material.EMERALD);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eDouble drops"));
        lore = new ArrayList<>();
        if(quarry.doubleDrops == true){
            lore.add(ChatColor.translateAlternateColorCodes('&', "&aActivated"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cUnactivated"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        menu.setItem(10, item);


        item = new ItemStack(Material.DIAMOND);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eRandom diamond"));
        lore = new ArrayList<>();
        if(quarry.randomDiamond == true){
            lore.add(ChatColor.translateAlternateColorCodes('&', "&aActivated"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cUnactivated"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        menu.setItem(12, item);



        item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e??????"));
        lore = new ArrayList<>();
        if(false){
            lore.add(ChatColor.translateAlternateColorCodes('&', "&aActivated"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cUnactivated"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        menu.setItem(14, item);



        item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e?????"));
        lore = new ArrayList<>();
        if(false == true){
            lore.add(ChatColor.translateAlternateColorCodes('&', "&aActivated"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&cUnactivated"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        menu.setItem(16, item);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        item = new ItemStack(Material.BOOK);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aUpgrade Item"));
        lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Place a quarry upgrade"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7item in the slot below"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        menu.setItem(31, item);

        item = new ItemStack(Material.AIR);
        menu.setItem(40, item);



        player.openInventory(menu);
    }

    private Quarry getQuarry(Inventory clickedInv, int index, World world){
        ItemStack item = clickedInv.getItem(index);

        int x = Integer.valueOf(item.getItemMeta().getLore().get(0));
        int y = Integer.valueOf(item.getItemMeta().getLore().get(1));
        int z = Integer.valueOf(item.getItemMeta().getLore().get(2));

        Location controllerLocation = new Location(world, x, y, z);

        Quarry quarry = MCQuarry.quarryList.get(controllerLocation);
        return quarry;
    }

}
