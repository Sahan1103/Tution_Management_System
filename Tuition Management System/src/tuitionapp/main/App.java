package tuitionapp.main;
import tuitionapp.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import tuitionapp.ui.LoginFrame;
/**
 * App - Main entry point for the Tuition Management System
 *
 * This class starts the application by creating and displaying the MainFrame.
 */
public class App {
    /**
     * Main method - Application entry point
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Print startup message
        System.out.println("========================================");
        System.out.println("  Tuition Management System");
        System.out.println("  Starting application...");
        System.out.println("========================================");

        // Set Look and Feel to match the operating system
        setSystemLookAndFeel();

        // Run the GUI on the Event Dispatch Thread (EDT)
        // This is the proper way to start Swing applications
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the main frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

                System.out.println("Application started successfully!");
                System.out.println("Main window is now visible.");

            } catch (Exception e) {
                System.err.println("Error starting application:");
                e.printStackTrace();

                // Show error dialog to user
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Failed to start application:\n" + e.getMessage(),
                        "Application Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );

                // Exit with error code
                System.exit(1);
            }
        });
    }

    /**
     * Set the system's native Look and Feel for better integration
     * Falls back to cross-platform Look and Feel if system L&F is not available
     */
    private static void setSystemLookAndFeel() {
        try {
            // Try to use the system's native Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Look and Feel: " + UIManager.getLookAndFeel().getName());

        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {

            System.out.println("Could not set system Look and Feel, using default.");

            // Try to set Nimbus Look and Feel as fallback (modern looking)
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        System.out.println("Using Nimbus Look and Feel");
                        return;
                    }
                }
            } catch (Exception ex) {
                System.out.println("Using default Look and Feel");
            }
        }
    }
}
