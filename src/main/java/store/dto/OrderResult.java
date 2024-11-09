package store.dto;

public record OrderResult(String itemName,
                          int price,
                          int promotionPurchases,
                          int regularPurchases,
                          int originPayCount,
                          int freeCount) {
}
