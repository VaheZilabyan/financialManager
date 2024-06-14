import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInForm extends JFrame {
    public SignInForm() {
        setTitle("Sign In");
        setSize(new Dimension(300, 140));
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/sign_in.png").getImage());

        JLabel username_label = new JLabel("Username: ");
        JLabel password_label = new JLabel("Password: ");
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JButton go_back = new JButton("Back");
        JButton login = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.add(username_label);
        panel.add(username);
        panel.add(password_label);
        panel.add(password);
        panel.add(go_back);
        panel.add(login);

        getContentPane().add(panel);

        go_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] cpass = password.getPassword();
                String pass = new String(cpass);
                if (username.getText().equals("admin") && pass.equals("admin")) {
                    AdminWindow admin = new AdminWindow();
                    dispose();
                    return;
                }

                if (Checker.usernameExists(username.getText())
                    && Checker.passwordExists(password.getText()))
                {
                    userWindow mw = new userWindow(username.getText());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Wrong username or password!",
                            "Error",  // Title
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}
