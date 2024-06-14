import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminWindow extends JFrame {
    JTable table;
    public AdminWindow() {
        setTitle("Admin");
        setSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(new ImageIcon("icons/admin.png").getImage());

        DefaultTableModel model = new DefaultTableModel();
        table = new JTable();
        table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(table);
        DBConnection.printTable(model, "users");

        JButton see_transactions = new JButton("See transactions");
        see_transactions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedRowId();
                transactionsWindow(id);
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(see_transactions, BorderLayout.SOUTH);
    }

    private void transactionsWindow(int id) {
        JFrame frame = new JFrame();
        frame.setTitle("Transactions");
        frame.setSize(new Dimension(600, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("transaction.png").getImage());

        DefaultTableModel model = new DefaultTableModel();
        JTable tr_table = new JTable();
        tr_table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(tr_table);
        DBConnection.showMyActions(model, "transaction", String.valueOf(id));

        JButton filterButton = new JButton("Filter");
        JButton chartButton = new JButton("Open Chart");
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(filterButton);
        buttonPanel.add(chartButton);

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Filter filterWindow = new Filter(model, String.valueOf(id));
            }
        });

        chartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUsername_fromID(String.valueOf(id));
                BarChart chart = new BarChart("Income and Expense Chart", username);
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private int getSelectedRowId() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            // Assuming ID is in the first column (index 0)
            Object idObject = table.getValueAt(selectedRow, 0);
            int id = Integer.parseInt(idObject.toString());
            return id;
        } else {
            return -1;
        }
    }
    private String getUsername_fromID(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT username FROM users WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                return null; // Username not found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
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
