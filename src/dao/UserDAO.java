package dao;

import model.User;

/**
 * The UserDAO interface defines the operations for accessing and managing user data in the system.
 * It provides methods to find a user by username and to create a new user.
 */
public interface UserDAO {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return The User object corresponding to the given username, or null if no user is found.
     */
    User findByUsername(String username);

    /**
     * Creates a new user in the system.
     *
     * @param user The User object containing the details of the user to be created.
     * @return true if the user was successfully created, false otherwise.
     */
    boolean createUser(User user);
}