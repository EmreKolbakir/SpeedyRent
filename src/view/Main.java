package view;

import javax.swing.*;
import java.awt.*;

/**
 * The Main class serves as the entry point for the SpeedyRent application.
 * It initializes the main JFrame, sets up the CardLayout for navigation,
 * and adds the various panels (LoginPanel, CarListPanel, AdminPanel) to the container.
 */
public class Main {
    /**
     * The main method initializes the application and sets up the GUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Set the look and feel of the application to match the system's appearance.
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        // Create and display the GUI on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame.
            JFrame frame = new JFrame("SpeedyRent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Create a CardLayout for navigating between panels.
            CardLayout cardLayout = new CardLayout();
            JPanel container  = new JPanel(cardLayout);


            // === Create panels ===
            LoginPanel    loginPanel   = new LoginPanel(cardLayout, container);
            CarListPanel  carListPanel = new CarListPanel(cardLayout, container);
            AdminPanel    adminPanel   = new AdminPanel(cardLayout, container);

            // === Add panels to the card container ===
            container.add(loginPanel,   "login");
            container.add(carListPanel, "carlist");

            // === Panellerin oluşturulması ===
            LoginPanel loginPanel = new LoginPanel(cardLayout, container);
            AdminPanel adminPanel = new AdminPanel(cardLayout, container);

            // === Panelleri karta ekle ===
            container.add(loginPanel, "login");

            container.add(adminPanel,   "admin");

            // Set the container as the content pane of the frame.
            frame.setContentPane(container);
            frame.setVisible(true);

            // Show the login screen as the initial panel.
            cardLayout.show(container, "login");
        });
    }
}