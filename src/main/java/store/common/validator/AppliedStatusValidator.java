package store.common.validator;

import java.util.regex.Pattern;

import static store.common.error.InputErrorMessage.*;

public class AppliedStatusValidator {

    private static final String PATTERN_REX = "^[YN]$";
    public static void validate(String input) {
        if (!Pattern.matches(PATTERN_REX, input)) {
            throw new IllegalArgumentException(INCORRECT_INPUT.getMessage());
        }
    }
}
