package store.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.common.error.InputErrorMessage;
import store.common.extractor.InputExtractor;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @ParameterizedTest
    @ValueSource(strings = {"콜라-10,사이다-10", "[콜라-10], [사이다-10]"})
    void 주문_생성_테스트(String input) {
        //given
        String[] orders = InputExtractor.extractOrder(input);

        //when & then
        Assertions.assertThatThrownBy(() -> {
                    for (String order : orders) {
                        Order o = new Order(order);
                    }
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(InputErrorMessage.UNAVAILABLE_FORMAT.getMessage());
    }
}