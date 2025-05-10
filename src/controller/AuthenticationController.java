package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.Srent_DB;

/**
 * Retrieves a list of active bookings that are confirmed and have not yet ended.
 *
 * @return A list of strings representing active bookings.
 */
public class AuthenticationController {
    /**
     * Enum representing user roles.
     */
    public enum UserRole {
        ADMIN,
        CUSTOMER,
        UNKNOWN
    }

    /**
     * Logs in a user by verifying their user ID and username.
     *
     * @param userId The unique identifier of the user.
     * @param username The username of the user.
     * @return The role of the user (ADMIN, CUSTOMER, or UNKNOWN).
     */
    public static UserRole login(int userId, String username) {
        String sql = "SELECT * FROM User WHERE user_id = ? AND username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Srent_DB.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null.");
                return UserRole.UNKNOWN;
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                try {
                    if (isAdmin(userId, conn)) {
                        System.out.println("Admin login successful.");
                        return UserRole.ADMIN;
                    } else if (isCustomer(userId, conn)) {
                        System.out.println("Customer login successful.");
                        return UserRole.CUSTOMER;
                    } else {
                        System.out.println("User exists but has no role.");
                        return UserRole.UNKNOWN;
                    }
                } catch (Exception roleEx) {
                    System.err.println("Error while checking role: " + roleEx.getMessage());
                    roleEx.printStackTrace();
                    return UserRole.UNKNOWN;
                }
            } else {
                System.out.println("Login failed: invalid user ID or username.");
                return UserRole.UNKNOWN;
            }

        } catch (SQLException e) {
            System.err.println("SQL error during login: " + e.getMessage());
            e.printStackTrace();
            return UserRole.UNKNOWN;

        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            return UserRole.UNKNOWN;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }


    /**
     * Checks if a user is an admin.
     *
     * @param userId The unique identifier of the user.
     * @param conn The database connection to use.
     * @return true if the user is an admin, false otherwise.
     */
    private static boolean isAdmin(int userId, Connection conn) {
        String query = "SELECT * FROM Admin WHERE user_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("SQL error in isAdmin(): " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.err.println("General error in isAdmin(): " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println("Failed to close ResultSet in isAdmin(): " + e.getMessage()); }
            try { if (ps != null) ps.close(); } catch (Exception e) { System.err.println("Failed to close PreparedStatement in isAdmin(): " + e.getMessage()); }
        }
    }

    /**
     * Checks if a user is a customer.
     *
     * @param userId The unique identifier of the user.
     * @param conn The database connection to use.
     * @return true if the user is a customer, false otherwise.
     */
    private static boolean isCustomer(int userId, Connection conn) {
        String query = "SELECT * FROM Customer WHERE user_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("SQL error in isCustomer(): " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.err.println("General error in isCustomer(): " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println("Failed to close ResultSet in isCustomer(): " + e.getMessage()); }
            try { if (ps != null) ps.close(); } catch (Exception e) { System.err.println("Failed to close PreparedStatement in isCustomer(): " + e.getMessage()); }
        }
    }

    /**
     * Registers a new customer in the database.
     *
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @param username The username of the customer.
     * @param email The email address of the customer.
     * @param gender The gender of the customer.
     * @param address The address of the customer.
     * @param occupation The occupation of the customer.
     * @return true if the customer was successfully registered, false otherwise.
     */
    public static boolean registerCustomer(String firstName, String lastName, String username, String email, String gender, String address, String occupation) {
        Connection conn = null;
        PreparedStatement checkEmailStmt = null;
        PreparedStatement insertUserStmt = null;
        PreparedStatement getIdStmt = null;
        PreparedStatement insertCustomerStmt = null;
        ResultSet rs = null;

        try {
            conn = Srent_DB.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null.");
                return false;
            }

            String checkEmailSql = "SELECT * FROM User WHERE email = ?";
            checkEmailStmt = conn.prepareStatement(checkEmailSql);
            checkEmailStmt.setString(1, email);
            rs = checkEmailStmt.executeQuery();

            if (rs.next()) {
                System.out.println("This email is already registered.");
                return false;
            }
            rs.close();

            String insertUserSql = "INSERT INTO User (first_name, last_name, username, email, gender, address) VALUES (?, ?, ?, ?, ?, ?)";
            insertUserStmt = conn.prepareStatement(insertUserSql);
            insertUserStmt.setString(1, firstName);
            insertUserStmt.setString(2, lastName);
            insertUserStmt.setString(3, username);
            insertUserStmt.setString(4, email);
            insertUserStmt.setString(5, gender);
            insertUserStmt.setString(6, address);
            insertUserStmt.executeUpdate();

            String getIdSql = "SELECT LAST_INSERT_ID() AS last_id";
            getIdStmt = conn.prepareStatement(getIdSql);
            rs = getIdStmt.executeQuery();

            int userId;
            if (rs.next()) {
                userId = rs.getInt("last_id");
            } else {
                System.err.println("Failed to retrieve last inserted user_id.");
                return false;
            }
            rs.close();

            String insertCustomerSql = "INSERT INTO Customer (user_id, occupation) VALUES (?, ?)";
            insertCustomerStmt = conn.prepareStatement(insertCustomerSql);
            insertCustomerStmt.setInt(1, userId);
            insertCustomerStmt.setString(2, occupation);
            insertCustomerStmt.executeUpdate();

            System.out.println("Customer registered successfully.");
            return true;

        } catch (SQLException e) {
            System.err.println("SQL error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.err.println("Unexpected error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (checkEmailStmt != null) checkEmailStmt.close(); } catch (Exception e) { }
            try { if (insertUserStmt != null) insertUserStmt.close(); } catch (Exception e) { }
            try { if (getIdStmt != null) getIdStmt.close(); } catch (Exception e) { }
            try { if (insertCustomerStmt != null) insertCustomerStmt.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    /**
     * Registers a new admin in the database.
     *
     * @param firstName The first name of the admin.
     * @param lastName The last name of the admin.
     * @param username The username of the admin.
     * @param gender The gender of the admin.
     * @param email The email address of the admin.
     * @param address The address of the admin.
     * @param salary The salary of the admin.
     * @return true if the admin was successfully registered, false otherwise.
     */
    public static boolean registerAdmin(String firstName, String lastName, String username, String gender, String email, String address, double salary) {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psAdmin = null;
        ResultSet rs = null;
        try {
            conn = Srent_DB.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            String checkSql = "SELECT * FROM User WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("This email is already registered.");
                    return false;
                }
            }

            String insertUser = "INSERT INTO User (first_name, last_name, username, gender, email, address) VALUES (?, ?, ?, ?, ?, ?)";
            psUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, firstName);
            psUser.setString(2, lastName);
            psUser.setString(3, username);
            psUser.setString(4, gender);
            psUser.setString(5, email);
            psUser.setString(6, address);
            psUser.executeUpdate();

            rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                String insertAdmin = "INSERT INTO Admin (user_id, salary) VALUES (?, ?)";
                psAdmin = conn.prepareStatement(insertAdmin);
                psAdmin.setInt(1, userId);
                psAdmin.setDouble(2, salary);
                psAdmin.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psUser != null) psUser.close(); } catch (Exception ignored) {}
            try { if (psAdmin != null) psAdmin.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }


}
