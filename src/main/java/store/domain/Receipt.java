package store.domain;

import store.dto.OrderResult;

import java.util.List;

public class Receipt {
    private final List<OrderResult> orderResults;
    private final Membership membership;
    private static final String HEADER_FORMAT = "%-10s %5s %10s\n";
    private static final String ITEM_FORMAT = "%-10s %5d %,10d\n";
    private static final String FREE_FORMAT = "%-10s %5d\n";
    private static final String DISCOUNT_FORMAT = "%-15s %10s\n";
    private static final String PAY_FORMAT = "%-15s %,10d\n";
    private static final String LINE_CHANGE = "\n";

    public Receipt(List<OrderResult> orderResults, Membership membership) {
        this.orderResults = orderResults;
        this.membership = membership;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========W 편의점 ============").append(LINE_CHANGE);
        sb.append(String.format(HEADER_FORMAT, "상품명", "수량", "금액"));
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
        sb.append(String.format(PAY_FORMAT, "내실돈", totalPrice - membership.getDiscountAmount()));
        return sb.toString();
    }

    private String makeMemberShipResult(List<OrderResult> orderResults) {
        for (OrderResult orderResult : orderResults) {
            if (orderResult.freeCount() == 0 && membership.isAvailable()) {
                int totalCount = orderResult.regularPurchases() + orderResult.promotionPurchases();
                int totalPrice = orderResult.price() * totalCount;
                membership.apply(totalPrice);
            }
        }
        StringBuilder sb = new StringBuilder();
        String discountAmount = "-" + membership.getDiscountAmount();
        sb.append(String.format(DISCOUNT_FORMAT, "멤버십할인", discountAmount));
        return sb.toString();
    }

    private String makeFreeDiscountResult(List<OrderResult> orderResults) {
        StringBuilder sb = new StringBuilder();
        int totalDiscount = 0;
        for (OrderResult orderResult : orderResults) {
            totalDiscount += orderResult.freeCount() * orderResult.price();
        }
        String discountAmount = "-" + totalDiscount;
        sb.append(String.format(DISCOUNT_FORMAT, "행사할인", discountAmount));
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
        sb.append(String.format(ITEM_FORMAT, "총구매액", totalCount, totalPrice));
        return sb.toString();
    }

    private String makeFreeItemResult(OrderResult orderResult) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FREE_FORMAT, orderResult.itemName(), orderResult.freeCount()));
        return sb.toString();
    }

    private String makeItemBuyResult(OrderResult orderResult) {
        StringBuilder sb = new StringBuilder();
        int totalCount = orderResult.promotionPurchases() + orderResult.regularPurchases();
        sb.append(String.format(ITEM_FORMAT, orderResult.itemName(), totalCount, totalCount * orderResult.price()));
        return sb.toString();
    }
}
