package co.aeria.quicksellwand.api;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ShopService {
    SellResult sell(Player player, List<ItemStack> items);
}
