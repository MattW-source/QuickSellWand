package co.aeria.quicksellwand.listener;

import ch.jalu.configme.SettingsManager;
import co.aeria.quicksellwand.Perms;
import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.api.SellResult;
import co.aeria.quicksellwand.api.SellResult.SellResultType;
import co.aeria.quicksellwand.api.ShopService;
import co.aeria.quicksellwand.config.MainConfig;
import co.aeria.quicksellwand.config.MessageSender;
import co.aeria.quicksellwand.config.MessageSender.Placeholder;
import co.aeria.quicksellwand.config.Messages;
import co.aeria.quicksellwand.config.WandItemConfig;
import co.aeria.quicksellwand.config.WandItemConfig.ClickType;
import co.aeria.quicksellwand.service.WandService;
import co.aeria.quicksellwand.utils.CompatUtils;
import java.util.Arrays;
import java.util.Optional;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private QuickSellWandPlugin plugin;
    private SettingsManager settings;
    private MessageSender msg;

    public PlayerListener(QuickSellWandPlugin plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
        this.msg = plugin.getMessageSender();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if ((event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            || CompatUtils.isNotMainHand(event)
            || !WandService.isWand(item)) {
            return;
        }
        event.setUseItemInHand(Result.DENY);

        // compare click type
        ClickType clickType = settings.getProperty(WandItemConfig.CLICK);
        Action action = event.getAction();
        if (clickType != ClickType.ANY) {
            if (clickType == ClickType.LEFT && action != Action.LEFT_CLICK_BLOCK) {
                if (settings.getProperty(MainConfig.DEBUG)) {
                    plugin.getLogger().info("Skip use wand with ClickType: Left, Action: " + action);
                }
                return;
            }
            if (clickType == ClickType.RIGHT && action != Action.RIGHT_CLICK_BLOCK) {
                if (settings.getProperty(MainConfig.DEBUG)) {
                    plugin.getLogger().info("Skip use wand with ClickType: Right, Action: " + action);
                }
                return;
            }
        }

        event.setCancelled(true);

        BlockState blockState = event.getClickedBlock().getState();
        if (!(blockState instanceof InventoryHolder)) {
            if (settings.getProperty(MainConfig.DEBUG)) {
                plugin.getLogger().info("BlockState isn't instance of InventoryHolder");
            }
            return;
        }

        if (!settings.getProperty(MainConfig.CONTAINERS).contains(blockState.getType())) {
            if (settings.getProperty(MainConfig.DEBUG)) {
                plugin.getLogger().info("BlockState type isn't whitelisted");
            }
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(Perms.USE_WAND)) {
            msg.send(player, Messages.NO_USE_WAND);
            return;
        }

        // Prevent people selling contents of chests on other peoples islands
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
        Location playerLocation = player.getLocation();
        Island island = SuperiorSkyblockAPI.getGrid().getIslandAt(playerLocation);
        if(!island.isMember(superiorPlayer)) {
            msg.send(player, Messages.NOT_ISLAND_MEMBER);
            return;
        }


        WandService wandService = plugin.getWandService();
        if (!player.hasPermission(Perms.NO_USE_COOLDOWN) && wandService.isOnCooldown(item)) {
            msg.send(player, Messages.WAND_ON_COOLDOWN, Placeholder.of("cooldown", wandService.getCooldownLeft(item)));
            return;
        }

        Inventory inv = ((InventoryHolder) blockState).getInventory();
        Optional<ShopService> shopService = plugin.getShopService();
        if (shopService.isPresent()) {
            SellResult result = shopService.get().sell(player, Arrays.asList(inv.getContents()));
            if (result.getResultType() == SellResultType.SOLD) {
                inv.removeItem(result.getSoldItems().toArray(new ItemStack[0]));
                ItemStack newWand = wandService.useWand(player, item);
                CompatUtils.setItemInHand(player, newWand);
                msg.send(player, Messages.ITEMS_SOLD, Placeholder.of("price", String.format("%,.2f", result.getPrice())));
            } else if (result.getResultType() == SellResultType.NO_ITEM_SOLD) {
                msg.send(player, Messages.NO_ITEMS_TO_SELL);
            }
        } else {
            if (settings.getProperty(MainConfig.DEBUG)) {
                plugin.getLogger().info("No ShopService available, it will do nothing");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTryPlaceWand(BlockPlaceEvent event) {
        if (WandService.isWand(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }
}
