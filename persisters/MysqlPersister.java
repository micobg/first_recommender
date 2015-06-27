package persisters;

import storage.MysqlConnection;

import java.sql.*;
import java.util.*;

public class MysqlPersister {

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
            "WHERE item_type = ? " +
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
        String sql = "SELECT DISTINCT item_id, COUNT(*) AS likes_count FROM likes GROUP BY item_id";

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

}
