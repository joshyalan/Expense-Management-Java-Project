package view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import utils.SessionManager;
import utils.ui.RoundedPanel;
import utils.ui.ToastNotification;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(244, 247, 252));
        setBorder(new EmptyBorder(40, 50, 40, 50));

        initComponents();
    }

    private void initComponents() {
        // Title
        JLabel title = new JLabel("Application Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 30, 47));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Center Area (Grid of Settings Cards)
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        centerPanel.setOpaque(false);

        // 1. Theme Settings
        RoundedPanel themeCard = createSettingsCard("Appearance");
        themeCard.add(new JLabel("Select your preferred theme:"));
        themeCard.add(Box.createRigidArea(new Dimension(0, 10)));
        JComboBox<String> cbTheme = new JComboBox<>(new String[]{"Light Mode", "Dark Mode"});
        cbTheme.addActionListener(e -> {
            if (cbTheme.getSelectedIndex() == 0) {
                applyTheme(new FlatLightLaf());
            } else {
                applyTheme(new FlatDarkLaf());
            }
        });
        themeCard.add(cbTheme);
        centerPanel.add(themeCard);

        // 2. Database Backup
        RoundedPanel backupCard = createSettingsCard("Database Backup");
        backupCard.add(new JLabel("Backup your entire financial data safely."));
        backupCard.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btnBackup = new JButton("Export Database SQL");
        btnBackup.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #3498db; foreground: #fff");
        btnBackup.addActionListener(e -> ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Backup feature requires mysqldump installed on path.", ToastNotification.INFO));
        backupCard.add(btnBackup);
        centerPanel.add(backupCard);

        // 3. User Preferences
        RoundedPanel prefCard = createSettingsCard("Preferences");
        prefCard.add(new JLabel("Default Currency:"));
        JComboBox<String> cbCurrency = new JComboBox<>(new String[]{"USD ($)", "EUR (€)", "GBP (£)", "INR (₹)"});
        prefCard.add(cbCurrency);
        prefCard.add(Box.createRigidArea(new Dimension(0, 10)));
        JCheckBox chkEmail = new JCheckBox("Receive Email Notifications");
        chkEmail.setSelected(true);
        prefCard.add(chkEmail);
        centerPanel.add(prefCard);

        add(centerPanel, BorderLayout.CENTER);
    }

    private RoundedPanel createSettingsCard(String title) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return card;
    }
    
    private void applyTheme(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
            ToastNotification.show((JFrame) SwingUtilities.getWindowAncestor(this), "Theme applied!", ToastNotification.SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
