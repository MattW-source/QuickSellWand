package co.aeria.quicksellwand.config;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;
import static co.aeria.quicksellwand.config.properties.EnumSetProperty.newEnumSetProperty;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import java.util.Set;
import org.bukkit.Material;

public final class MainConfig implements SettingsHolder {

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
    public static final Property<Set<Enum<Material>>> CONTAINERS =
        newEnumSetProperty(Material.class, "container-types",
            Material.CHEST, Material.TRAPPED_CHEST, Material.ENDER_CHEST);

}
