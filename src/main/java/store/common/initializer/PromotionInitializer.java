package store.common.initializer;

import store.domain.Promotion;
import store.repository.PromotionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static store.common.constants.FilePath.*;

public class PromotionInitializer {
    public static void init(PromotionRepository promotionRepository) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String root = System.getProperty(ROOT.getPath());

        try (BufferedReader br = new BufferedReader(new FileReader(root + PROMOTIONS.getPath()))) {
            String line = br.readLine();
            addPromotions(promotionRepository, formatter, br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addPromotions(PromotionRepository promotionRepository,
                                      DateTimeFormatter formatter,
                                      BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            Promotion promotion = new Promotion(
                    fields[0],
                    Integer.valueOf(fields[1]),
                    Integer.valueOf(fields[2]),
                    LocalDate.parse(fields[3], formatter),
                    LocalDate.parse(fields[4], formatter)
            );
            promotionRepository.add(promotion);
        }
    }
}
