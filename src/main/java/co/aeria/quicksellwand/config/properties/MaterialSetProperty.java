package co.aeria.quicksellwand.config.properties;

import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.PropertyResource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;

public class MaterialSetProperty extends Property<Set<Material>> {

    private MaterialSetProperty(String path, Set<Material> defaultValue) {
        super(path, defaultValue);
    }

    public static Property<Set<Material>> newProperty(String path,
        Material... defaultValue) {
        return new MaterialSetProperty(path, new HashSet<>(Arrays.asList(defaultValue)));
    }

    @Override
    protected Set<Material> getFromResource(PropertyResource resource) {
        Set<Material> result = new HashSet<>();
        List<?> rawList = resource.getList(getPath());
        if (rawList != null) {
            for (Object value : rawList) {
                if (value instanceof String) {
                    String textValue = (String) value;
                    Material material = Material.getMaterial(textValue);
                    if (material != null) {
                        result.add(material);
                    }
                }
            }
            return result;
        }
        return null;
    }
}