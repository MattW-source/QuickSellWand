package co.aeria.quicksellwand.api;

import java.util.Optional;

public interface QuickSellWand {

    Optional<ShopService> getShopService();

    void setShopService(ShopService shopService);
}
