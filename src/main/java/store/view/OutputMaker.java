package store.view;

import store.dto.ItemDto;

import java.util.List;

public class OutputMaker {
    private static final String ITEM_FORMAT = "- %s %,d원 %s %s";

    public static String inventoryStatus(List<ItemDto> items) {
        StringBuilder sb = new StringBuilder();
        for (ItemDto item : items) {
            sb.append(makeInformation(item)).append("\n");
        }
        return sb.toString();
    }

    private static String makeInformation(ItemDto item) {
        return String.format(ITEM_FORMAT, item.itemName(), item.price(),
                makeQuantity(item.quantity()), makePromotionName(item.promotionName()));
    }

    private static String makeQuantity(int quantity) {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private static String makePromotionName(String promotionName) {
        if (promotionName == null) {
            return "";
        }
        return promotionName;
    }
}
