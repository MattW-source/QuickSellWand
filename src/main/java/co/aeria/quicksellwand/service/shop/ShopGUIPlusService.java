package co.aeria.quicksellwand.service.shop;

import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.api.SellResult;
import co.aeria.quicksellwand.api.SellResult.SellResultType;
import co.aeria.quicksellwand.api.ShopService;
import java.util.ArrayList;
import java.util.List;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.player.PlayerData;
import net.brcdev.shopgui.shop.Shop;
import net.brcdev.shopgui.shop.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ShopGUIPlusService implements ShopService {

    private ShopGuiPlugin shopPlugin;

    public ShopGUIPlusService(QuickSellWandPlugin plugin, Plugin shopPlugin) {
        this.shopPlugin = (ShopGuiPlugin) shopPlugin;
    }

    @Override
    public SellResult sell(Player player, List<ItemStack> items) {
        PlayerData playerData = shopPlugin.getPlayerManager().getPlayerData(player);
        List<ItemStack> soldItems = new ArrayList<>();
        double soldPrice = 0;
        SellResultType resultType = SellResultType.NO_ITEM_SOLD;
        for (ItemStack item : items) {
            ShopItem shopItem = ShopGuiPlusApi.getItemStackShopItem(player, item);
            if (shopItem != null) {
                Shop shop = ShopGuiPlusApi.getItemStackShop(player, item);
                double sellPrice = shopItem.getSellPriceForAmount(shop, player, playerData, item.getAmount());
                if (sellPrice > 0) {
                    shopPlugin.getEconomyProvider().deposit(player, sellPrice);
                    soldItems.add(item);
                    soldPrice += sellPrice;
                    resultType = SellResultType.SOLD;
                }
            }
        }
        return new SellResult(resultType, soldItems, soldPrice);
    }

}
