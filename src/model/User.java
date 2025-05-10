package model;

/**
 * The User class represents a user in the system with a username and password.
 * It provides methods to retrieve the username and password.
 */
public class User {
    private final String username; // The username of the user.
    private final String password; // The password of the user.

    /**
     * Constructs a User object with the specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }
}