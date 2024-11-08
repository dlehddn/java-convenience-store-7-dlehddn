package store.common.error;

public enum InputErrorMessage implements ErrorMessage{
    INCORRECT_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
    UNAVAILABLE_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    private final String message;

    InputErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
