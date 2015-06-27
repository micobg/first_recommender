import models.Item;
import models.similarities.KarypisSimilarity;
import models.similarities.Similarity;
import persisters.ItemType;
import persisters.MysqlPersister;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Recommender {

    /**
     * Run recommender.
     *
     * @throws RecommenderException on error
     */
    public void run() throws RecommenderException {
        Scanner in = new Scanner(System.in);
        int userId = in.nextInt();

        // load active user's liked items
        MysqlPersister mysqlPersister = new MysqlPersister();
        Set<Item> activeUserItems = mysqlPersister.getLikedItemsPerUser(userId, ItemType.BOOK);
        if (activeUserItems.size() == 0) {
            throw new RecommenderException("There is no user with user_id = " + userId + ".");
        }

        // calculate similarity to other items
        buildSimilarityMatrix(activeUserItems);

        // select top-n items
    }

    protected void buildSimilarityMatrix(Set<Item> activeUserItems) {
        MysqlPersister mysqlPersister = new MysqlPersister();

        // get other items (that the active user does not like)
        Set<Item> nonLikedItems = mysqlPersister.getAllItems(ItemType.BOOK);
        nonLikedItems.removeAll(activeUserItems);

        Similarity similarityCalculator = new KarypisSimilarity();
        Map<Map<Item, Item>, Double> similarityMatrix = new HashMap<>();
        for(Item activeUserItem : activeUserItems) {
            for(Item nonLikedItem : nonLikedItems) {
                Map<Item, Item> itemsMap = new HashMap<>();
                itemsMap.put(activeUserItem, nonLikedItem);

                // calculate similarity
                Double similarity = similarityCalculator.calculate(activeUserItem, nonLikedItem);

                similarityMatrix.put(itemsMap, similarity);
            }
        }
    }
}