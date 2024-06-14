import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class userWindow extends JFrame {
    String username;
    double userAmount;
    int userId;
    public userWindow(String username) {
        this.username = username;
        this.userId = DBConnection.getUsernameId(username);
        setTitle(username);
        setSize(new Dimension(900, 500));
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(new ImageIcon("icons/cash-managements.png").getImage());


        //NORTH
        JButton information_button = new JButton("About user");
        JLabel name_surname_label = new JLabel();
        JLabel action_type_label = new JLabel("Action type ");
        action_type_label.setHorizontalAlignment(JLabel.RIGHT);
        action_type_label.setVerticalAlignment(JLabel.CENTER);
        JTextField textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        JButton do_button = new JButton("DO");
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.insertItemAt("Expense", 0);
        comboBox.insertItemAt("Income", 1);
        comboBox.setSelectedIndex(0);

        JPanel northPanel = new JPanel(new GridLayout(1, 5));
        northPanel.add(information_button);
        northPanel.add(action_type_label);
        northPanel.add(comboBox);
        northPanel.add(textField);
        northPanel.add(do_button);

        //SOUTH
        JLabel total_label = new JLabel("Your balance: ");
        userAmount = Double.parseDouble( DBConnection.getUserAmount(username) );
        JLabel balance_label = new JLabel("Your balance:  " + String.valueOf( userAmount ) + " ");
        balance_label.setHorizontalAlignment(SwingConstants.RIGHT);
        balance_label.setVerticalAlignment(JLabel.CENTER);
        JButton exportButton = new JButton("Export to CSV");
        JButton settings_button = new JButton("Settings");
        JButton bar_chart_button = new JButton("Open Chart");

        JPanel southPanel = new JPanel(new GridLayout(1,4));
        southPanel.add(settings_button);
        southPanel.add(bar_chart_button);
        southPanel.add(exportButton);
        southPanel.add(balance_label);

        //CENTER
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable();
        table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(table);

        //WEST
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));


        JButton filter = new JButton("Filter");
        JButton show_all_transactions = new JButton("Show all transactions");
        JPanel filter_panel = new JPanel(new GridLayout(2,1));
        filter_panel.add(filter);
        filter_panel.add(show_all_transactions);

        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Filter frame = new Filter(model, String.valueOf(userId));
            }
        });

        westPanel.add(Box.createVerticalStrut(330));
        westPanel.add(filter_panel);

        settings_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingsWindow();
            }
        });

        show_all_transactions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBConnection.showMyActions(model, "transaction", String.valueOf(userId));
            }
        });

        bar_chart_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarChart chart = new BarChart("Income and Expense Chart", username);
            }
        });

        information_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = DBConnection.getUser_Name(username);
                String surname = DBConnection.getUser_Surname(username);
                //AboutUser userInfo = new AboutUser(name, surname, username, String.valueOf(userAmount));
                JOptionPane.showMessageDialog(null,
                        "Name: " + name + "\nSurname: " + surname + "\nUsername: " + username + "\nAmount: " + String.valueOf(userAmount)
                        , "About user",  // Title
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CSVExporter.exportTableToCSV(table, new File(username + "_transactions.csv"));
                    JOptionPane.showMessageDialog(null, "Transactions exported successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error exporting transactions.");
                    ex.printStackTrace();
                }
            }
        });

        getContentPane().add(westPanel, BorderLayout.WEST);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        DBConnection.showMyActions(model, "transaction", String.valueOf(userId));
        do_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Checker.isNumberAndMoreThanZero(textField.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid data, enter a number greater than zero!",
                            "Wrong input",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    //create new transaction and add data with these values

                    String id = String.valueOf(userId);
                    double value = Double.parseDouble( textField.getText() );
                    String type = comboBox.getSelectedItem().toString();
                    Timestamp datetime = new Timestamp(System.currentTimeMillis());
                    boolean is_successful = true;
                    if (type.equals("Expense")) {
                        is_successful = userAmount - value < 0 ? false : true;
                    }

                    TransactionManager newaction = new TransactionManager();
                    newaction.addTransaction(userId, type, BigDecimal.valueOf(value), is_successful);

                    DBConnection.showMyActions(model, "transaction", String.valueOf(userId));
                    if (is_successful) {
                        updateTotalAmount(type, balance_label, userAmount, value);
                    }
                    textField.setText("");
                }
            }
        });
    }
    private void openSettingsWindow() {
        SettingsWindow sw = new SettingsWindow(this, username);
    }
    public void updateTheme(String theme) {
        try {
            switch (theme) {
                case "Metal":
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "CDE/Motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "Windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case "Windows Classic":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
            }
            // Update the look and feel for the entire application
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateTotalAmount(String actionType, JLabel total, double amount, double value) {
        double result = 0.0;
        if (actionType.equals("Expense")) {
            result = amount - value;
        } else if (actionType.equals("Income")) {
            result = amount + value;
        }
        total.setText("Your balance:  " + String.valueOf(result) + " ");
        boolean success = updateUserAmount(userId, BigDecimal.valueOf( result ));
        userAmount = result;
        if (success) {
            System.out.println("User amount updated successfully.");
        } else {
            System.out.println("Failed to update user amount.");
        }
    }
    public static boolean updateUserAmount(int userId, BigDecimal newAmount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "UPDATE users SET amount = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, newAmount);
            preparedStatement.setInt(2, userId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
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
