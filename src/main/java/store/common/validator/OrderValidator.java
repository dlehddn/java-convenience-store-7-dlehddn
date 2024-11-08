package store.common.validator;

import store.common.error.InputErrorMessage;
import store.common.error.ItemErrorMessage;

import java.util.regex.Pattern;

import static store.common.error.InputErrorMessage.*;

public class OrderValidator {
    private static final String ORDER_REX = "^\\[[가-힣a-zA-Z]+-\\d+\\]";

    public static void validate(String input) {
        if (!Pattern.matches(ORDER_REX, input)) {
            throw new IllegalArgumentException(UNAVAILABLE_FORMAT.getMessage());
        }
    }
}
