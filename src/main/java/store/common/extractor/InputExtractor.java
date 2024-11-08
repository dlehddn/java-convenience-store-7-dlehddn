package store.common.extractor;

public class InputExtractor {
    public static String[] extractOrder(String input) {
        return input.split(",");
    }
}
