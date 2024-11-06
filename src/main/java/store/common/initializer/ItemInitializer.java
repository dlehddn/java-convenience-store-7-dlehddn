package store.common.initializer;

import store.domain.Item;
import store.domain.Promotion;
import store.repository.ItemRepository;
import store.repository.PromotionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static store.common.constants.FilePath.*;

public class ItemInitializer {
    public static void init(ItemRepository itemRepository, PromotionRepository promotionRepository) {
        String root = System.getProperty(ROOT.getPath());

        try (BufferedReader br = new BufferedReader(new FileReader(root + PRODUCTS.getPath()))) {
            String line = br.readLine();
            addItems(itemRepository, promotionRepository, br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addItems(ItemRepository itemRepository,
                                 PromotionRepository promotionRepository,
                                 BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            Promotion promotion = null;
            if (!fields[3].equals("null")) {
                promotion = promotionRepository.get(fields[3]);
            }
            Item item = new Item(fields[0], Integer.valueOf(fields[1]), promotion);
            itemRepository.add(item, Integer.valueOf(fields[2]));
        }
    }
}
