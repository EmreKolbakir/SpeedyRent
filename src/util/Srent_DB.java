package util;

import java.sql.*;
import java.util.*;

/**
 * The Srent_DB class provides utility methods for interacting with the database.
 * It includes methods for establishing a connection, authenticating users,
 * reserving cars, and canceling reservations.
 */
public class Srent_DB {
    private static String userType; // The type of user (e.g., admin, customer).
    private static Connection conn; // The database connection object.
    private static PreparedStatement ps; // The prepared statement for executing SQL queries.
    private static Scanner scanner; // Scanner for user input (not used in the current implementation).
    private static String HOST_NAME = "127.0.0.1"; // The hostname of the database server.
    private static final String PORT = "3306"; // The default port for MySQL.
    private static final String USER_NAME = "root"; // The username for database authentication.
    private static final String PASSWORD = "Wthrw_<>1215@"; // The password for database authentication.
    private static final String DB_NAME = "srent"; // The name of the database.

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object if successful, or null if an error occurs.
     */
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME; // Database URL.
            Connection connection = DriverManager.getConnection(url, USER_NAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the name of the database.
     *
     * @return The name of the database as a String.
     */
    public static String getDBName() {
        return DB_NAME;
    }

    /**
     * Authenticates a user by checking if the provided user ID and password exist in the database.
     *
     * @param userID   The ID of the user.
     * @param password The password of the user.
     * @return True if the user is found, false otherwise.
     */
    public static boolean authenticateUser(int userID, String password) {
        try {
            boolean isFound;
            conn = getConnection();
            String sql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            conn.close();

            isFound = rs.next();
            System.out.println(isFound ? "User is found" : "User is not found");
            return isFound;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred! \n");
            return false;
        }
    }

    /**
     * Reserves a car for a user by inserting a record into the reserves table.
     *
     * @param userID The ID of the user.
     * @param carID  The ID of the car to be reserved.
     * @return True if the reservation is successful, false otherwise.
     */
    public static boolean reserveCar(int userID, int carID) {
        try {
            conn = getConnection();
            String sql = "INSERT INTO reserves VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, carID);
            ResultSet rs = ps.executeQuery();
            conn.close();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred! \n");
            return false;
        }
    }

    /**
     * Cancels a reservation by deleting a record from the reservations table.
     *
     * @param bookingID The ID of the booking to be canceled.
     * @return True if the cancellation is successful, false otherwise.
     */
    public static boolean cancelReservation(int bookingID) {
        try {
            conn = getConnection();
            String sql = "DELETE FROM reservations WHERE booking_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            conn.close();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred! \n");
            return false;
        }
    }
}