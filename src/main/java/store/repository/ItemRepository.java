package store.repository;

import store.domain.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemRepository {
    private final Map<Item, Integer> promotionItems;
    private final Map<Item, Integer> nonPromotionItems;
    private final Map<String, Item> pks;

    public ItemRepository() {
        this.promotionItems = new HashMap<>();
        this.nonPromotionItems = new HashMap<>();
        this.pks = new HashMap<>();
    }

    public void add(Item item, int quantity) {
        if (item.getPromotion() == null) {
            nonPromotionItems.put(item, quantity);
            pks.put(item.getName(), item);
            return;
        }
        promotionItems.put(item, quantity);
        pks.put(item.getName(), item);
    }

    public Item getItemByName(String name) {
        return pks.get(name);
    }

    public Map<Item, Integer> getPromotionItems() {
        return promotionItems;
    }

    public Map<Item, Integer> getNonPromotionItems() {
        return nonPromotionItems;
    }
}
