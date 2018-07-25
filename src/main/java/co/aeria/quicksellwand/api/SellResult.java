package co.aeria.quicksellwand.api;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class SellResult {

    private final SellResultType resultType;
    private final List<ItemStack> soldItems;
    private final double price;
    private boolean type;

    public SellResult(SellResultType type, List<ItemStack> soldItems, double soldPrice) {
        this.resultType = type;
        this.soldItems = soldItems;
        this.price = soldPrice;
    }

    public SellResultType getResultType() {
        return resultType;
    }

    public double getPrice() {
        return price;
    }

    public List<ItemStack> getSoldItems() {
        return soldItems;
    }

    public enum SellResultType {
        NO_ITEM_SOLD, SOLD
    }
}
