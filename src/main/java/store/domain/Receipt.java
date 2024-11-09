package store.domain;

import store.dto.OrderResult;

import java.util.List;

public class Receipt {
    private final List<OrderResult> orderResults;
    private final Membership membership;

    public Receipt(List<OrderResult> orderResults, Membership membership) {
        this.orderResults = orderResults;
        this.membership = membership;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========W 편의점 ============").append("\n");
        sb.append("상품명           ").append("수량").append("금액").append("\n");
        for (OrderResult orderResult : orderResults) {
            sb.append(makeItemBuyResult(orderResult));
        }
        sb.append("===========증    정 ============").append("\n");
        for (OrderResult orderResult : orderResults) {
            sb.append(makeFreeItemResult(orderResult));
        }
        sb.append("===============================").append("\n");
        sb.append(makeTotalPriceResult(orderResults));
        sb.append(makeFreeDiscountResult(orderResults));
        sb.append(makeMemberShipResult(orderResults));
        sb.append(makeFinalPrice(orderResults));
        return sb.toString();
    }

    private String makeFinalPrice(List<OrderResult> orderResults) {
        int totalPrice = 0;
        for (OrderResult orderResult : orderResults) {
            totalPrice += orderResult.price() *
                    (orderResult.promotionPurchases() + orderResult.regularPurchases() - orderResult.freeCount());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("내실돈                            ").append(String.format("%,d", totalPrice - membership.getDiscountAmount())).append("\n");
        return sb.toString();
    }

    private String makeMemberShipResult(List<OrderResult> orderResults) {
        if (!membership.isAvailable()) return "";
        for (OrderResult orderResult : orderResults) {
            if (orderResult.freeCount() == 0) {
                int totalCount = orderResult.regularPurchases() + orderResult.promotionPurchases();
                int totalPrice = orderResult.price() * totalCount;
                membership.apply(totalPrice);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("멤버십할인                        ").append("-").append(String.format("%,d", membership.getDiscountAmount())).append("\n");
        return sb.toString();
    }

    private String makeFreeDiscountResult(List<OrderResult> orderResults) {
        StringBuilder sb = new StringBuilder();
        int totalDiscount = 0;
        for (OrderResult orderResult : orderResults) {
            totalDiscount += orderResult.freeCount() * orderResult.price();
        }
        sb.append("행사할인                          ").append("-").append(String.format("%,d", totalDiscount)).append("\n");
        return sb.toString();
    }

    private String makeTotalPriceResult(List<OrderResult> orderResults) {
        int totalCount = 0;
        int totalPrice = 0;
        for (OrderResult orderResult : orderResults) {
            int tmp = orderResult.regularPurchases() + orderResult.promotionPurchases();
            totalCount += tmp;
            totalPrice += tmp * orderResult.price();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("총구매액            ").append(totalCount + "        ").append(String.format("%,d", totalPrice)).append("\n");
        return sb.toString();
    }

    private String makeFreeItemResult(OrderResult orderResult) {
        StringBuilder sb = new StringBuilder();
        if (orderResult.freeCount() != 0) {
            sb.append(orderResult.itemName() + "            ").append(orderResult.freeCount()).append("\n");
        }
        return sb.toString();
    }

    private String makeItemBuyResult(OrderResult orderResult) {
        StringBuilder sb = new StringBuilder();
        int totalCount = orderResult.promotionPurchases() + orderResult.regularPurchases();
        sb.append(orderResult.itemName() + "        ")
                .append(totalCount).append("        ")
                .append(totalCount * orderResult.price())
                .append("\n");
        return sb.toString();
    }
}
