package com.itzbradmc.mcquarry.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class mqCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(args.length >= 3){
            if(args[0].equalsIgnoreCase("give")){
                give(args[1], args[2]);
            }
        } else{
            commandSender.sendMessage("Invalid command");
        }


        return false;
    }

    private void give(String playerName, String level){

        Player player = Bukkit.getPlayer(playerName);
        ItemStack item = new ItemStack(Material.IRON_BLOCK);

        if(level.equalsIgnoreCase("1")){
            item = new ItemStack(Material.IRON_BLOCK);
        } else if(level.equalsIgnoreCase("2")){
            item = new ItemStack(Material.GOLD_BLOCK);
        } else if(level.equalsIgnoreCase("3")){
            item = new ItemStack(Material.DIAMOND_BLOCK);
        }
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eQuarry"));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Place this next to an area"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7bordered with redstone torches"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7and a chest"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        player.getInventory().addItem(item);


    }


}
