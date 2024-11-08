package store.domain;


public class Item {
    private final String name;
    private final int price;
    private final Promotion promotion;
    private int quantity;

    public Item(String name, int price, Promotion promotion, int quantity) {
        this.name = name;
        this.price = price;
        this.promotion = promotion;
        this.quantity = quantity;
    }

    public Item(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.promotion = item.getPromotion();
        this.quantity = item.getQuantity();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getQuantity() {
        return quantity;
    }
}
