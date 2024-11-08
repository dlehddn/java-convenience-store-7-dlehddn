package store.common.error;

public enum ItemErrorMessage implements ErrorMessage{
    NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
    ;
    private final String message;

    ItemErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
