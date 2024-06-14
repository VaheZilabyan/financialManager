import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpForm extends JFrame {
    public SignUpForm() {
        setTitle("Sign Up");
        setSize(new Dimension(300, 270));
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/sign_up.png").getImage());

        JLabel name_label = new JLabel("Name: ");
        JLabel surname_label = new JLabel("Surname: ");
        JLabel amount_label = new JLabel("Amount: ");
        JLabel username_label = new JLabel("Username: ");
        JLabel password_label = new JLabel("Password: ");
        JLabel pass_again_label = new JLabel("Write again: ");

        JTextField name = new JTextField(20);
        JTextField surname = new JTextField(20);
        JTextField amount = new JTextField(20);
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JPasswordField pass_again = new JPasswordField(20);

        JButton go_back = new JButton("Go Back");
        JButton sign_up = new JButton("Sign Up");

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(name_label);
        panel.add(name);
        panel.add(surname_label);
        panel.add(surname);
        panel.add(amount_label);
        panel.add(amount);
        panel.add(username_label);
        panel.add(username);
        panel.add(password_label);
        panel.add(password);
        panel.add(pass_again_label);
        panel.add(pass_again);
        panel.add(go_back);
        panel.add(sign_up);

        getContentPane().add(panel, BorderLayout.CENTER);

        go_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password1 = password.getPassword();
                char[] password2 = pass_again.getPassword();

                // Convert char arrays to Strings
                String pass1 = new String(password1);
                String pass2 = new String(password2);

                if (!Checker.isValidNameOrSurname(name.getText())
                        || !Checker.isValidNameOrSurname(surname.getText()) ) {
                    JOptionPane.showMessageDialog(null,
                            "Incorrect name or surname",
                            "Validation error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!Checker.isValidAmount(amount.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "Input correct amount",
                            "Validation error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!Checker.isValidUsername(username.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "Username is invalid. It must be 5-15 characters long,\n" +
                                    "start with a letter, contain only alphanumeric characters,\n" +
                                    "and have at least one letter.",
                            "Validation error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (Checker.usernameExists(username.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "Username is already taken. Please choose another one.",
                            "Change Username",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!Checker.isValidPassword(password.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "The password must has at least 8 characters long \n" +
                                    "and contains at least one letter and one number.",
                            "Validation error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!pass1.equals(pass2)) {
                    JOptionPane.showMessageDialog(null,
                            "Write password again",
                            "Error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    String nameText = name.getText();
                    String surnameText = surname.getText();
                    String amountText = amount.getText();
                    String usernameText = username.getText();
                    String passwordText = new String(password.getPassword());
                    addUserToDatabase(nameText, surnameText, amountText, usernameText, passwordText);
                    JOptionPane.showMessageDialog(getContentPane(),
                            "Successfully added",
                            "Added",  // Title
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                }
            }
            private void addUserToDatabase(String name, String surname, String amount, String username, String password) {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = DBConnection.getConnection();
                    String sql = "INSERT INTO users (name, surname, amount, username, password) " +
                            "VALUES (?, ?, ?, ?, ?)";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, surname);
                    statement.setString(3, amount);
                    statement.setString(4, username);
                    statement.setString(5, password);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A new user was inserted successfully!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (statement != null) statement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
