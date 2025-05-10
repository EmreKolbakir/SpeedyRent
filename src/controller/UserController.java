package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import util.Srent_DB;

/**
 * The UserController class provides methods to manage user-related operations in the database.
 * It includes functionality to retrieve user roles, check email registration, search users,
 * and retrieve detailed user information.
 */
public class UserController {

    /**
     * Retrieves the role of a user based on their user ID.
     *
     * @param userId The unique identifier of the user.
     * @return The role of the user ("admin", "customer", or "unknown").
     */
    public static String getUserRole(int userId) {
        String sqlAdmin = "SELECT user_id FROM Admin WHERE user_id = ?";
        String sqlCustomer = "SELECT user_id FROM Customer WHERE user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin)) {
            psAdmin.setInt(1, userId);
            try (ResultSet rsAdmin = psAdmin.executeQuery()) {
                if (rsAdmin.next()) {
                    return "admin";
                }
            }
            try (PreparedStatement psCust = conn.prepareStatement(sqlCustomer)) {
                psCust.setInt(1, userId);
                try (ResultSet rsCust = psCust.executeQuery()) {
                    if (rsCust.next()) {
                        return "customer";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * Checks if an email is already registered in the system.
     *
     * @param email The email address to check.
     * @return true if the email is registered, false otherwise.
     */
    public static boolean isEmailRegistered(String email) {
        String sql = "SELECT 1 FROM User WHERE email = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of users based on their type (admin or customer).
     *
     * @param type The type of users to retrieve ("admin" or "customer").
     * @return A list of strings representing the users of the specified type.
     */
    public static List<String> getUsersByType(String type) {
        List<String> users = new ArrayList<>();
        String joinQuery = "SELECT u.user_id, u.name, u.email FROM User u JOIN %s t ON u.user_id = t.user_id";
        String sql = type.equalsIgnoreCase("admin")
                ? String.format(joinQuery, "Admin")
                : String.format(joinQuery, "Customer");
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String user = String.format("%s ID %d: %s | %s",
                        type.toUpperCase(),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Searches for users by their name using a keyword.
     *
     * @param keyword The keyword to search for in user names.
     * @return A list of strings representing the users whose names match the keyword.
     */
    public static List<String> searchUsersByName(String keyword) {
        List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE name LIKE ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String user = String.format("User ID %d: %s | Email: %s",
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retrieves the most recently registered users, limited by the specified number.
     *
     * @param limit The maximum number of users to retrieve.
     * @return A list of strings representing the latest registered users.
     */
    public static List<String> getLatestUsers(int limit) {
        List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM User ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String user = String.format("User ID %d: %s | Email: %s | Registered: %s",
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retrieves the email address of a user based on their user ID.
     *
     * @param userId The unique identifier of the user.
     * @return The email address of the user, or null if not found.
     */
    public static String getUserEmail(int userId) {
        String sql = "SELECT email FROM User WHERE user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves detailed information about a user, including their role, based on their user ID.
     *
     * @param userId The unique identifier of the user.
     * @return A string representing the user's full information, or "User not found." if no user is found.
     */
    public static String getFullUserInfoWithRole(int userId) {
        String sql = "SELECT u.user_id, u.name, u.email, u.gender, u.address, a.salary, c.occupation " +
                "FROM User u " +
                "LEFT JOIN Admin a ON u.user_id = a.user_id " +
                "LEFT JOIN Customer c ON u.user_id = c.user_id " +
                "WHERE u.user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("salary") != null ? "Admin"
                            : rs.getString("occupation") != null ? "Customer" : "Unknown";
                    return String.format("User ID: %d | Name: %s | Email: %s | Gender: %s | Address: %s | Role: %s",
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getString("address"),
                            role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User not found.";
    }

    /**
     * Retrieves a list of users along with the count of cards associated with each user.
     *
     * @return A list of strings representing users and their card counts.
     */
    public static List<String> getUsersWithCardCount() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT u.user_id, u.name, COUNT(b.card_id) AS card_count " +
                "FROM User u LEFT JOIN brings b ON u.user_id = b.user_id " +
                "GROUP BY u.user_id, u.name ORDER BY card_count DESC";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String entry = String.format("User ID %d: %s | Cards: %d",
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("card_count"));
                result.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}