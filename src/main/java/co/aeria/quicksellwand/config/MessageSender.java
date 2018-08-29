package co.aeria.quicksellwand.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageSender {

    private SettingsManager settings;

    public MessageSender(SettingsManager settings) {
        this.settings = settings;
    }

    public void send(CommandSender to, Property<String> key, Placeholder... placeholders) {
        String message = settings.getProperty(key);
        if (message.isEmpty()) {
            return;
        }
        for (Placeholder placeholder : placeholders) {
            message = message.replace(placeholder.key, String.valueOf(placeholder.value));
        }
        to.sendMessage(ChatColor.translateAlternateColorCodes('&',
            settings.getProperty(Messages.PREFIX) + message));
    }

    public static class Placeholder {

        private final String key;
        private final Object value;

        private Placeholder(String key, Object value) {
            this.key = "%" + key + "%";
            this.value = value;
        }

        public static Placeholder of(String key, Object value) {
            return new Placeholder(key, value);
        }
    }
}
