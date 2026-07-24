package view;

import controller.AuthController;
import com.formdev.flatlaf.FlatClientProperties;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Premium Login GUI View.
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
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.getBgApp());
        
        // Remove default title bar (optional for modern look, but let's keep it simple for now)
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, Theme.getBgApp());
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Theme.getTextPrimary());
        getRootPane().putClientProperty("JRootPane.titleBarShowIcon", false);
    }

    private void initComponents() {
        // Center wrapper to allow the login card to be centered properly
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Theme.getBgApp());
        
        // The main login card
        RoundedPanel cardPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        cardPanel.setLayout(new GridBagLayout());
        // Shadow effect simulation by adding a border
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Brand / Logo Area
        JLabel brandLabel = new JLabel("ExpenseTracker Pro", SwingConstants.CENTER);
        brandLabel.setFont(Theme.fontHeading2());
        brandLabel.setForeground(Theme.PRIMARY);
        cardPanel.add(brandLabel, gbc);

        // Title
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(Theme.fontHeading1());
        titleLabel.setForeground(Theme.getTextPrimary());
        cardPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 32, 0);
        JLabel subtitleLabel = new JLabel("Enter your credentials to access your account", SwingConstants.CENTER);
        subtitleLabel.setFont(Theme.fontBody());
        subtitleLabel.setForeground(Theme.getTextMuted());
        cardPanel.add(subtitleLabel, gbc);

        // Username Label & Field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(Theme.fontBodyBold());
        lblUsername.setForeground(Theme.getTextPrimary());
        cardPanel.add(lblUsername, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. john_doe");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "margin: 8,12,8,12; arc: 8;");
        txtUsername.setFont(Theme.fontBody());
        cardPanel.add(txtUsername, gbc);

        // Password Label & Field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(Theme.fontBodyBold());
        lblPassword.setForeground(Theme.getTextPrimary());
        cardPanel.add(lblPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 32, 0);
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true; margin: 8,12,8,12; arc: 8;");
        txtPassword.setFont(Theme.fontBody());
        cardPanel.add(txtPassword, gbc);

        // Login Button
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        btnLogin = new JButton("Log In");
        btnLogin.setFont(Theme.fontBodyBold());
        btnLogin.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + String.format("#%06x", Theme.PRIMARY.getRGB() & 0xFFFFFF) + ";" +
            "hoverBackground: " + String.format("#%06x", Theme.PRIMARY_DARK.getRGB() & 0xFFFFFF) + ";" +
            "foreground: #ffffff;" +
            "arc: 12;" +
            "margin: 10,20,10,20;" +
            "borderWidth: 0;" + 
            "focusWidth: 0");
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.add(btnLogin, gbc);

        // Don't have an account? Register
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setOpaque(false);
        JLabel lblNoAccount = new JLabel("Don't have an account?");
        lblNoAccount.setFont(Theme.fontBody());
        lblNoAccount.setForeground(Theme.getTextMuted());
        registerPanel.add(lblNoAccount);
        
        btnRegister = new JButton("Sign up");
        btnRegister.setFont(Theme.fontBodyBold());
        btnRegister.putClientProperty(FlatClientProperties.STYLE, 
            "buttonType: borderless;" +
            "hoverBackground: null;" +
            "foreground: " + String.format("#%06x", Theme.PRIMARY.getRGB() & 0xFFFFFF) + ";");
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerPanel.add(btnRegister);
        
        cardPanel.add(registerPanel, gbc);

        wrapperPanel.add(cardPanel);
        add(wrapperPanel, BorderLayout.CENTER);

        // Action Listeners
        btnLogin.addActionListener(e -> handleLogin());
        
        // Enter key to login on password field
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                }
            }
        });

        btnRegister.addActionListener(e -> {
            new RegisterView().setVisible(true);
            this.dispose();
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            ToastNotification.show(this, "Please enter both username and password.", ToastNotification.WARNING);
            txtUsername.putClientProperty(FlatClientProperties.OUTLINE, "error");
            txtPassword.putClientProperty(FlatClientProperties.OUTLINE, "error");
            return;
        }

        // Clear outlines
        txtUsername.putClientProperty(FlatClientProperties.OUTLINE, null);
        txtPassword.putClientProperty(FlatClientProperties.OUTLINE, null);

        // Show loading state (simulated by disabling button text momentarily)
        btnLogin.setText("Logging in...");
        btnLogin.setEnabled(false);

        SwingUtilities.invokeLater(() -> {
            if (authController.login(username, password)) {
                new DashboardView().setVisible(true);
                this.dispose();
            } else {
                ToastNotification.show(this, "Invalid credentials. Please try again.", ToastNotification.ERROR);
                txtUsername.putClientProperty(FlatClientProperties.OUTLINE, "error");
                txtPassword.putClientProperty(FlatClientProperties.OUTLINE, "error");
                btnLogin.setText("Log In");
                btnLogin.setEnabled(true);
            }
        });
    }
}
