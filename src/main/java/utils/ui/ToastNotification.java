package utils.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * A beautiful, sliding Toast Notification for the application.
 */
public class ToastNotification extends JDialog {
    private String message;
    private Color bgColor;
    private Color fgColor;
    private float opacity = 0.0f;
    private Timer fadeInTimer;
    private Timer fadeOutTimer;
    private Timer displayTimer;

    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color ERROR = new Color(231, 76, 60);
    public static final Color INFO = new Color(52, 152, 219);
    public static final Color WARNING = new Color(241, 196, 15);

    public ToastNotification(JFrame parent, String message, Color type) {
        super(parent, false);
        this.message = message;
        this.bgColor = type;
        this.fgColor = Color.WHITE;

        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        setUndecorated(true);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setBackground(new Color(0, 0, 0, 0)); // Transparent window

        // Create Panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                
                // Add a subtle drop shadow effect (simple approach)
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));
        panel.setOpaque(false);

        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(fgColor);
        panel.add(label, BorderLayout.CENTER);

        add(panel);
        pack();

        // Position at bottom-right of the parent frame
        if (parent != null) {
            int x = parent.getX() + parent.getWidth() - getWidth() - 30;
            int y = parent.getY() + parent.getHeight() - getHeight() - 50;
            setLocation(x, y);
        }

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        setupTimers();
    }

    private void setupTimers() {
        fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeInTimer.stop();
                    displayTimer.start();
                }
                setOpacity(opacity);
            }
        });

        displayTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimer.stop();
                fadeOutTimer.start();
            }
        });

        fadeOutTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    fadeOutTimer.stop();
                    dispose();
                }
                setOpacity(opacity);
            }
        });
    }

    public void showToast() {
        setOpacity(0.0f);
        setVisible(true);
        fadeInTimer.start();
    }
    
    /**
     * Static helper to easily show a toast.
     */
    public static void show(JFrame parent, String message, Color type) {
        new ToastNotification(parent, message, type).showToast();
    }
}
