import database.DatabaseManager;
import gui.RailRouteGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        DatabaseManager.initializeDatabase();
        SwingUtilities.invokeLater(() -> new RailRouteGUI());
    }
}
