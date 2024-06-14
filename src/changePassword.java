import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class changePassword extends JFrame {
    String username;

    public changePassword(String username) {
        this.username = username;
        setTitle("Settings");
        setSize(new Dimension(300, 170));
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/password.png").getImage());
        setVisible(true);

        JLabel old_label = new JLabel("Old password: ");
        JLabel new_label = new JLabel("New password: ");
        JLabel confirm_label = new JLabel("Confirm new password: ");

        JPasswordField old_password = new JPasswordField(20);
        JPasswordField new_password = new JPasswordField(20);
        JPasswordField confirm_password = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(old_label);
        panel.add(old_password);
        panel.add(new_label);
        panel.add(new_password);
        panel.add(confirm_label);
        panel.add(confirm_password);

        JButton change_button = new JButton("Change");

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(change_button, BorderLayout.SOUTH);

        change_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String current_password = DBConnection.getUser_Password(username);

                char[] char_old_password = old_password.getPassword();
                char[] char_new_password = new_password.getPassword();
                char[] char_confirm_password = confirm_password.getPassword();

                // Convert char arrays to Strings
                String String_old_password = new String(char_old_password);
                String String_new_password = new String(char_new_password);
                String String_confirm_password = new String(char_confirm_password);

                if (!String_old_password.equals(current_password)) {
                    JOptionPane.showMessageDialog(null,
                            "Wrong old password",
                            "Error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!Checker.isValidPassword(String_new_password)) {
                    JOptionPane.showMessageDialog(null,
                            "The password must has at least 8 characters long \n" +
                                    "and contains at least one letter and one number.",
                            "Validation error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!String_new_password.equals(String_confirm_password)) {
                    JOptionPane.showMessageDialog(null,
                            "Confirm new password",
                            "Error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    changePassword(String_new_password);
                    JOptionPane.showMessageDialog(null,
                            "Password changed successfully",
                            "Password",  // Title
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                }
            }
        });
    }

    private void changePassword(String new_password) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            String sql = "UPDATE users"
                    + " SET password = ?"
                    + " WHERE username = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, new_password);
            statement.setString(2, username);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Password was changed successfully!");
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
}
