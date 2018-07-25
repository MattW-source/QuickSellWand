package co.aeria.quicksellwand.config.properties;

import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.PropertyResource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnumSetProperty<E extends Enum<E>> extends Property<Set<Enum<E>>> {

    private final Class<E> clazz;

    private EnumSetProperty(Class<E> clazz, String path, Set<Enum<E>> defaultValue) {
        super(path, defaultValue);
        this.clazz = clazz;
    }

    @SafeVarargs
    public static <E extends Enum<E>> Property<Set<Enum<E>>> newEnumSetProperty(Class<E> clazz, String path, E... defaultValue) {
        return new EnumSetProperty<>(clazz, path, new HashSet<>(Arrays.asList(defaultValue)));
    }

    @Override
    protected Set<Enum<E>> getFromResource(PropertyResource resource) {
        Set<Enum<E>> result = new HashSet<>();
        List<?> rawList = resource.getList(getPath());
        if (rawList != null) {
            for (Object value : rawList) {
                if (clazz.isInstance(value)) {
                    result.add(clazz.cast(value));
                }
                if (value instanceof String) {
                    String textValue = (String) value;
                    result.add(mapToEnum(textValue));
                }
            }
            return result;
        }
        return null;
    }

    private E mapToEnum(String value) {
        for (E entry : clazz.getEnumConstants()) {
            if (entry.name().equalsIgnoreCase(value)) {
                return entry;
            }
        }
        return null;
    }
}
