package co.aeria.quicksellwand.listener;

import co.aeria.quicksellwand.Perms;
import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.api.SellResult;
import co.aeria.quicksellwand.api.SellResult.SellResultType;
import co.aeria.quicksellwand.api.ShopService;
import co.aeria.quicksellwand.config.MainConfig;
import co.aeria.quicksellwand.config.MessageSender;
import co.aeria.quicksellwand.config.MessageSender.Placeholder;
import co.aeria.quicksellwand.config.Messages;
import co.aeria.quicksellwand.service.WandService;
import java.util.Arrays;
import java.util.Optional;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private QuickSellWandPlugin plugin;
    private MessageSender msg;

    public PlayerListener(QuickSellWandPlugin plugin) {
        this.plugin = plugin;
        this.msg = plugin.getMessageSender();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND
            || item == null || !WandService.isWand(item)) {
            return;
        }

        BlockState blockState = event.getClickedBlock().getState();
        if (!(blockState instanceof Container)) {
            return;
        }
        if (!plugin.getSettings().getProperty(MainConfig.CONTAINERS).contains(blockState.getType())) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(Perms.USE_WAND)) {
            msg.send(player, Messages.NO_USE_WAND);
            event.setCancelled(true);
            return;
        }

        WandService wandService = plugin.getWandService();
        if (!player.hasPermission(Perms.NO_USE_COOLDOWN) && wandService.isOnCooldown(item)) {
            msg.send(player, Messages.WAND_ON_COOLDOWN, Placeholder.of("cooldown", wandService.getCooldownLeft(item)));
            event.setCancelled(true);
            return;
        }

        Inventory inv = ((Container) blockState).getInventory();
        Optional<ShopService> shopService = plugin.getShopService();
        if (shopService.isPresent()) {
            SellResult result = shopService.get().sell(player, Arrays.asList(inv.getContents()));
            if (result.getResultType() == SellResultType.SOLD) {
                inv.removeItem(result.getSoldItems().toArray(new ItemStack[0]));
                ItemStack newWand = wandService.useWand(player, item);
                player.getInventory().setItemInMainHand(newWand);
                msg.send(player, Messages.ITEMS_SOLD, Placeholder.of("price", String.format("%,.2f", result.getPrice())));
            } else if (result.getResultType() == SellResultType.NO_ITEM_SOLD) {
                msg.send(player, Messages.NO_ITEMS_TO_SELL);
            }
        }
        event.setCancelled(true);
    }
}
