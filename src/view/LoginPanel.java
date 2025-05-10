package view;

import controller.AuthenticationController;
import controller.AuthenticationController.UserRole;
import java.awt.*;
import javax.swing.*;

/**
 * The LoginPanel class represents the login screen for the application.
 * It allows users to log in using their User ID and name.
 * Depending on the user's role, it navigates to the appropriate panel.
 */
public class LoginPanel extends JPanel {
    private final CardLayout cardLayout; // The CardLayout used for navigation between panels.
    private final JPanel container; // The parent container holding this panel.
    private final JTextField userIdField; // Text field for entering the User ID.
    private final JTextField nameField; // Text field for entering the user's name.

    /**
     * Constructs the LoginPanel with the specified CardLayout and container.
     *
     * @param cardLayout The CardLayout used for navigation.
     * @param container  The parent container for the panel.
     */
    public LoginPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add the title label to the panel.
        JLabel title = new JLabel("SpeedyRent Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Add the User ID label and text field.
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("User ID:"), gbc);
        userIdField = new JTextField();
        gbc.gridx = 1;
        add(userIdField, gbc);

        // Add the Name label and text field.
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Name:"), gbc);
        nameField = new JTextField();
        gbc.gridx = 1;
        add(nameField, gbc);

        // Add the Login button.
        JButton loginButton = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // Add an action listener to handle the login process.
        loginButton.addActionListener(e -> doLogin());
    }

    /**
     * Handles the login process when the Login button is clicked.
     * Validates the input fields and checks the user's credentials.
     * Navigates to the appropriate panel based on the user's role.
     */
    private void doLogin() {
        String idText = userIdField.getText().trim();
        String name = nameField.getText().trim();
        int userId;
        try {
            // Parse the User ID as an integer.
            userId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            // Show an error message if the User ID is not a valid number.
            JOptionPane.showMessageDialog(this,
                    "User ID must be a valid number.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate the user and retrieve their role.
        UserRole role = AuthenticationController.login(userId, name);
        if (role == UserRole.UNKNOWN) {
            // Show an error message if login fails.
            JOptionPane.showMessageDialog(this,
                    "Login failed. Please check your credentials.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            // Show a success message and navigate to the appropriate panel.
            JOptionPane.showMessageDialog(this,
                    "Login successful: " + role,
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);

            if (role == UserRole.ADMIN) {
                cardLayout.show(container, "admin");
            } else if (role == UserRole.CUSTOMER) {
                // 1) Kullanıcı ID’sini CarListPanel’e geçiriyoruz
                CarListPanel clp = new CarListPanel(cardLayout, container, userId);
                container.add(clp, "carlist");
                cardLayout.show(container, "carlist");
            }
        }
    }
}