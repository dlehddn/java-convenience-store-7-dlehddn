package store;

import store.controller.StoreController;
import store.controller.StoreController;
import store.repository.ItemRepository;
import store.repository.PromotionRepository;
import store.service.ItemService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
//         TODO: 프로그램 구현
        StoreController storeController = new StoreController(
                new InputView(),
                new OutputView(),
                new ItemService(new ItemRepository(), new PromotionRepository())
        );
        storeController.start();
    }
}
