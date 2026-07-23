package view;

import controller.AuthController;
import com.formdev.flatlaf.FlatClientProperties;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

/**
 * Premium Registration GUI View.
 */
public class RegisterView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    private JLabel lblPasswordStrength;
    private JLabel lblEmailValidation;
    private AuthController authController;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public RegisterView() {
        authController = new AuthController();
        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle("Expense Tracker Pro - Register");
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.getBgApp());
        
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, Theme.getBgApp());
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Theme.getTextPrimary());
    }

    private void initComponents() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Theme.getBgApp());
        
        RoundedPanel cardPanel = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.getBorder(), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Title
        JLabel titleLabel = new JLabel("Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(Theme.fontHeading1());
        titleLabel.setForeground(Theme.getTextPrimary());
        gbc.insets = new Insets(0, 0, 8, 0);
        cardPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Join us to start tracking your expenses", SwingConstants.CENTER);
        subtitleLabel.setFont(Theme.fontBody());
        subtitleLabel.setForeground(Theme.getTextMuted());
        gbc.insets = new Insets(0, 0, 24, 0);
        cardPanel.add(subtitleLabel, gbc);

        // Full Name
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(createLabel("Full Name"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        txtFullName = createTextField("e.g. Jane Doe");
        cardPanel.add(txtFullName, gbc);

        // Email
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(createLabel("Email Address"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        txtEmail = createTextField("name@example.com");
        cardPanel.add(txtEmail, gbc);

        // Email Validation Label
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        lblEmailValidation = new JLabel(" ");
        lblEmailValidation.setFont(Theme.fontSmall());
        cardPanel.add(lblEmailValidation, gbc);

        // Username
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(createLabel("Username"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        txtUsername = createTextField("Choose a username");
        cardPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(createLabel("Password"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        txtPassword = createPasswordField();
        cardPanel.add(txtPassword, gbc);

        // Password Strength Label
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        lblPasswordStrength = new JLabel(" ");
        lblPasswordStrength.setFont(Theme.fontSmall());
        cardPanel.add(lblPasswordStrength, gbc);

        // Confirm Password
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(createLabel("Confirm Password"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        txtConfirmPassword = createPasswordField();
        cardPanel.add(txtConfirmPassword, gbc);

        // Register Button
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        btnRegister = new JButton("Create Account");
        btnRegister.setFont(Theme.fontBodyBold());
        btnRegister.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + String.format("#%06x", Theme.PRIMARY.getRGB() & 0xFFFFFF) + ";" +
            "foreground: #ffffff;" +
            "arc: 8;" +
            "margin: 10,20,10,20;" +
            "borderWidth: 0;" + 
            "focusWidth: 0");
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.add(btnRegister, gbc);

        // Back to login
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);
        JLabel lblAlready = new JLabel("Already have an account?");
        lblAlready.setFont(Theme.fontBody());
        lblAlready.setForeground(Theme.getTextMuted());
        loginPanel.add(lblAlready);
        
        btnBackToLogin = new JButton("Log In");
        btnBackToLogin.setFont(Theme.fontBodyBold());
        btnBackToLogin.putClientProperty(FlatClientProperties.STYLE, 
            "buttonType: borderless;" +
            "foreground: " + String.format("#%06x", Theme.PRIMARY.getRGB() & 0xFFFFFF) + ";");
        btnBackToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(btnBackToLogin);
        
        cardPanel.add(loginPanel, gbc);

        wrapperPanel.add(cardPanel);
        
        // Wrap the whole thing in a scroll pane just in case on smaller screens
        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setupListeners();
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.fontBodyBold());
        label.setForeground(Theme.getTextPrimary());
        return label;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        field.putClientProperty(FlatClientProperties.STYLE, "margin: 8,12,8,12; arc: 8;");
        field.setFont(Theme.fontBody());
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        field.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true; margin: 8,12,8,12; arc: 8;");
        field.setFont(Theme.fontBody());
        return field;
    }

    private void setupListeners() {
        btnRegister.addActionListener(e -> handleRegistration());

        btnBackToLogin.addActionListener(e -> {
            new LoginView().setVisible(true);
            this.dispose();
        });

        // Real-time Email Validation
        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
        });

        // Password Strength Indicator
        txtPassword.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkPasswordStrength(); }
            public void removeUpdate(DocumentEvent e) { checkPasswordStrength(); }
            public void changedUpdate(DocumentEvent e) { checkPasswordStrength(); }
        });
        
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleRegistration();
                }
            }
        };
        txtConfirmPassword.addKeyListener(enterListener);
    }

    private void validateEmail() {
        String email = txtEmail.getText();
        if (email.isEmpty()) {
            lblEmailValidation.setText(" ");
            txtEmail.putClientProperty(FlatClientProperties.OUTLINE, null);
        } else if (EMAIL_PATTERN.matcher(email).matches()) {
            lblEmailValidation.setText("Valid email address");
            lblEmailValidation.setForeground(Theme.SUCCESS);
            txtEmail.putClientProperty(FlatClientProperties.OUTLINE, Theme.SUCCESS);
        } else {
            lblEmailValidation.setText("Invalid email format");
            lblEmailValidation.setForeground(Theme.DANGER);
            txtEmail.putClientProperty(FlatClientProperties.OUTLINE, Theme.DANGER);
        }
    }

    private void checkPasswordStrength() {
        String pw = new String(txtPassword.getPassword());
        if (pw.isEmpty()) {
            lblPasswordStrength.setText(" ");
            txtPassword.putClientProperty(FlatClientProperties.OUTLINE, null);
        } else if (pw.length() < 6) {
            lblPasswordStrength.setText("Weak: Too short");
            lblPasswordStrength.setForeground(Theme.DANGER);
            txtPassword.putClientProperty(FlatClientProperties.OUTLINE, Theme.DANGER);
        } else if (pw.matches(".*\\d.*") && pw.matches(".*[A-Z].*")) {
            lblPasswordStrength.setText("Strong");
            lblPasswordStrength.setForeground(Theme.SUCCESS);
            txtPassword.putClientProperty(FlatClientProperties.OUTLINE, Theme.SUCCESS);
        } else {
            lblPasswordStrength.setText("Medium: Add numbers & uppercase");
            lblPasswordStrength.setForeground(Theme.WARNING);
            txtPassword.putClientProperty(FlatClientProperties.OUTLINE, Theme.WARNING);
        }
    }

    private void handleRegistration() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            ToastNotification.show(this, "Please fill in all fields.", ToastNotification.WARNING);
            return;
        }

        if (!password.equals(confirm)) {
            ToastNotification.show(this, "Passwords do not match.", ToastNotification.WARNING);
            txtConfirmPassword.putClientProperty(FlatClientProperties.OUTLINE, Theme.DANGER);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            ToastNotification.show(this, "Please enter a valid email address.", ToastNotification.WARNING);
            return;
        }

        // Simulating OTP Verification process
        String otp = JOptionPane.showInputDialog(this, "An OTP has been sent to " + email + ".\nFor testing, enter '1234':", "Email Verification", JOptionPane.INFORMATION_MESSAGE);
        
        if (otp != null && otp.equals("1234")) {
            btnRegister.setText("Creating Account...");
            btnRegister.setEnabled(false);
            
            SwingUtilities.invokeLater(() -> {
                if (authController.register(username, password, email, fullName)) {
                    ToastNotification.show(this, "Account created successfully! You can now log in.", ToastNotification.SUCCESS);
                    new LoginView().setVisible(true);
                    this.dispose();
                } else {
                    ToastNotification.show(this, "Registration failed. Username or email might already exist.", ToastNotification.ERROR);
                    btnRegister.setText("Create Account");
                    btnRegister.setEnabled(true);
                }
            });
        } else if (otp != null) {
            ToastNotification.show(this, "Invalid OTP. Registration aborted.", ToastNotification.ERROR);
        }
    }
}
