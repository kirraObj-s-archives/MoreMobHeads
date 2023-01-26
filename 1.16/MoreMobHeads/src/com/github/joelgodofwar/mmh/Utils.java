package com.github.joelgodofwar.mmh;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    @NotNull
    public static ItemStack getKilledSkull(ItemStack originItem, LivingEntity entity, Player killer) {
        ItemStack item = originItem.clone();
        ItemMeta meta = item.getItemMeta();
        setKilledName(meta, entity.getCustomName());
        addKilledLore(meta, killer.getKiller());
        item.setItemMeta(meta);
        return item;
    }

    @NotNull
    public static ItemStack getReplaceTimeSkull(ItemStack originItem) {
        if (!MoreMobHeads.getInstance().getConfig().getBoolean("time.enable")) {
            return originItem;
        }
        ItemStack item = originItem.clone();
        ItemMeta meta = item.getItemMeta();
        addReplacedTimeLore(meta);
        item.setItemMeta(meta);
        return item;
    }

    private static void setKilledName(ItemMeta meta, String entityName) {
        meta.setDisplayName(colored(MoreMobHeads.getInstance().getConfig().getString("player_skull.name").replace("{player_name}", entityName)));
    }

    private static void addKilledLore(ItemMeta meta, Player killer) {
        List<String> lore = meta.getLore();
        lore.addAll(MoreMobHeads.getInstance().getConfig().getStringList("player_skull.lore")
                .stream()
                .map(it -> it.replace("{player_name}", killer.getCustomName()))
                .map(it -> PlaceholderAPI.setPlaceholders(killer, it))
                .map(Utils::colored)
                .collect(Collectors.toList()));
        meta.setLore(lore);
    }

    private static void addReplacedTimeLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        lore.add(colored(DateFormatUtils.format(System.currentTimeMillis(), MoreMobHeads.getInstance().getConfig().getString("time.value"))));
        meta.setLore(lore);
    }

    private static String colored(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
