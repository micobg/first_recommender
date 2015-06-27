package persisters;

import models.Book;
import models.Item;
import storage.MysqlConnection;

import java.sql.*;
import java.util.*;

public class MysqlPersister {

    /**
     * Get all users that like at leas one item of given type.
     *
     * @param itemType type of the items
     *
     * @return HasMap of item_id and users count
     */
    public Set<Integer> getAllUsers(ItemType itemType) {
        Set<Integer> result = new HashSet<>();
        String sql = "SELECT DISTINCT user_id FROM likes WHERE type = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.add(resultSet.getInt("user_id"));
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

     * @return HasMap of user_id and likes count
     */
    public Map<String, Integer> getLikesCountPerUser(ItemType itemType) {
        Map<String, Integer> result = new HashMap<>();
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
                result.put("user_id", resultSet.getInt("user_id"));
                result.put("likes_count", resultSet.getInt("likes_count"));
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
     * @return HasMap of item_id and users count
     */
    public Map<String, Integer> getUsersCountPerItem(ItemType itemType) {
        Map<String, Integer> result = new HashMap<>();
        String sql = "" +
            "SELECT " +
                "DISTINCT item_id, " +
                "COUNT(*) AS likes_count " +
            "FROM likes " +
            "WHERE " +
                "type = ? " +
            "GROUP BY item_id";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.put("item_id", resultSet.getInt("item_id"));
                result.put("likes_count", resultSet.getInt("likes_count"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getUsersCountPerItem: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Get all items that has been liked by given user.
     *
     * @param userId id of the user
     * @param itemType type of the items

     * @return Set of items
     */
    public Set<Item> getLikedItemsPerUser(Integer userId, ItemType itemType) {
        StringBuilder join = new StringBuilder();
        if (itemType.toString().equals(ItemType.BOOK.toString())) {
            join.append("JOIN books AS i ON i.id = likes.item_id ");
        }

        Set<Item> result = new HashSet<>();
        String sql = "" +
            "SELECT " +
                "i.id, " +
                "i.name, " +
                "i.link " +
            "FROM likes " +
            join.toString() +
            "WHERE " +
                "likes.user_id = ? " +
                "AND likes.item_type = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setInt(1, userId);
            sqlStatement.setString(2, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                Book book = new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("link")
                );

                result.add(book);
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getLikedItemsPerUser: " + ex.getMessage());
        }

        return result;
    }

}
