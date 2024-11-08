package store.service;

import net.bytebuddy.matcher.FilterableList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import store.common.error.ItemErrorMessage;
import store.domain.Order;
import store.repository.ItemRepository;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {
    ItemService itemService;

    @BeforeEach
    void init() {
        itemService = new ItemService(new ItemRepository(), new PromotionRepository());
        itemService.initRepository();
    }

    @Test
    void 존재하지_않는_상품명은_예외가_발생한다() {
        //given
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("과자", 10));

        //when && then
        assertThatThrownBy(() -> itemService.testPayment(orders))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ItemErrorMessage.NOT_FOUND.getMessage());
    }

    @Test
    void 재고를_초과한_주문은_예외가_발생한다() {
        //given
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("콜라", 22));

        //when && then
        assertThatThrownBy(() -> itemService.testPayment(orders))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ItemErrorMessage.INSUFFICIENT_STOCK.getMessage());
    }

    @Test
    void 프로모션_적용을_안내할_수_있다() {
        //given
        Order order = new Order("콜라", 2);

        //when & then
        assertThat(itemService.canApplyPromotion(order)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideOrdersAndExpected")
    void 프로모션_미적용_개수를_안내할_수_있다(Order order, int expected) {
        //when & then
        assertThat(itemService.calculateOriginPayCount(order)).isEqualTo(expected);
    }

    static Stream<Arguments> provideOrdersAndExpected() {
        return Stream.of(
                Arguments.of(new Order("콜라", 11), 2),
                Arguments.of(new Order("콜라", 8), 2),
                Arguments.of(new Order("초코바", 5), 1),
                Arguments.of(new Order("컵라면", 2), 2)
        );
    }
}