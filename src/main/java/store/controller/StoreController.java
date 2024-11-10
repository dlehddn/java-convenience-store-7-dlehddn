package store.controller;

import store.common.extractor.InputExtractor;
import store.domain.AppliedStatus;
import store.domain.Membership;
import store.domain.Order;
import store.domain.Receipt;
import store.dto.OrderResult;
import store.service.ItemService;
import store.view.InputView;
import store.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ItemService itemService;

    public StoreController(InputView inputView, OutputView outputView, ItemService itemService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.itemService = itemService;
    }

    public void start() {
        itemService.initRepository();
        do {
            outputView.printInventory(itemService.getInventoryStatus());
            List<Order> orders = makeOrders();
            List<OrderResult> orderResults = new ArrayList<>();
            orderItems(orders, orderResults);
            Receipt receipt = makeReceipt(orderResults);
            outputView.printReceipt(receipt.generate());
        } while (readReBuy().isApplied());
    }

    private AppliedStatus readReBuy() {
        try {
            return new AppliedStatus(inputView.readReBuy());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e);
            return readReBuy();
        }
    }

    private Receipt makeReceipt(List<OrderResult> orderResults) {
        AppliedStatus appliedStatus = readMembership();
        Receipt receipt = new Receipt(orderResults, new Membership(appliedStatus.isApplied()));
        return receipt;
    }

    private AppliedStatus readMembership() {
        try {
            return new AppliedStatus(inputView.readMembership());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e);
            return readMembership();
        }
    }

    private void orderItems(List<Order> orders, List<OrderResult> results) {
        for (Order order : orders) {
            checkFreeGift(order);
            int originPayCount = getOriginPayCount(order);
            OrderResult result = itemService.order(order, originPayCount);
            results.add(result);
        }
    }

    private int getOriginPayCount(Order order) {
        int originPayCount = itemService.calculateOriginPayCount(order);
        if (originPayCount > 0) {
            if (!readTakingItem(order, originPayCount).isApplied()) {
                order.excludeItems(originPayCount);
                originPayCount = 0;
            }
        }
        return originPayCount;
    }

    private AppliedStatus readTakingItem(Order order, int originPayCount) {
        try {
            return new AppliedStatus(inputView.readPurchaseOrNot(order.getItemName(), originPayCount));
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e);
            return readTakingItem(order, originPayCount);
        }
    }

    private void checkFreeGift(Order order) {
        if (itemService.canApplyFreeGift(order)) {
            String input = isAppliedAsFree(order);
            if (input.equals("Y")) {
                order.applyPromotion();
            }
        }
    }

    private String isAppliedAsFree(Order order) {
        try {
            return inputView.readPromotionApplied(order.getItemName());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e);
            return isAppliedAsFree(order);
        }
    }
    private List<Order> makeOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            String[] split = InputExtractor.extractOrder(inputView.readOrder());
            for (String s : split) {
                orders.add(new Order(s));
            }
            itemService.testPayment(orders);
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e);
            makeOrders();
        }
        return orders;
    }
}
