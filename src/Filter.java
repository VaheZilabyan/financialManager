import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class Filter extends JFrame {
    DefaultTableModel model;
    String id;
    StringBuilder query = null;
    public Filter(DefaultTableModel model, String id) {
        this.model = model;
        this.id = id;
        setTitle("Filter");
        setSize(320, 485);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/filter.png").getImage());
        setVisible(true);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Transaction Date panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        datePanel.setBorder(BorderFactory.createTitledBorder("Transaction Date (yyyy-MM-dd)"));
        datePanel.add(new JLabel("From:"));
        JTextField fromDate = new JTextField();
        datePanel.add(fromDate);
        datePanel.add(new JLabel("To:"));
        JTextField toDate = new JTextField();
        datePanel.add(toDate);
        mainPanel.add(datePanel);

        // Transaction Type panel
        JPanel typePanel = new JPanel(new GridLayout(3, 1, 5, 5));
        typePanel.setBorder(BorderFactory.createTitledBorder("Transaction Type"));
        JRadioButton incomeButton = new JRadioButton("Only Income");
        JRadioButton expenseButton = new JRadioButton("Only Expense");
        JRadioButton showAllButton = new JRadioButton("Show All");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(incomeButton);
        typeGroup.add(expenseButton);
        typeGroup.add(showAllButton);
        typePanel.add(incomeButton);
        typePanel.add(expenseButton);
        typePanel.add(showAllButton);
        showAllButton.setSelected(true);
        mainPanel.add(typePanel);

        // Condition and Value panel
        JPanel conditionPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        conditionPanel.setBorder(BorderFactory.createTitledBorder("Money"));
        conditionPanel.add(new JLabel("Condition:"));
        JTextField conditionField = new JTextField();
        conditionPanel.add(conditionField);
        conditionPanel.add(new JLabel("Value:"));
        JTextField valueField = new JTextField();
        conditionPanel.add(valueField);
        mainPanel.add(conditionPanel);

        // Successfully panel
        JPanel successPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        successPanel.setBorder(BorderFactory.createTitledBorder("Successfully"));
        JRadioButton successButton = new JRadioButton("Only Successfully");
        JRadioButton notSuccessButton = new JRadioButton("Only Not Successfully");
        JRadioButton showAllSuccessButton = new JRadioButton("Show All");
        ButtonGroup successGroup = new ButtonGroup();
        successGroup.add(successButton);
        successGroup.add(notSuccessButton);
        successGroup.add(showAllSuccessButton);
        successPanel.add(successButton);
        successPanel.add(notSuccessButton);
        successPanel.add(showAllSuccessButton);
        showAllSuccessButton.setSelected(true);
        mainPanel.add(successPanel);

        // Limit panel
        JPanel limitPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        limitPanel.setBorder(BorderFactory.createTitledBorder("Limit"));
        limitPanel.add(new JLabel("Enter LIMIT:"));
        JTextField limitField = new JTextField();
        limitPanel.add(limitField);
        mainPanel.add(limitPanel);

        // Filter button
        JButton filterButton = new JButton("Filter");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(filterButton);
        mainPanel.add(buttonPanel);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                query = new StringBuilder("SELECT * FROM transaction WHERE user_id = ");
                query.append(id);

                if (!fromDate.getText().isEmpty()) {
                    String dateFrom = fromDate.getText();
                    if (Checker.isValidDate(dateFrom)) {
                        query.append(" AND transaction_date >= '").append(dateFrom).append("'");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid date, input correct date! (yyyy-MM-dd)",
                                "Wrong DATE",  // Title
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

                if (!toDate.getText().isEmpty()) {
                    String dateTo = toDate.getText();
                    if (Checker.isValidDate(dateTo)) {
                        query.append(" AND transaction_date <= '").append(dateTo).append("'");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid date, input correct date! (yyyy-MM-dd)",
                                "Wrong DATE",  // Title
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

                String selectedActionType = GroupButtonUtils.getSelectedButtonText(typeGroup);
                if (selectedActionType.equals("Only Income")) {
                    query.append(" AND action_type = 'income'");
                } else if (selectedActionType.equals("Only Expense")) {
                    query.append(" AND action_type = 'expense'");
                }

                if (!conditionField.getText().isEmpty() && !valueField.getText().isEmpty()) {
                    String condition = conditionField.getText();
                    String value = valueField.getText();
                    if (Checker.isCorrectCondition(condition) && Checker.isNumberAndMoreThanZero(value)) {
                        query.append(" AND value " + condition).append(value);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Wrong condition or money",
                                "Error",  // Title
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

                String selectedSuccessType = GroupButtonUtils.getSelectedButtonText(successGroup);
                if (selectedSuccessType.equals("Only Successfully")) {
                    query.append(" AND is_successful = 1");
                } else if (selectedSuccessType.equals("Only Not Successfully")) {
                    query.append(" AND is_successful = 0");
                }

                if (!limitField.getText().isEmpty()) {
                    String limit = limitField.getText();
                    if (Checker.isValidID(limit)) {
                        query.append(" LIMIT ").append(limit);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Wrong LIMIT, input number > 0",
                                "Error",  // Title
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }

                filter(model);
            }
        });
    }
    public void filter(DefaultTableModel model) {
        String sql;
        if (query == null) {
            query = new StringBuilder("SELECT * FROM transaction WHERE user_id = ");
            query.append(id);
            sql = query.toString();
        } else {
            sql = query.toString();
        }
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            model.setColumnCount(0);
            model.setRowCount(0);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names to model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }
            // Add data rows to model
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class GroupButtonUtils {
    public static String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
}
