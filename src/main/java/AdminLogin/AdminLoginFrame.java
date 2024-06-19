package AdminLogin;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AdminLoginListener adminLoginListener;

    public AdminLoginFrame() {
        initializeFrame();
        JPanel panel = createFormPanel();
        add(panel);
        setVisible(true);

        KeyStroke keyStroke = KeyStroke.getKeyStroke("ENTER");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "login");
        panel.getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void initializeFrame() {
        setTitle("Admin Login");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        Border emptyBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        panel.setBorder(emptyBorder);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton submitButton = createSubmitButton();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(submitButton);

        return panel;
    }

    private JButton createSubmitButton() {
        JButton submitButton = new JButton("Login");
        submitButton.setFocusable(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        return submitButton;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        boolean loginSuccessful = Admin.checkLogin(username, password);
        if (loginSuccessful) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }

        if (adminLoginListener != null) {
            adminLoginListener.adminLoginEventOccurred(new AdminLoginEvent(this,loginSuccessful));
        }
    }

    public void setAdminLoginListener(AdminLoginListener listener) {
        this.adminLoginListener = listener;
    }
}
