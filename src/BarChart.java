import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarChart extends JFrame {
    private String username;
    private int userId;

    public BarChart(String title, String username) {
        super(title);
        this.username = username;
        this.userId = getUsernameId();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(new ImageIcon("icons/chart.png").getImage());

        // Create dataset
        CategoryDataset dataset = createDataset();

        // Check if dataset is empty
        if (dataset.getRowCount() == 0 || dataset.getColumnCount() == 0) {
            System.out.println("Dataset is empty");
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Income and Expenses Over Time",
                "Date",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize the chart
        // ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT action_type, DATE(transaction_date) as date, SUM(value) as total_value " +
                    "FROM transaction WHERE user_id = ? AND is_successful = 1 " +
                    "GROUP BY action_type, DATE(transaction_date)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String actionType = resultSet.getString("action_type");
                double totalValue = resultSet.getDouble("total_value");
                String date = resultSet.getString("date");

                // Debugging: Print fetched data
                //System.out.println("Fetched data - Type: " + actionType + ", Total Value: " + totalValue + ", Date: " + date);

                dataset.addValue(totalValue, actionType, date);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // Debugging: Print dataset values
        /*for (int row = 0; row < dataset.getRowCount(); row++) {
            for (int col = 0; col < dataset.getColumnCount(); col++) {
                System.out.println("Dataset - Row: " + dataset.getRowKey(row) + ", Column: " + dataset.getColumnKey(col) + ", Value: " + dataset.getValue(row, col));
            }
        }*/

        return dataset;
    }

    private int getUsernameId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT id FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1; // Username not found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
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
