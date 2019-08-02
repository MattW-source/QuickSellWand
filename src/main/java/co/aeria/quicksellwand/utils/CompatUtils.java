package co.aeria.quicksellwand.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public final class CompatUtils {

    public static Sound ITEM_BREAK_SOUND;
    private static boolean mMaterial_IsItem;
    private static boolean mPlayerInventory_setItemInMainHand;
    private static boolean mPlayerInteractEvent_getHand;
    private static boolean mItemMeta_setUnbreakable;

    static {
        for (Sound sound : Sound.class.getEnumConstants()) {
            if (sound.name().equals("ITEM_BREAK") || sound.name().equals("ENTITY_ITEM_BREAK")) {
                ITEM_BREAK_SOUND = sound;
                break;
            }
        }

        try {
            Material.class.getMethod("isItem");
            mMaterial_IsItem = true;
        } catch (NoSuchMethodException ex) {
            mMaterial_IsItem = false;
        }

        try {
            PlayerInventory.class.getMethod("setItemInMainHand", ItemStack.class);
            mPlayerInventory_setItemInMainHand = true;
        } catch (NoSuchMethodException ex) {
            mPlayerInventory_setItemInMainHand = false;
        }

        try {
            PlayerInteractEvent.class.getMethod("getHand");
            mPlayerInteractEvent_getHand = true;
        } catch (NoSuchMethodException ex) {
            mPlayerInteractEvent_getHand = false;
        }

        try {
            ItemMeta.class.getMethod("setUnbreakable", boolean.class);
            mItemMeta_setUnbreakable = true;
        } catch (NoSuchMethodException ex) {
            mItemMeta_setUnbreakable = false;
        }
    }

    private CompatUtils() {
    }

    public static boolean materialIsItem(Material material) {
        if (mMaterial_IsItem) {
            material.isItem();
        }
        return material != Material.WATER
            && material != Material.STATIONARY_WATER
            && material != Material.LAVA
            && material != Material.STATIONARY_LAVA
            && material != Material.BED_BLOCK
            && material != Material.PISTON_EXTENSION
            && material != Material.PISTON_MOVING_PIECE
            && material != Material.DOUBLE_STEP && material != Material.FIRE
            && material != Material.REDSTONE_WIRE && material != Material.CROPS
            && material != Material.BURNING_FURNACE
            && material != Material.SIGN_POST
            && material != Material.WOODEN_DOOR
            && material != Material.WALL_SIGN
            && material != Material.IRON_DOOR_BLOCK
            && material != Material.GLOWING_REDSTONE_ORE
            && material != Material.REDSTONE_TORCH_OFF
            && material != Material.SUGAR_CANE_BLOCK
            && material != Material.PORTAL
            && material != Material.CAKE_BLOCK
            && material != Material.DIODE_BLOCK_OFF
            && material != Material.DIODE_BLOCK_ON
            && material != Material.PUMPKIN_STEM
            && material != Material.MELON_STEM
            && material != Material.NETHER_WARTS
            && material != Material.BREWING_STAND
            && material != Material.CAULDRON
            && material != Material.ENDER_PORTAL
            && material != Material.REDSTONE_LAMP_ON
            && material != Material.WOOD_DOUBLE_STEP
            && material != Material.COCOA
            && material != Material.TRIPWIRE
            && material != Material.FLOWER_POT
            && material != Material.CARROT
            && material != Material.POTATO
            && material != Material.SKULL
            && material != Material.REDSTONE_COMPARATOR_OFF
            && material != Material.REDSTONE_COMPARATOR_ON
            && material != Material.STANDING_BANNER
            && material != Material.WALL_BANNER
            && material != Material.DAYLIGHT_DETECTOR_INVERTED
            && material != Material.DOUBLE_STONE_SLAB2
            && material != Material.SPRUCE_DOOR
            && material != Material.BIRCH_DOOR
            && material != Material.JUNGLE_DOOR
            && material != Material.ACACIA_DOOR
            && material != Material.DARK_OAK_DOOR;
    }

    public static boolean isNotMainHand(PlayerInteractEvent event) {
        if (mPlayerInteractEvent_getHand) {
            return event.getHand() != EquipmentSlot.HAND;
        }
        return false;
    }

    public static void setItemInHand(Player player, ItemStack newWand) {
        if (mPlayerInventory_setItemInMainHand) {
            player.getInventory().setItemInMainHand(newWand);
        } else {
            player.setItemInHand(newWand);
        }
    }

    public static void setItemMetaUnbreakable(ItemMeta itemMeta) {
        if (mItemMeta_setUnbreakable) {
            itemMeta.setUnbreakable(true);
        } else {
            itemMeta.spigot().setUnbreakable(true);
        }
    }

    public static Material getMaterial(String name, String fallbackName) {
        Material m = Material.getMaterial(name);
        if (m != null)
            return m;
        return Material.getMaterial(fallbackName);
    }
}
