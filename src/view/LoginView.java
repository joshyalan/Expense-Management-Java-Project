package view;

import controller.AuthController;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

/**
 * Modern Login GUI View.
 * Built with FlatLaf for a production-ready, clean fintech aesthetic.
 */
public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();
        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle("Expense Tracker Pro - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        
        // Remove default title bar (optional for modern look, but let's keep it simple for now)
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, new Color(25, 25, 30));
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Color.WHITE);
    }

    private void initComponents() {
        // Center wrapper to allow the login card to be centered properly
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        
        // The main login card
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 20;" +
            "background: $Panel.background;" +
            "border: 10,10,10,10");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");
        cardPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Log in to manage your finances", SwingConstants.CENTER);
        subtitleLabel.putClientProperty(FlatClientProperties.STYLE, "foreground: $Label.disabledForeground");
        cardPanel.add(subtitleLabel, gbc);

        // Username Field
        gbc.gridy++;
        gbc.gridwidth = 2;
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        cardPanel.add(txtUsername, gbc);

        // Password Field
        gbc.gridy++;
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        cardPanel.add(txtPassword, gbc);

        // Login Button
        gbc.gridy++;
        btnLogin = new JButton("Log In");
        btnLogin.putClientProperty(FlatClientProperties.STYLE, 
            "background: @accentColor;" +
            "foreground: #ffffff;" +
            "arc: 10;" +
            "font: bold");
        cardPanel.add(btnLogin, gbc);

        // Don't have an account? Register
        gbc.gridy++;
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.add(new JLabel("Don't have an account?"));
        btnRegister = new JButton("Register");
        btnRegister.putClientProperty(FlatClientProperties.STYLE, 
            "buttonType: borderless;" +
            "foreground: @accentColor");
        registerPanel.add(btnRegister);
        cardPanel.add(registerPanel, gbc);

        wrapperPanel.add(cardPanel);
        add(wrapperPanel, BorderLayout.CENTER);

        // Action Listeners
        btnLogin.addActionListener(e -> handleLogin());
        
        // Enter key to login
        getRootPane().setDefaultButton(btnLogin);

        btnRegister.addActionListener(e -> {
            new RegisterView().setVisible(true);
            this.dispose();
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (authController.login(username, password)) {
            new DashboardView().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
