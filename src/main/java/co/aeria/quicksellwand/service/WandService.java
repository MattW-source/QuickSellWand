package co.aeria.quicksellwand.service;

import ch.jalu.configme.SettingsManager;
import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.config.WandItemConfig;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import utils.nbt.NBTItem;

public class WandService {

    private static final String TAG_USAGE = "tag.QuickSellWand.Usage";
    private static final String TAG_LAST_USE = "tag.QuickSellWand.LastUse";

    private QuickSellWandPlugin plugin;
    private SettingsManager settings;

    public WandService(QuickSellWandPlugin plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
    }

    public static boolean isWand(ItemStack item) {
        Boolean hasKey = new NBTItem(item).hasKey(TAG_USAGE);
        if (hasKey == null) {
            return false;
        }
        return hasKey;
    }

    public ItemStack getNewWand(int usage) {
        String name = settings.getProperty(WandItemConfig.NAME);
        name = ChatColor.translateAlternateColorCodes('&', name);
        List<String> lore = new ArrayList<>();
        for (String s : settings.getProperty(WandItemConfig.LORE)) {
            s = s.replace("%usage%", usage > 0 ? String.valueOf(usage) :
                settings.getProperty(WandItemConfig.INFINITY_TEXT));
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        Material type = settings.getProperty(WandItemConfig.TYPE);
        if (!type.isItem()) {
            type = Material.GOLD_HOE;
            plugin.getLogger().warning("Error while creating a new sell wand, the material type is not an item");
        }
        ItemStack item = new ItemStack(type);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.setUnbreakable(true);
        if (settings.getProperty(WandItemConfig.GLOW)) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setLong(TAG_LAST_USE, System.currentTimeMillis());
        nbtItem.setInteger(TAG_USAGE, usage);

        return nbtItem.getItem();
    }

    public ItemStack useWand(Player player, ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        int usage = nbtItem.getInteger(TAG_USAGE);
        if (usage < 0) {
            return getNewWand(usage);
        }

        int usageLeft = usage - 1;
        if (usageLeft == 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 0);
            return new ItemStack(Material.AIR);
        }
        return getNewWand(usageLeft);
    }

    public boolean isOnCooldown(ItemStack item) {
        int cooldown = settings.getProperty(WandItemConfig.COOLDOWN);
        if (cooldown > 0) {
            NBTItem nbtItem = new NBTItem(item);
            long lastUse = nbtItem.getLong(TAG_LAST_USE);
            return (System.currentTimeMillis() - lastUse) < (cooldown * 1000);
        }
        return false;
    }

    public int getCooldownLeft(ItemStack item) {
        int cooldown = settings.getProperty(WandItemConfig.COOLDOWN);
        if (cooldown > 0) {
            NBTItem nbtItem = new NBTItem(item);
            long lastUse = nbtItem.getLong(TAG_LAST_USE);
            return cooldown - (int) ((System.currentTimeMillis() - lastUse) / 1000);
        }
        return 0;
    }


}
