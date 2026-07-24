package utils.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

/**
 * Design System constants for Expense Tracker Pro.
 * Centralized theme colors, typography, and spacing for a premium look.
 */
public class Theme {
    
    public static boolean isDarkMode = false;

    public static void toggleTheme(JFrame currentFrame) {
        isDarkMode = !isDarkMode;
        try {
            UIManager.put("TitlePane.buttonStyle", "mac");
            UIManager.put("TitlePane.showIcon", false);
            UIManager.put("TitlePane.centerTitle", true);
            UIManager.put("TitlePane.unifiedBackground", true);
            
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.trackInsets", new java.awt.Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", new Color(0,0,0,0));
            
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(currentFrame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --- COLORS ---
    public static final Color PRIMARY = new Color(99, 91, 255);      // Stripe Blurple
    public static final Color PRIMARY_DARK = new Color(74, 67, 208); 
    public static final Color SUCCESS = new Color(16, 185, 129);     
    public static final Color DANGER = new Color(239, 68, 68);       
    public static final Color WARNING = new Color(245, 158, 11);     
    
    public static Color getBgApp() {
        return isDarkMode ? new Color(15, 23, 42) : new Color(246, 249, 252); // Stripe very light gray-blue
    }
    
    public static Color getBgPanel() {
        return isDarkMode ? new Color(30, 41, 59) : new Color(255, 255, 255); 
    }
    
    public static Color getTextPrimary() {
        return isDarkMode ? new Color(248, 250, 252) : new Color(10, 37, 64); // Stripe dark blue text
    }
    
    public static Color getTextMuted() {
        return isDarkMode ? new Color(148, 163, 184) : new Color(66, 84, 102); // Stripe muted text
    }
    
    public static Color getBorder() {
        return isDarkMode ? new Color(51, 65, 85) : new Color(229, 232, 235);
    }
    
    public static Color getSidebarBg() {
        return isDarkMode ? new Color(9, 14, 23) : new Color(10, 37, 64); // Stripe dark sidebar
    }
    
    public static Color getSidebarBtnHover() {
        return isDarkMode ? new Color(30, 41, 59) : new Color(29, 57, 88); 
    }

    // --- TYPOGRAPHY ---
    public static final String FONT_FAMILY = "Segoe UI"; 
    
    public static Font fontHero() { return new Font(FONT_FAMILY, Font.BOLD, 32); }
    public static Font fontHeading1() { return new Font(FONT_FAMILY, Font.BOLD, 28); }
    public static Font fontHeading2() { return new Font(FONT_FAMILY, Font.BOLD, 22); }
    public static Font fontHeading3() { return new Font(FONT_FAMILY, Font.BOLD, 18); }
    public static Font fontBodyBold() { return new Font(FONT_FAMILY, Font.BOLD, 14); }
    public static Font fontBody() { return new Font(FONT_FAMILY, Font.PLAIN, 14); }
    public static Font fontSmall() { return new Font(FONT_FAMILY, Font.PLAIN, 12); }
    
    // --- SPACING ---
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;
    public static final int SPACING_XXL = 48;
    
    // --- BORDER RADIUS ---
    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 12;
    public static final int RADIUS_LG = 16;
    
    // --- UTILITIES ---
    public static String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
