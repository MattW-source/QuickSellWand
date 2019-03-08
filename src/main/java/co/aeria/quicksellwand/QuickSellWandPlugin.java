package co.aeria.quicksellwand;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import co.aeria.quicksellwand.api.QuickSellWand;
import co.aeria.quicksellwand.api.ShopService;
import co.aeria.quicksellwand.cmd.QuickSellWandCommand;
import co.aeria.quicksellwand.config.MainConfig;
import co.aeria.quicksellwand.config.MessageSender;
import co.aeria.quicksellwand.config.Messages;
import co.aeria.quicksellwand.config.WandItemConfig;
import co.aeria.quicksellwand.listener.PlayerListener;
import co.aeria.quicksellwand.service.WandService;
import co.aeria.quicksellwand.service.shop.EssentialsWorthShopService;
import co.aeria.quicksellwand.service.shop.ShopGUIPlusService;
import com.google.common.base.Preconditions;
import java.io.File;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuickSellWandPlugin extends JavaPlugin implements QuickSellWand {

    private SettingsManager settings;
    private WandService wandService;
    private ShopService shopService;
    private MessageSender messageSender;

    private SettingsManager initSettings() {
        saveDefaultConfig();
        PropertyResource resource = new YamlFileResource(new File(getDataFolder(), "config.yml"));
        return new SettingsManager(resource, new PlainMigrationService(),
            MainConfig.class, WandItemConfig.class, Messages.class);
    }

    private ShopService initShopService() {
        String pluginName = settings.getProperty(MainConfig.SHOP_PLUGIN);
        Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            if (!plugin.isEnabled()) {
                messageSender.send(Bukkit.getConsoleSender(), Messages.SHOP_PLUGIN_DISABLED);
                return null;
            }
            ShopService service = null;
            switch (plugin.getName()) {
                case "ShopGUIPlus":
                    service = new ShopGUIPlusService(this, plugin);
                    break;
                case "Essentials":
                    service = new EssentialsWorthShopService(this, plugin);
                    break;

            }
            if (service != null) {
                getLogger().info("Using '" + plugin.getName() + "' as Shop Plugin");
                return service;
            }
        }
        messageSender.send(Bukkit.getConsoleSender(), Messages.NO_SHOP_PLUGIN);
        return null;
    }

    @Override
    public void onEnable() {
        settings = initSettings();
        settings.save();

        messageSender = new MessageSender(settings);

        shopService = initShopService();
        wandService = new WandService(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getCommand("quicksellwand").setExecutor(new QuickSellWandCommand(this));

    }

    @Override
    public void onDisable() {
        shopService = null;
    }

    public void reload() {
        settings.reload();
        shopService = initShopService();
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public WandService getWandService() {
        return wandService;
    }

    @Override
    public Optional<ShopService> getShopService() {
        return Optional.ofNullable(shopService);
    }

    @Override
    public void setShopService(ShopService shopService) {
        Preconditions.checkNotNull(shopService);
        this.shopService = shopService;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }
}
