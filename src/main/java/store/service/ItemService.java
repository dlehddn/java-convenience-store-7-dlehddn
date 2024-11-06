package store.service;

import store.common.initializer.ItemInitializer;
import store.common.initializer.PromotionInitializer;
import store.domain.Item;
import store.dto.ItemDto;
import store.repository.ItemRepository;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ItemService {
    private final ItemRepository itemRepository;
    private final PromotionRepository promotionRepository;

    public ItemService(ItemRepository itemRepository, PromotionRepository promotionRepository) {
        this.itemRepository = itemRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<ItemDto> getInventoryStatus() {
        Map<Item, Integer> promotionItems = itemRepository.getPromotionItems();
        Map<Item, Integer> nonPromotionItems = itemRepository.getNonPromotionItems();
        return makeInventoryStatus(promotionItems, nonPromotionItems);
    }

    public void initRepository() {
        PromotionInitializer.init(promotionRepository);
        ItemInitializer.init(itemRepository, promotionRepository);
    }

    private List<ItemDto> makeInventoryStatus(Map<Item, Integer> promotionItems,
                                              Map<Item, Integer> nonPromotionItems) {
        List<ItemDto> items = new ArrayList<>();
        promotionItems.forEach((k, v) -> {
            items.add(new ItemDto(k.getName(), k.getPrice(), v, k.getPromotion().getName()));
        });
        nonPromotionItems.forEach((k, v) -> {
            items.add(new ItemDto(k.getName(), k.getPrice(), v, null));
        });
        items.sort(Comparator.comparing(i -> i.itemName()));
        return items;
    }
}
