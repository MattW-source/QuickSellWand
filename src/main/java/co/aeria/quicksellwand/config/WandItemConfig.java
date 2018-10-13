package co.aeria.quicksellwand.config;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import co.aeria.quicksellwand.config.properties.MaterialProperty;
import java.util.List;
import org.bukkit.Material;

public class WandItemConfig implements SettingsHolder {

    @Comment({
        "Click type to use the wand. valid types:",
        "LEFT, RIGHT, ANY"})
    public static final Property<ClickType> CLICK = newProperty(ClickType.class, "wand-item.click-type", ClickType.RIGHT);

    @Comment("Wand usage cooldown in seconds. set 0 to disable")
    public static final Property<Integer> COOLDOWN = newProperty("wand-item.cooldown", 5);

    @Comment("Bukkit Material type for the item. https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    public static final Property<Material> TYPE = new MaterialProperty( "wand-item.type", Material.getMaterial("GOLD_HOE"));

    @Comment("Display name of item")
    public static final Property<String> NAME = newProperty("wand-item.name", "&2Sell Wand");

    @Comment("Enchantment glow effect")
    public static final Property<Boolean> GLOW = newProperty("wand-item.glow", true);

    @Comment("Text used if the wand usage is infinity")
    public static final Property<String> INFINITY_TEXT = newProperty("wand-item.infinity_text", "Infinity");

    @Comment({
        "The item lore",
        "Placeholders:",
        "%usage% : how many times left this wand can be used"})
    public static final Property<List<String>> LORE = newListProperty("wand-item.lore",
        "&7&m----------------------",
        "",
        "&fUsage Left: &7%usage%",
        "",
        "&fRight-Click container block with",
        "&fthis item to sell its contents",
        "");

    public enum ClickType {
        LEFT, RIGHT, ANY
    }

}
