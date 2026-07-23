package view;

import controller.AuthController;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Modern Registration GUI View.
 * Built with FlatLaf for a production-ready, clean fintech aesthetic.
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
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 20;" +
            "background: $Panel.background;" +
            "border: 15,25,15,25");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Create an Account", SwingConstants.CENTER);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");
        cardPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Join us to start tracking your expenses", SwingConstants.CENTER);
        subtitleLabel.putClientProperty(FlatClientProperties.STYLE, "foreground: $Label.disabledForeground");
        cardPanel.add(subtitleLabel, gbc);

        // Full Name Field
        gbc.gridy++;
        txtFullName = new JTextField();
        txtFullName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Full Name");
        cardPanel.add(txtFullName, gbc);

        // Email Field
        gbc.gridy++;
        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email Address");
        cardPanel.add(txtEmail, gbc);

        // Email Validation Label
        gbc.gridy++;
        lblEmailValidation = new JLabel(" ");
        lblEmailValidation.putClientProperty(FlatClientProperties.STYLE, "font: -2");
        cardPanel.add(lblEmailValidation, gbc);

        // Username Field
        gbc.gridy++;
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        cardPanel.add(txtUsername, gbc);

        // Password Field
        gbc.gridy++;
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        cardPanel.add(txtPassword, gbc);

        // Password Strength Label
        gbc.gridy++;
        lblPasswordStrength = new JLabel(" ");
        lblPasswordStrength.putClientProperty(FlatClientProperties.STYLE, "font: -2");
        cardPanel.add(lblPasswordStrength, gbc);

        // Confirm Password Field
        gbc.gridy++;
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm Password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        cardPanel.add(txtConfirmPassword, gbc);

        // Register Button
        gbc.gridy++;
        btnRegister = new JButton("Create Account");
        btnRegister.putClientProperty(FlatClientProperties.STYLE, 
            "background: @accentColor;" +
            "foreground: #ffffff;" +
            "arc: 10;" +
            "font: bold");
        cardPanel.add(btnRegister, gbc);

        // Back to login
        gbc.gridy++;
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.add(new JLabel("Already have an account?"));
        btnBackToLogin = new JButton("Log In");
        btnBackToLogin.putClientProperty(FlatClientProperties.STYLE, "buttonType: borderless; foreground: @accentColor");
        loginPanel.add(btnBackToLogin);
        cardPanel.add(loginPanel, gbc);

        wrapperPanel.add(cardPanel);
        add(wrapperPanel, BorderLayout.CENTER);

        setupListeners();
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
    }

    private void validateEmail() {
        String email = txtEmail.getText();
        if (email.isEmpty()) {
            lblEmailValidation.setText(" ");
        } else if (EMAIL_PATTERN.matcher(email).matches()) {
            lblEmailValidation.setText("Valid email address");
            lblEmailValidation.setForeground(new Color(46, 204, 113)); // Green
        } else {
            lblEmailValidation.setText("Invalid email format");
            lblEmailValidation.setForeground(new Color(231, 76, 60)); // Red
        }
    }

    private void checkPasswordStrength() {
        String pw = new String(txtPassword.getPassword());
        if (pw.isEmpty()) {
            lblPasswordStrength.setText(" ");
        } else if (pw.length() < 6) {
            lblPasswordStrength.setText("Weak: Too short");
            lblPasswordStrength.setForeground(new Color(231, 76, 60));
        } else if (pw.matches(".*\\d.*") && pw.matches(".*[A-Z].*")) {
            lblPasswordStrength.setText("Strong");
            lblPasswordStrength.setForeground(new Color(46, 204, 113));
        } else {
            lblPasswordStrength.setText("Medium: Add numbers & uppercase");
            lblPasswordStrength.setForeground(new Color(241, 196, 15));
        }
    }

    private void handleRegistration() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText();
        String fullName = txtFullName.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulating OTP Verification process
        String otp = JOptionPane.showInputDialog(this, "An OTP has been sent to " + email + ".\nFor testing, enter '1234':", "Email Verification", JOptionPane.INFORMATION_MESSAGE);
        
        if (otp != null && otp.equals("1234")) {
            if (authController.register(username, password, email, fullName)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username or email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid OTP. Registration aborted.", "Verification Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
