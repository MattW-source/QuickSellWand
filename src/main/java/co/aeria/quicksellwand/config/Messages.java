package co.aeria.quicksellwand.config;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

public final class Messages implements SettingsHolder {

    public static final Property<String> PREFIX = newProperty("messages.prefix",
        "&7[&bQuickSellWand&7] &f");
    public static final Property<String> SHOP_PLUGIN_DISABLED = newProperty("messages.shop_plugin_disabled",
        "&cThe Shop Plugin is not enabled!");
    public static final Property<String> NO_SHOP_PLUGIN = newProperty("messages.no_shop_plugin",
        "&cThe shop plugin is not found or configured. Please configure it in config.yml");
    public static final Property<String> NO_USE_WAND = newProperty("messages.no_use_wand",
        "&cYou are not allowed to use the sell wand");
    public static final Property<String> WAND_ON_COOLDOWN = newProperty("messages.wand_on_cooldown",
        "&cThe wand is on cooldown for &f%cooldown% &csecond(s)");
    public static final Property<String> NO_PERMISSION = newProperty("messages.no_permission",
        "&cYou don't have permission to use this command");
    public static final Property<String> CONFIG_RELOADED = newProperty("messages.config_reloaded",
        "&aConfigurations reloaded.");
    public static final Property<String> PLAYER_NOT_FOUND = newProperty("messages.player_not_found",
        "&cCan't find that player");
    public static final Property<String> INVENTORY_FULL = newProperty("messages.inventory_full",
        "&cCan't add sell wand to player's inventory. The inventory is full!");
    public static final Property<String> WAND_ADDED = newProperty("messages.wand_added",
        "&eA sell wand added to player's inventory.");
    public static final Property<String> ITEMS_SOLD = newProperty("messages.items_sold",
        "&eSold items inside chest to the server shop for &b$%price%");
    public static final Property<String> NO_ITEMS_TO_SELL = newProperty("messages.no_items_to_sell",
        "&eThere is nothing inside this container that can be sold");

}
