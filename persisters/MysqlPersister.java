package persisters;

import models.Book;
import models.Item;
import storage.MysqlConnection;

import java.sql.*;
import java.util.*;

public class MysqlPersister {

    /**
     * Get all items that has been liked by given user.
     *
     * @param userId id of the user
     * @param itemType type of the items
     *
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
                "DISTINCT i.id, " +
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
                Item item = null;
                if (itemType.toString().equals(ItemType.BOOK.toString())) {
                    item = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("link")
                    );
                }

                result.add(item);
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getLikedItemsPerUser: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Get all items that has been liked by given user.
     *
     * @param itemType type of the items
     *
     * @return Set of items
     */
    public Set<Item> getAllItems(ItemType itemType) {
        StringBuilder join = new StringBuilder();
        if (itemType.toString().equals(ItemType.BOOK.toString())) {
            join.append("JOIN books AS i ON i.id = likes.item_id ");
        }

        Set<Item> result = new HashSet<>();
        String sql = "" +
            "SELECT " +
                "DISTINCT i.id, " +
                "i.name, " +
                "i.link " +
            "FROM likes " +
            join.toString() +
            "WHERE " +
                "likes.item_type = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, itemType.toString());

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                Item item = null;
                if (itemType.toString().equals(ItemType.BOOK.toString())) {
                    item = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("link")
                    );
                }

                result.add(item);
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on getAllItems: " + ex.getMessage());
        }

        return result;
    }

}
