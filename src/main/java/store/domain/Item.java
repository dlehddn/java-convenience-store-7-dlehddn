package store.domain;

import java.util.Objects;

public class Item {
    private final String name;
    private final int price;
    private final Promotion promotion;

    public Item(String name, int price, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotion = promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return price == item.price && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
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
}
