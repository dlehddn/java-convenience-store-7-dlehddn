package store.common.constants;

public enum ReceiptFormat {
    HEADER_FORMAT("%-10s %5s %10s\n"),
    ITEM_FORMAT("%-10s %5d %,10d\n"),
    FREE_FORMAT("%-10s %5d\n"),
    DISCOUNT_FORMAT("%-15s %10s\n"),
    PAY_FORMAT("%-15s %,10d\n"),
    LINE_CHANGE("\n"),
    ;

    private final String format;

    ReceiptFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
