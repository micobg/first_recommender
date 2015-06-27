import models.Item;
import persisters.ItemType;
import persisters.MysqlPersister;

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

        
    }
}