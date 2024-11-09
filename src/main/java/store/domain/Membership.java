package store.domain;

public class Membership {
    private final boolean isAvailable;
    private int discountAmount;

    public Membership(boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.discountAmount = 0;
    }

    public void apply(int price) {
        int discountAmount = price * 30 / 100;
        if (this.discountAmount + discountAmount >= 8000) {
            this.discountAmount = 8000;
            return;
        }
        this.discountAmount += discountAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
