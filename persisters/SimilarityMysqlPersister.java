package persisters;

import models.Book;
import models.Item;
import models.models.User;
import storage.MysqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimilarityMysqlPersister extends MysqlPersister {

    /**
     * Get all users that liked given item.
     *
     * @param itemId id of the item
     * @param itemType type of the items
     *
     * @return Set of user ids
     */
    public Set<User> getUsersThatLikeAnItem(Integer itemId, ItemType itemType) {
        Set<User> result = new HashSet<>();
        String sql = "" +
            "SELECT " +
                "user_id " +
            "FROM likes " +
            "WHERE " +
                "item_id = ? " +
                "AND item_type = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setInt(1, itemId);
            sqlStatement.setString(2, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.add(new User(resultSet.getInt("user_id")));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getUsersThatLikeAnItem: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Get all users that likes given item type.
     *
     * @param itemType type of the items
     *
     * @return Set of users
     */
    public Set<User> getAllUsers(ItemType itemType) {
        Set<User> result = new HashSet<>();
        String sql = "SELECT DISTINCT user_id FROM likes WHERE type = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.add(new User(resultSet.getInt("user_id")));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getAllUsers: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Get all users that like at least one book and count of books that every of them like.
     *
     * @param itemType type of the items

     * @return HasMap of user and likes count
     */
    public Map<User, Integer> getLikesCountPerUser(ItemType itemType) {
        Map<User, Integer> result = new HashMap<>();
        String sql = "" +
            "SELECT " +
                "DISTINCT user_id, " +
                "COUNT(*) AS likes_count " +
            "FROM likes " +
            "WHERE " +
                "item_type = ? " +
            "GROUP BY user_id";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.put(new User(resultSet.getInt("user_id")), resultSet.getInt("likes_count"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getLikesCountPerUser: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Get all items that have al least one like and count of users that like every of them.
     *
     * @param itemType type of the items
     *
     * @return HasMap of item and users count
     */
    public Map<Item, Integer> getUsersCountPerItem(ItemType itemType) {
        StringBuilder join = new StringBuilder();
        if (itemType.toString().equals(ItemType.BOOK.toString())) {
            join.append("JOIN books AS i ON i.id = likes.item_id ");
        }

        Map<Item, Integer> result = new HashMap<>();
        String sql = "" +
            "SELECT " +
                "DISTINCT likes.item_id, " +
                "i.name, " +
                "i.link, " +
                "COUNT(*) AS likes_count " +
            "FROM likes " +
            join.toString() +
            "WHERE " +
                "likes.item_type = ? " +
            "GROUP BY item_id";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                Item item = null;
                if (itemType.toString().equals(ItemType.BOOK.toString())) {
                    item = new Book(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("link")
                    );
                }

                result.put(item, resultSet.getInt("likes_count"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getUsersCountPerItem: " + ex.getMessage());
        }

        return result;
    }
}
