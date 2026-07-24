import view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point.
 */
public class Main {
    public static void main(String[] args) {
        // Enable custom window decorations before Laf setup
        System.setProperty("flatlaf.useWindowDecorations", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "false");
        
        // Set modern FlatLaf theme
        try {
            UIManager.put("TitlePane.buttonStyle", "mac");
            UIManager.put("TitlePane.showIcon", false);
            UIManager.put("TitlePane.centerTitle", true);
            UIManager.put("TitlePane.unifiedBackground", true);
            
            // Extreme Rounded Corners
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);

            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.trackInsets", new java.awt.Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", new java.awt.Color(0,0,0,0)); // Transparent track
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ensure critical database tables exist
        utils.DatabaseInitializer.initialize();

        // Run GUI on the Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}
