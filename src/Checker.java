import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {
    public static boolean isValidTime(String input) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setLenient(false);

        try {
            timeFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean isValidDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean isValidID(String input) {
        try {
            int id = Integer.parseInt(input);
            return id > 0;
        } catch (NumberFormatException e1) {
            return false;
        }
    }
    public static boolean isNumber(String text) {
        try {
            if (text.isEmpty()) return false;
            double t = Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isNumberAndMoreThanZero(String text) {
        try {
            if (text.isEmpty()) return false;
            double t = Double.parseDouble(text);
            if (t < 0) return false;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidNameOrSurname(String name) {
        String regex = "^[A-Z][a-zA-Z]*$";
        return name.matches(regex);
    }
    public static boolean isValidUsername(String username) {
        String regex = "^(?=.*[A-Za-z])[A-Za-z][A-Za-z0-9]{4,14}$";
        return username.matches(regex);
    }
    public static boolean isValidAmount(String text) {
        try {
            if (text.isEmpty()) return false;
            double t = Double.parseDouble(text);
            if (t < 0) return false;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(passwordRegex);
    }
    public static boolean existsInDatabase(String tableName, String genreName, String text) {
        Connection connection = DBConnection.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + genreName + " = ?";
            System.out.println(sql);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, text);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            // Genre name already exists in the database
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean isCorrectCondition(String text) {
        String regex = "(=|!=|>|<|>=|<=)";
        return text.matches(regex);
    }
    public static boolean checkIDExists(String tableName, String key_name, int id) {
        boolean exists = false;
        String sql = "SELECT * FROM " + tableName + " WHERE " + key_name + " = ?";
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                exists = resultSet.next(); // If resultSet.next() returns true, the ID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    public static boolean usernameExists(String username) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnection.getConnection();
            // SQL query to check if the username exists
            String sql = "SELECT * FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            // Check if any result is returned
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static boolean passwordExists(String password) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnection.getConnection();
            // SQL query to check if the username exists
            String sql = "SELECT * FROM users WHERE password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, password);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            // Check if any result is returned
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
