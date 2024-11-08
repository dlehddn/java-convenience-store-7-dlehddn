package store.view;

import store.dto.ItemDto;

import java.util.List;

public class OutputView {
    private static final String SHOW_INVENTORY = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";

    public void printInventory(List<ItemDto> items) {
        System.out.println(SHOW_INVENTORY);
        System.out.println(OutputMaker.inventoryStatus(items));
    }

    public void printErrorMessage(IllegalArgumentException e) {
        System.out.println("[ERROR] " + e.getMessage());
    }
}
