package co.aeria.quicksellwand.config;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;
import static org.bukkit.Material.getMaterial;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import co.aeria.quicksellwand.config.properties.MaterialSetProperty;
import java.util.Set;
import org.bukkit.Material;

public final class MainConfig implements SettingsHolder {
    @Comment("Show debug info")
    public static final Property<Boolean> DEBUG = newProperty("debug", false);

    @Comment({
        "Shop Plugin used for selling the items",
        "Supported Plugins:",
        "  ShopGUIPlus"
    })
    public static final Property<String> SHOP_PLUGIN = newProperty("shop-plugin", "");

    @Comment({
        "Allowed block types. Use bukkit material type. https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html",
        "the block type must contains Inventory"
    })
    public static final Property<Set<Material>> CONTAINERS =
        MaterialSetProperty.newProperty("container-types",
            getMaterial("CHEST"), getMaterial("TRAPPED_CHEST"), getMaterial("ENDER_CHEST"));

}
