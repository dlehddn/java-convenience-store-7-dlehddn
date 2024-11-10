package store.domain;

import store.common.validator.AppliedStatusValidator;

public class AppliedStatus {
    private final boolean isApplied;

    public AppliedStatus(String input) {
        AppliedStatusValidator.validate(input);
        if (input.equals("Y")) {
            isApplied = true;
            return;
        }
        isApplied = false;
    }

    public boolean isApplied() {
        return isApplied;
    }
}
