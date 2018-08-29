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
        switch (material) {
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
            case BED_BLOCK:
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case DOUBLE_STEP:
            case FIRE:
            case REDSTONE_WIRE:
            case CROPS:
            case BURNING_FURNACE:
            case SIGN_POST:
            case WOODEN_DOOR:
            case WALL_SIGN:
            case IRON_DOOR_BLOCK:
            case GLOWING_REDSTONE_ORE:
            case REDSTONE_TORCH_OFF:
            case SUGAR_CANE_BLOCK:
            case PORTAL:
            case CAKE_BLOCK:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case NETHER_WARTS:
            case BREWING_STAND:
            case CAULDRON:
            case ENDER_PORTAL:
            case REDSTONE_LAMP_ON:
            case WOOD_DOUBLE_STEP:
            case COCOA:
            case TRIPWIRE:
            case FLOWER_POT:
            case CARROT:
            case POTATO:
            case SKULL:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case STANDING_BANNER:
            case WALL_BANNER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case DOUBLE_STONE_SLAB2:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
                return false;
            default:
                return true;
        }
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
}
