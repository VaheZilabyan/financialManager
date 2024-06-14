import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionManager {
    public void addTransaction(int userId, String actionType, BigDecimal value, boolean isSuccessful) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "INSERT INTO transaction (user_id, action_type, value, transaction_date, is_successful) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, actionType);
            preparedStatement.setBigDecimal(3, value);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setBoolean(5, isSuccessful);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new transaction was inserted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
