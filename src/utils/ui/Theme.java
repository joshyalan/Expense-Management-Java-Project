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
    public static final Color PRIMARY = new Color(79, 70, 229);      // Indigo 600
    public static final Color PRIMARY_DARK = new Color(67, 56, 202); // Indigo 700
    public static final Color SUCCESS = new Color(16, 185, 129);     // Emerald 500
    public static final Color DANGER = new Color(239, 68, 68);       // Red 500
    public static final Color WARNING = new Color(245, 158, 11);     // Amber 500
    
    public static Color getBgApp() {
        return isDarkMode ? new Color(15, 23, 42) : new Color(249, 250, 251); // Slate 900 vs Gray 50
    }
    
    public static Color getBgPanel() {
        return isDarkMode ? new Color(30, 41, 59) : new Color(255, 255, 255); // Slate 800 vs White
    }
    
    public static Color getTextPrimary() {
        return isDarkMode ? new Color(248, 250, 252) : new Color(17, 24, 39); // Slate 50 vs Gray 900
    }
    
    public static Color getTextMuted() {
        return isDarkMode ? new Color(148, 163, 184) : new Color(107, 114, 128); // Slate 400 vs Gray 500
    }
    
    public static Color getBorder() {
        return isDarkMode ? new Color(51, 65, 85) : new Color(229, 231, 235); // Slate 700 vs Gray 200
    }
    
    public static Color getSidebarBg() {
        return isDarkMode ? new Color(9, 14, 23) : new Color(30, 30, 47);
    }
    
    public static Color getSidebarBtnHover() {
        return isDarkMode ? new Color(30, 41, 59) : new Color(50, 50, 75);
    }

    // --- TYPOGRAPHY ---
    public static final String FONT_FAMILY = "Inter"; 
    
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
}
