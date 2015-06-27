package models.similarities;

import models.Item;
import models.models.User;
import persisters.ItemType;
import persisters.SimilarityMysqlPersister;

import java.util.Map;
import java.util.Set;

public class KarypisSimilarity implements Similarity {

    private static final Double ALPHA = 0.5;

    private Map<Item, Integer> usersCountPerItem = null;
    private Map<User, Integer> likesCountPerUser = null;

    private ItemType currentlyDataLoadedForItemType = null;

    public KarypisSimilarity() {
        // hardcoded TODO: do it better
        currentlyDataLoadedForItemType = ItemType.BOOK;
        loadData();
    }

    public Double calculate(Item itemX, Item itemY) {
        // WARNING: itemX and itemY should be from the same type
        if (!currentlyDataLoadedForItemType.toString().equals(itemX.getType().toString())) {
            currentlyDataLoadedForItemType = itemX.getType();
            loadData();
        }

        return ratingMatrixSum(itemY) / (freq(itemX) * Math.pow(freq(itemY), ALPHA));
    }

    /**
     * Return sum normalized item ratings for given item from rating matrix;
     *
     * @param item the item
     *
     * @return calculated sum
     */
    private Double ratingMatrixSum(Item item) {
        SimilarityMysqlPersister mysqlPersister = new SimilarityMysqlPersister();
        Set<User> usersThatLikeTheItem = mysqlPersister.getUsersThatLikeAnItem(item.getId(), item.getType());

        Double sum = 0.0;
        for(User user : usersThatLikeTheItem) {
            sum += likesCountPerUser.get(user);
        }

        return sum;
    }

    /**
     * Count of users that likes given item
     *
     * @return count
     */
    private Integer freq(Item item) {
        return usersCountPerItem.get(item);
    }

    /**
     * Load data for calculations
     */
    private void loadData() {
        SimilarityMysqlPersister mysqlPersister = new SimilarityMysqlPersister();

        usersCountPerItem = mysqlPersister.getUsersCountPerItem(currentlyDataLoadedForItemType);
        likesCountPerUser = mysqlPersister.getLikesCountPerUser(currentlyDataLoadedForItemType);
    }
}
