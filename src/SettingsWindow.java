import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {
    private userWindow userWindow;
    String username;
    public SettingsWindow(userWindow userWindow, String username) {
        this.userWindow = userWindow;
        this.username = username;
        setTitle("Settings");
        setSize(new Dimension(300, 250));
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/settings.png").getImage());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Appearance Tab
        JPanel appearancePanel = new JPanel(new GridLayout(3, 2));
        appearancePanel.add(new JLabel("Theme:"));
        String[] themes = {"Metal", "Nimbus", "CDE/Motif", "Windows", "Windows Classic"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        appearancePanel.add(themeComboBox);
        appearancePanel.add(new JLabel(""));
        appearancePanel.add(new JLabel(""));
        tabbedPane.add("Appearance", appearancePanel);

        // Account Tab
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        JButton changePasswordButton = new JButton("Change Password");
        JPanel button_panel = new JPanel(new GridLayout(1,1));
        button_panel.add(changePasswordButton);
        accountPanel.add(button_panel);
        accountPanel.add(Box.createVerticalStrut(300));
        tabbedPane.add("Account", accountPanel);

        // Add tabbed pane to the dialog
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Add OK button
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Confirm all changes");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheme = (String) themeComboBox.getSelectedItem();
                userWindow.updateTheme(selectedTheme);
                //settingsDialog.dispose();
                dispose();
            }
        });
        buttonPanel.add(okButton);

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordWindow();
            }
        });

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private void changePasswordWindow() {
        changePassword frame = new changePassword(username);

    }
}
