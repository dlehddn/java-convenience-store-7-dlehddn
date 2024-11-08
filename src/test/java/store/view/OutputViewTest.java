package store.view;

import org.junit.jupiter.api.Test;
import store.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

class OutputViewTest {
    OutputView outputView = new OutputView();

    @Test
    void 출력_테스트() {
        //given
        List<ItemDto> items = testGenerate();

        //when & then
        outputView.printInventory(items);
    }

    static List<ItemDto> testGenerate() {
        List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto("콜라", 1000, 0, "탄산2+1"));
        items.add(new ItemDto("콜라", 1000, 7, null));
        items.add(new ItemDto("사이다", 1000, 8, "탄산2+1"));
        items.add(new ItemDto("사이다", 1000, 7, null));
        return items;
    }
}