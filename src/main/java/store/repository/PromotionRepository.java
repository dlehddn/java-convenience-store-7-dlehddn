package store.repository;

import store.domain.Promotion;

import java.util.HashMap;
import java.util.Map;

public class PromotionRepository {
    private final Map<String, Promotion> promotions;

    public PromotionRepository() {
        this.promotions = new HashMap<>();
    }

    public Promotion get(String name) {
        return promotions.get(name);
    }

    public void add(Promotion promotion) {
        promotions.put(promotion.getName(), promotion);
    }
}
