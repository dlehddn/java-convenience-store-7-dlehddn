package store.domain;

import store.common.validator.OrderValidator;

public class Order {
    private final String itemName;
    private int quantity;

    public Order(String order) {
        OrderValidator.validate(order);
        String[] extracted = extract(order);
        this.itemName = extracted[0];
        this.quantity = Integer.valueOf(extracted[1]);
    }

    private static String[] extract(String order) {
        order = order.substring(1, order.length() - 1);
        String[] split = order.split("-");
        return split;
    }

    public Order(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public void applyPromotion() {
        quantity++;
    }

    public void excludeItems(int count) {
        quantity -= count;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }
}
