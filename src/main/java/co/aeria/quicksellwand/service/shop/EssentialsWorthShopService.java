package co.aeria.quicksellwand.service.shop;

import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.api.SellResult;
import co.aeria.quicksellwand.api.SellResult.SellResultType;
import co.aeria.quicksellwand.api.ShopService;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EssentialsWorthShopService implements ShopService {

    private QuickSellWandPlugin plugin;
    private Essentials essentials;

    public EssentialsWorthShopService(QuickSellWandPlugin plugin, Plugin shopPlugin) {
        this.plugin = plugin;
        this.essentials = (Essentials) shopPlugin;
    }

    @Override
    public SellResult sell(Player player, List<ItemStack> items) {
        SellResultType resultType = SellResultType.NO_ITEM_SOLD;
        List<ItemStack> soldItems = new ArrayList<>();
        BigDecimal soldPrice = new BigDecimal(0.0);
        User user = essentials.getUser(player);
        for (ItemStack item : items) {
            try {
                BigDecimal sellPrice = essentials.getWorth().getPrice(essentials, item);
                if (sellPrice != null && sellPrice.signum() > 0) {
                    sellPrice = sellPrice.multiply(new BigDecimal(item.getAmount()));
                    user.giveMoney(sellPrice);
                    soldItems.add(item);
                    soldPrice = soldPrice.add(sellPrice);
                    resultType = SellResultType.SOLD;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new SellResult(resultType, soldItems, soldPrice.doubleValue());
    }
}
