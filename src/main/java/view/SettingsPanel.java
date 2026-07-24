package view;

import com.formdev.flatlaf.FlatClientProperties;
import utils.SessionManager;
import utils.ui.Theme;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import org.mindrot.jbcrypt.BCrypt;
import dao.UserDAOImpl;
import model.User;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.getBgApp());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        initComponents();
    }

    private void initComponents() {
        // Title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Application Settings");
        title.setFont(Theme.fontHeading2());
        title.setForeground(Theme.getTextPrimary());
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Area (Grid of Settings Cards)
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 24, 24));
        centerPanel.setOpaque(false);

        // 1. Preferences
        RoundedPanel prefCard = createSettingsCard("Preferences");
        
        JLabel lblCurrency = createLabel("Default Currency:");
        lblCurrency.setAlignmentX(Component.LEFT_ALIGNMENT);
        prefCard.add(lblCurrency);
        prefCard.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboPanel.setOpaque(false);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<String> cbCurrency = new JComboBox<>(new String[]{
            "🇺🇸 USD ($)", "🇪🇺 EUR (€)", "🇬🇧 GBP (£)", "🇮🇳 INR (₹)", "🇯🇵 JPY (¥)", "🇨🇦 CAD ($)", "🇦🇺 AUD ($)"
        });
        cbCurrency.setFont(Theme.fontBodyBold());
        cbCurrency.putClientProperty(FlatClientProperties.STYLE, 
            "margin: 8,16,8,16; " +
            "arc: 12; " +
            "background: #F0F4FF; " +
            "foreground: #3742FA; " +
            "hoverBackground: #E0E8FF; " +
            "selectionBackground: #3742FA; " +
            "selectionForeground: #FFFFFF; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        cbCurrency.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comboPanel.add(cbCurrency);
        prefCard.add(comboPanel);
        
        prefCard.add(Box.createRigidArea(new Dimension(0, 16)));
        
        JCheckBox chkEmail = new JCheckBox("Receive Email Notifications");
        chkEmail.setSelected(true);
        chkEmail.setFont(Theme.fontBody());
        chkEmail.setForeground(Theme.getTextPrimary());
        chkEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        prefCard.add(chkEmail);
        centerPanel.add(prefCard);

        // 2. Database Backup
        RoundedPanel backupCard = createSettingsCard("Data Management");
        backupCard.add(createLabel("Backup your entire financial data safely."));
        backupCard.add(Box.createRigidArea(new Dimension(0, 16)));
        
        JButton btnBackup = new JButton("Export Database SQL");
        btnBackup.setFont(Theme.fontBodyBold());
        btnBackup.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.PRIMARY) + "; foreground: #ffffff; padding: 8,16,8,16; borderWidth: 0");
        btnBackup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBackup.addActionListener(e -> ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Backup feature requires mysqldump installed on path.", ToastNotification.INFO));
        backupCard.add(btnBackup);
        centerPanel.add(backupCard);
        
        // 3. Smart Features & AI
        RoundedPanel aiCard = createSettingsCard("Smart Features");
        aiCard.add(createLabel("Enable AI spending insights and anomaly detection."));
        aiCard.add(Box.createRigidArea(new Dimension(0, 16)));
        
        JCheckBox chkAIInsights = new JCheckBox("Enable AI Insights");
        chkAIInsights.setSelected(true);
        chkAIInsights.setFont(Theme.fontBody());
        chkAIInsights.setForeground(Theme.getTextPrimary());
        aiCard.add(chkAIInsights);
        
        JCheckBox chkAnomalies = new JCheckBox("Detect Unusual Spending");
        chkAnomalies.setSelected(true);
        chkAnomalies.setFont(Theme.fontBody());
        chkAnomalies.setForeground(Theme.getTextPrimary());
        aiCard.add(chkAnomalies);
        
        centerPanel.add(aiCard);

        // 4. Security
        RoundedPanel secCard = createSettingsCard("Security");
        secCard.add(createLabel("Manage your account security."));
        secCard.add(Box.createRigidArea(new Dimension(0, 16)));
        
        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setFont(Theme.fontBodyBold());
        btnChangePassword.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: " + hex(Theme.getBorder()) + "; foreground: " + hex(Theme.getTextPrimary()) + "; padding: 8,16,8,16; borderWidth: 0");
        btnChangePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePassword.addActionListener(e -> showChangePasswordDialog());
        secCard.add(btnChangePassword);
        
        centerPanel.add(secCard);

        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void showChangePasswordDialog() {
        JPasswordField txtCurrent = new JPasswordField();
        JPasswordField txtNew = new JPasswordField();
        JPasswordField txtConfirm = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.add(new JLabel("Current Password:"));
        panel.add(txtCurrent);
        panel.add(new JLabel("New Password:"));
        panel.add(txtNew);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(txtConfirm);
        
        int result = JOptionPane.showConfirmDialog(
            SwingUtilities.getWindowAncestor(this),
            panel,
            "Change Password",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String currentStr = new String(txtCurrent.getPassword());
            String newStr = new String(txtNew.getPassword());
            String confirmStr = new String(txtConfirm.getPassword());
            
            if (currentStr.isEmpty() || newStr.isEmpty() || confirmStr.isEmpty()) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "All fields are required.", ToastNotification.ERROR);
                return;
            }
            
            if (!newStr.equals(confirmStr)) {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "New passwords do not match.", ToastNotification.ERROR);
                return;
            }
            
            User currentUser = SessionManager.getCurrentUser();
            if (BCrypt.checkpw(currentStr, currentUser.getPasswordHash())) {
                String newHash = BCrypt.hashpw(newStr, BCrypt.gensalt(12));
                UserDAOImpl userDAO = new UserDAOImpl();
                if (userDAO.updatePassword(currentUser.getId(), newHash)) {
                    // Update the session user's hash so they don't get locked out on next check
                    currentUser.setPasswordHash(newHash);
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Password updated successfully!", ToastNotification.SUCCESS);
                } else {
                    ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Failed to update password.", ToastNotification.ERROR);
                }
            } else {
                ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Incorrect current password.", ToastNotification.ERROR);
            }
        }
    }

    private RoundedPanel createSettingsCard(String title) {
        RoundedPanel card = new RoundedPanel(Theme.RADIUS_LG, Theme.getBgPanel());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.fontHeading3());
        lblTitle.setForeground(Theme.getTextPrimary());
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        
        return card;
    }
    
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.fontBody());
        l.setForeground(Theme.getTextMuted());
        return l;
    }
    
    private void styleComponent(JComponent comp) {
        comp.setFont(Theme.fontBody());
        if (comp instanceof JComboBox) {
            comp.putClientProperty(FlatClientProperties.STYLE, "margin: 6,12,6,12; arc: 8");
        }
    }
    
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
