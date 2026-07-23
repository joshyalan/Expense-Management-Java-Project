package utils.ui;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that draws a rounded rectangle background.
 * Perfect for creating modern, card-like UIs and glassmorphism effects.
 */
public class RoundedPanel extends JPanel {
    private int cornerRadius = 15;
    private Color backgroundColor;

    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false); // Make the standard background transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        
        // Anti-aliasing for smooth corners
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the rounded rectangle
        if (backgroundColor != null) {
            graphics.setColor(backgroundColor);
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
}
