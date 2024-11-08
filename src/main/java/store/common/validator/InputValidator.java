package store.common.validator;

import store.common.error.InputErrorMessage;

import static store.common.error.InputErrorMessage.*;

public class InputValidator{
    public static void validate(String input) {
        checkBlank(input);
    }

    private static void checkBlank(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(INCORRECT_INPUT.getMessage());
        }
    }
}
