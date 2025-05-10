package controller;

import util.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The BookingController class provides methods to manage booking-related operations in the database.
 * It includes functionality to create, cancel, finish, update, and retrieve bookings, as well as
 * retrieve bookings by user, status, or other criteria.
 */
public class BookingController {
    /**
     * Creates a new booking in the database and links it to a user and a car.
     *
     * @param userId The unique identifier of the user making the booking.
     * @param carId The unique identifier of the car being booked.
     * @param startDate The start date of the booking.
     * @param endDate The end date of the booking.
     * @param deposit The deposit amount for the booking.
     * @param amount The total amount for the booking.
     * @param driveOption The drive option for the booking (e.g., "self-drive", "chauffeur").
     * @param reading The odometer reading at the start of the booking.
     * @param dateOut The date the car is taken out.
     * @return true if the booking was successfully created, false otherwise.
     */
    public static boolean createBooking(int userId, int carId, String startDate, String endDate,
                                        double deposit, double amount, String driveOption,
                                        int reading, String dateOut) {
        Connection conn = null;
        PreparedStatement psBooking = null;
        PreparedStatement psMakes = null;
        PreparedStatement psReserves = null;
        ResultSet rs = null;

        try {
            conn = Srent_DB.getConnection();
            if (conn == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            String insertBooking = "INSERT INTO Booking (start_date, end_date, booking_status, " +
                    "secure_deposit, amount, drive_option, reading, date_out) VALUES (?, ?, 'confirmed', ?, ?, ?, ?, ?)";
            psBooking = conn.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS);
            psBooking.setString(1, startDate);
            psBooking.setString(2, endDate);
            psBooking.setDouble(3, deposit);
            psBooking.setDouble(4, amount);
            psBooking.setString(5, driveOption);
            psBooking.setInt(6, reading);
            psBooking.setString(7, dateOut);

            int affectedRows = psBooking.executeUpdate();
            if (affectedRows == 0) return false;

            rs = psBooking.getGeneratedKeys();
            if (rs.next()) {
                int bookingId = rs.getInt(1);

                String insertMakes = "INSERT INTO makes (user_id, booking_id) VALUES (?, ?)";
                psMakes = conn.prepareStatement(insertMakes);
                psMakes.setInt(1, userId);
                psMakes.setInt(2, bookingId);
                psMakes.executeUpdate();

                String insertReserves = "INSERT INTO reserves (booking_id, car_id) VALUES (?, ?)";
                psReserves = conn.prepareStatement(insertReserves);
                psReserves.setInt(1, bookingId);
                psReserves.setInt(2, carId);
                psReserves.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psBooking != null) psBooking.close(); } catch (Exception ignored) {}
            try { if (psMakes != null) psMakes.close(); } catch (Exception ignored) {}
            try { if (psReserves != null) psReserves.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * Cancels an existing booking by updating its status to "cancelled".
     *
     * @param bookingId The unique identifier of the booking to cancel.
     * @return true if the booking was successfully cancelled, false otherwise.
     */
    public static boolean cancelBooking(int bookingId) {
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Booking SET booking_status = 'cancelled' WHERE booking_id = ?")) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marks a booking as finished by updating its status to "finished".
     *
     * @param bookingId The unique identifier of the booking to finish.
     * @return true if the booking was successfully marked as finished, false otherwise.
     */
    public static boolean finishBooking(int bookingId) {
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Booking SET booking_status = 'finished' WHERE booking_id = ?")) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of bookings made by a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of strings representing the bookings made by the user.
     */
    public static List<String> getBookingsByUser(int userId) {
        List<String> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.start_date, b.end_date, b.booking_status, b.amount, c.car_id, c.model " +
                "FROM Booking b " +
                "JOIN makes m ON b.booking_id = m.booking_id " +
                "JOIN reserves r ON b.booking_id = r.booking_id " +
                "JOIN Car c ON r.car_id = c.car_id " +
                "WHERE m.user_id = ?";

        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String entry = String.format("Booking #%d: Car ID %d (%s), %s to %s - %s [$%.2f]",
                        rs.getInt("booking_id"),
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("booking_status"),
                        rs.getDouble("amount"));
                bookings.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    /**
     * Retrieves a list of bookings filtered by their status.
     *
     * @param status The status to filter bookings by (e.g., "confirmed", "cancelled").
     * @return A list of strings representing the bookings with the specified status.
     */
    public static List<String> getBookingsByStatus(String status) {
        List<String> bookings = new ArrayList<>();
        String sql = "SELECT booking_id, start_date, end_date, amount FROM Booking WHERE booking_status = ?";

        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String entry = String.format("Booking #%d: %s to %s [$%.2f]",
                        rs.getInt("booking_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("amount"));
                bookings.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    /**
     * Retrieves detailed information about a booking by its unique identifier.
     *
     * @param bookingId The unique identifier of the booking.
     * @return A string representing the booking details, or "Booking not found." if no booking is found.
     */
    public static String getBookingById(int bookingId) {
        String sql = "SELECT b.booking_id, b.start_date, b.end_date, b.booking_status, b.amount, b.drive_option, c.model " +
                "FROM Booking b " +
                "JOIN reserves r ON b.booking_id = r.booking_id " +
                "JOIN Car c ON r.car_id = c.car_id " +
                "WHERE b.booking_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("Booking #%d: %s to %s | Car: %s | Status: %s | Amount: %.2f | Drive: %s",
                        rs.getInt("booking_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("model"),
                        rs.getString("booking_status"),
                        rs.getDouble("amount"),
                        rs.getString("drive_option"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Booking not found.";
    }

    /**
     * Updates the details of an existing booking in the database.
     *
     * @param bookingId The unique identifier of the booking to update.
     * @param startDate The updated start date of the booking.
     * @param endDate The updated end date of the booking.
     * @param amount The updated total amount for the booking.
     * @param driveOption The updated drive option for the booking.
     * @param reading The updated odometer reading for the booking.
     * @return true if the booking was successfully updated, false otherwise.
     */
    public static boolean updateBooking(int bookingId, String startDate, String endDate, double amount, String driveOption, int reading) {
        String sql = "UPDATE Booking SET start_date = ?, end_date = ?, amount = ?, drive_option = ?, reading = ? WHERE booking_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setDouble(3, amount);
            ps.setString(4, driveOption);
            ps.setInt(5, reading);
            ps.setInt(6, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of active bookings that are confirmed and have not yet ended.
     *
     * @return A list of strings representing active bookings.
     */
    public static List<String> getActiveBookings() {
        List<String> bookings = new ArrayList<>();
        String sql = "SELECT booking_id, start_date, end_date, booking_status FROM Booking WHERE booking_status = 'confirmed' AND end_date >= CURRENT_DATE";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String booking = String.format("Active Booking #%d: %s to %s | Status: %s",
                        rs.getInt("booking_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("booking_status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }



}
