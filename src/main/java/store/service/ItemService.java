package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.common.initializer.ItemInitializer;
import store.common.initializer.PromotionInitializer;
import store.domain.Item;
import store.domain.Order;
import store.domain.Promotion;
import store.dto.ItemDto;
import store.dto.OrderResult;
import store.repository.ItemRepository;
import store.repository.PromotionRepository;

import java.time.LocalDate;
import java.util.*;

import static store.common.error.ItemErrorMessage.*;

public class ItemService {
    private final ItemRepository itemRepository;
    private final PromotionRepository promotionRepository;

    public ItemService(ItemRepository itemRepository, PromotionRepository promotionRepository) {
        this.itemRepository = itemRepository;
        this.promotionRepository = promotionRepository;
    }

    public void initRepository() {
        PromotionInitializer.init(promotionRepository);
        ItemInitializer.init(itemRepository, promotionRepository);
    }

    public List<ItemDto> getInventoryStatus() {
        List<ItemDto> promotionItems = itemRepository.getPromotionItems();
        List<ItemDto> nonPromotionItems = itemRepository.getRegularItems();
        return makeInventoryStatus(promotionItems, nonPromotionItems);
    }

    public void testPayment(List<Order> orders) {
        for (Order order : orders) {
            itemRepository.checkExist(order.getItemName());
            int promotionStock = itemRepository.getPromotionRemainQuantity(order.getItemName());
            int regularStock = itemRepository.getRegularRemainQuantity(order.getItemName());
            if (promotionStock + regularStock < order.getQuantity()) {
                throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
            }
        }
    }

    public boolean canApplyPromotion(Order order) {
        int promotionStock = itemRepository.getPromotionRemainQuantity(order.getItemName());
        if (promotionStock == 0) {
            return false;
        }
        Item item = itemRepository.getItemFromPromotions(order.getItemName()).get();
        Promotion promotion = item.getPromotion();
        if (!isPromotionDate(promotion)) {
            return false;
        }

        if (order.getQuantity() % (promotion.getBuy() + promotion.getGet()) == promotion.getBuy()
                && promotionStock > order.getQuantity()) {
            return true;
        }
        return false;
    }

    public int calculateOriginPayCount(Order order) {
        int promotionStock = itemRepository.getPromotionRemainQuantity(order.getItemName());
        if (promotionStock == 0) {
            return 0;
        }

        Item item = itemRepository.getItemFromPromotions(order.getItemName()).get();
        Promotion promotion = item.getPromotion();

        if (promotionStock > 0 && !isPromotionDate(promotion)) {
            return 0;
        }

        //Case1. All Regular
        if ((promotion.getGet() + promotion.getBuy() > promotionStock)) {
            return order.getQuantity();
        }
        int orderMaxPromotion = order.getQuantity() / (promotion.getBuy() + promotion.getGet()) * (promotion.getBuy() + promotion.getGet());
        //Case2. 프로모션 재고 중에서 처리 가능
        if (orderMaxPromotion <= promotionStock) {
            return order.getQuantity() - orderMaxPromotion;
        }
        //Case3. 프로모션 + 일반 상품으로 처리해야하는 경우
        int maxPromotion = promotionStock / (promotion.getBuy() + promotion.getGet()) * (promotion.getBuy() + promotion.getGet());
        return order.getQuantity() - maxPromotion;
    }

    private boolean isPromotionDate(Promotion promotion) {
        LocalDate now = DateTimes.now().toLocalDate();
        return isWithinRange(promotion, now);
    }

    private boolean isWithinRange(Promotion promotion, LocalDate now) {
        LocalDate startDate = promotion.getStart_date();
        LocalDate endDate = promotion.getEnd_date();
        return (now.isEqual(startDate) || now.isAfter(startDate))
                && (now.isEqual(endDate) || now.isBefore(endDate));
    }


    public OrderResult order(Order order, final int originPayCount) {
        int promotionBuyCount = itemRepository.updatePromotionQuantity(order);
        int regularBuyCount = itemRepository.updateRegularQuantity(new Order(order.getItemName(), order.getQuantity() - promotionBuyCount));
        int freeCount = 0;
        if (promotionBuyCount > 0) {
            Item item = itemRepository.getItemFromPromotions(order.getItemName()).get();
            Promotion promotion = promotionRepository.get(item.getPromotion().getName());
            freeCount = promotionBuyCount / (promotion.getBuy() + promotion.getGet());
            if (!isPromotionDate(promotion)) {
                freeCount = 0;
            }
        }
        return new OrderResult(
                order.getItemName(),
                itemRepository.getPrice(order.getItemName()),
                promotionBuyCount,
                regularBuyCount,
                originPayCount,
                freeCount
        );
    }

    private List<ItemDto> makeInventoryStatus(List<ItemDto> promotionItems,
                                              List<ItemDto> nonPromotionItems) {
        List<ItemDto> items = new ArrayList<>();
        items.addAll(promotionItems);
        items.addAll(nonPromotionItems);
        items.sort(Comparator.comparing(i -> i.itemName()));
        return items;
    }
}
