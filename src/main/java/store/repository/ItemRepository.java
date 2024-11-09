package store.repository;

import store.domain.Item;
import store.domain.Order;
import store.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static store.common.error.ItemErrorMessage.*;

public class ItemRepository {
    private final Map<String, Item> promotionItems;
    private final Map<String, Item> regularItems;

    public ItemRepository() {
        this.promotionItems = new HashMap<>();
        this.regularItems = new HashMap<>();
    }

    public void addBlankRegularItems() {
        for (String itemName : promotionItems.keySet()) {
            if (!regularItems.containsKey(itemName)) {
                Item item = promotionItems.get(itemName);
                regularItems.put(itemName, new Item(itemName, item.getPrice(), null, 0));
            }
        }
    }

    public void save(Item item) {
        if (item.getPromotion() == null) {
            regularItems.put(item.getName(), item);
            return;
        }
        promotionItems.put(item.getName(), item);
    }

    public int getPromotionRemainQuantity(String itemName) {
        return getItemFromPromotions(itemName)
                .orElse(new Item(null, 0, null, 0))
                .getQuantity();
    }

    public int getRegularRemainQuantity(String itemName) {
        return getItemFromRegular(itemName)
                .orElse(new Item(null, 0, null, 0))
                .getQuantity();
    }

    public void checkExist(String itemName) {
        if (!promotionItems.containsKey(itemName) && !regularItems.containsKey(itemName)) {
            throw new IllegalArgumentException(NOT_FOUND.getMessage());
        }
    }

    public Optional<Item> getItemFromPromotions(String name) {
        return Optional.ofNullable(promotionItems.get(name));
    }

    public Optional<Item> getItemFromRegular(String name) {
        return Optional.ofNullable(regularItems.get(name));
    }

    public List<ItemDto> getPromotionItems() {
        return promotionItems.values()
                .stream()
                .map(item -> new ItemDto(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPromotion()
                                .getName())
                )
                .collect(Collectors.toList());
    }

    public List<ItemDto> getRegularItems() {
        return regularItems.values()
                .stream()
                .map(item -> new ItemDto(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        null)
                )
                .collect(Collectors.toList());
    }

    public int updatePromotionQuantity(Order order) {
        int buyQuantity = order.getQuantity();
        Item item = promotionItems.getOrDefault(order.getItemName(), new Item("default", 0, null, 0));
        int maxBuyQuantity = Math.min(item.getQuantity(), buyQuantity);
        item.setQuantity(item.getQuantity() - maxBuyQuantity);
        return maxBuyQuantity;
    }

    public int updateRegularQuantity(Order order) {
        Item item = regularItems.get(order.getItemName());
        item.setQuantity(item.getQuantity() - order.getQuantity());
        return order.getQuantity();
    }

    public int getPrice(String itemName) {
        Item regularItem = regularItems.get(itemName);
        Item promotionItem = promotionItems.get(itemName);
        if (regularItem != null) {
            return regularItem.getPrice();
        }
        return promotionItem.getPrice();
    }
}
