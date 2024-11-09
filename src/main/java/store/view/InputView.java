package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.common.validator.InputValidator;

public class InputView {
    private static final String REQUEST_ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String POSSIBLE_PROMOTION_APPLY = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String NOT_DISCOUNT = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_REQUEST = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String REBUY_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String readOrder() {
        System.out.println(REQUEST_ORDER);
        String order = Console.readLine();
        InputValidator.validate(order);
        return order;
    }

    public String readPromotionApplied(String itemName) {
        System.out.println(String.format(POSSIBLE_PROMOTION_APPLY, itemName));
        String input = Console.readLine();
        InputValidator.validate(input);
        return input;
    }

    public String readPurchaseOrNot(String itemName, int originPayCount) {
        System.out.println(String.format(NOT_DISCOUNT, itemName, originPayCount));
        String input = Console.readLine();
        InputValidator.validate(input);
        return input;
    }

    public String readMembership() {
        System.out.println(MEMBERSHIP_REQUEST);
        String input = Console.readLine();
        InputValidator.validate(input);
        return input;
    }

    public String readReBuy() {
        System.out.println(REBUY_MESSAGE);
        String input = Console.readLine();
        InputValidator.validate(input);
        return input;
    }
}
