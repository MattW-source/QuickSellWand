package co.aeria.quicksellwand.config.properties;

import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.PropertyResource;
import org.bukkit.Material;

public class MaterialProperty extends Property<Material> {

    public MaterialProperty(String path, Material defaultValue) {
        super(path, defaultValue);
    }
    
    @Override
    protected Material getFromResource(PropertyResource resource) {
        // Value is read from file as a String, but when it is set later on it is an enum
        Object value = resource.getObject(getPath());
        if (value instanceof String) {
            String textValue = (String) value;
            return Material.getMaterial(textValue);
        }
        return null;
    }
}
