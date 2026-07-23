import view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point.
 */
public class Main {
    public static void main(String[] args) {
        // Set modern FlatLaf theme
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run GUI on the Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}
