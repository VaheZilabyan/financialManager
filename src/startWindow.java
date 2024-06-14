import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class startWindow extends JFrame {
    public startWindow() {
        setTitle("MyActions");
        setSize(new Dimension(250, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/cash-managements.png").getImage());

        JLabel label = new JLabel("Welcome!");
        Font font = new Font("Arial", Font.BOLD, 24);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        JButton sign_in = new JButton("Sign in");
        JButton sign_up = new JButton("Sign up");
        JButton exit = new JButton("Exit");

        JPanel panel = new JPanel(new GridLayout(4,1));
        panel.add(label);
        panel.add(sign_in);
        panel.add(sign_up);
        panel.add(exit);

        getContentPane().add(panel, BorderLayout.CENTER);

        sign_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInWindow();
            }
        });
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUpWindow();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    private void openSignInWindow() {
        SignInForm sign_in = new SignInForm();
        sign_in.setVisible(true);
    }
    private void openSignUpWindow() {
        SignUpForm sign_up = new SignUpForm();
        sign_up.setVisible(true);
    }
}
