package store.repository;

import store.domain.Item;
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


}
