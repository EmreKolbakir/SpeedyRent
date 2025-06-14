package controller;

import util.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingController {

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

    public static boolean cancelBooking(int bookingId) {
        Connection conn = null;
        try {
            conn = Srent_DB.getConnection();
            conn.setAutoCommit(false);

            // 1. Rezervasyon durumunu iptal et
            String cancelSQL = "UPDATE Booking SET booking_status = 'cancelled' WHERE booking_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(cancelSQL)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            // 2. Rezervasyonla ilişkili araba ID'sini bul
            int carId = -1;
            String carQuery = "SELECT car_id FROM reserves WHERE booking_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(carQuery)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    carId = rs.getInt("car_id");
                }
            }

            // 3. Arabayı available yap
            if (carId != -1) {
                String updateCar = "UPDATE Car SET vehicle_status = 'available' WHERE car_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateCar)) {
                    ps.setInt(1, carId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

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

    public static List<String> getActiveBookingsByUser(int userId) {
        List<String> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.start_date, b.end_date, b.booking_status, b.amount, c.car_id, c.model " +
                "FROM Booking b " +
                "JOIN makes m ON b.booking_id = m.booking_id " +
                "JOIN reserves r ON b.booking_id = r.booking_id " +
                "JOIN Car c ON r.car_id = c.car_id " +
                "WHERE m.user_id = ? AND b.booking_status = 'confirmed'";

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

    public static List<String> getAllBookings() {
        List<String> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.start_date, b.end_date, b.booking_status, b.amount, b.drive_option, " +
                "u.username, c.model " +
                "FROM Booking b " +
                "JOIN makes m ON b.booking_id = m.booking_id " +
                "JOIN user u ON m.user_id = u.user_id " +
                "JOIN reserves r ON b.booking_id = r.booking_id " +
                "JOIN car c ON r.car_id = c.car_id";

        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String booking = String.format("Booking #%d | User: %s | Car: %s\nStart: %s | End: %s | Status: %s | Drive: %s | Amount: %.2f",
                        rs.getInt("booking_id"),
                        rs.getString("username"),
                        rs.getString("model"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("booking_status"),
                        rs.getString("drive_option"),
                        rs.getDouble("amount"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

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

    public static boolean updateBookingDates(int bookingId, String startDate, String endDate) {
        String sql = "UPDATE Booking SET start_date = ?, end_date = ? WHERE booking_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setInt(3, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Start/end tarihlerine göre amount'u yeniden hesaplayıp günceller.
     */
    public static boolean updateBookingDatesAndAmount(int bookingId, String startDate, String endDate) {
        Connection conn = null;
        try {
            conn = Srent_DB.getConnection();
            conn.setAutoCommit(false);

            // 1) İlgili Booking için car_id ve günlük kira fiyatını bul
            String rentQuery =
                    "SELECT c.daily_rent " +
                            "FROM reserves r " +
                            "JOIN Car c ON r.car_id = c.car_id " +
                            "WHERE r.booking_id = ?";
            double dailyRent;
            try (PreparedStatement ps = conn.prepareStatement(rentQuery)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return false;
                dailyRent = rs.getDouble("daily_rent");
            }

            // 2) Yeni tarihleri güncelle ve amount'u hesapla
            LocalDate s = LocalDate.parse(startDate);
            LocalDate e = LocalDate.parse(endDate);
            long days = ChronoUnit.DAYS.between(s, e) + 1;
            if (days < 1) days = 1;
            double newAmount = dailyRent * days;

            String updateSQL =
                    "UPDATE Booking " +
                            "SET start_date = ?, end_date = ?, amount = ? " +
                            "WHERE booking_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                ps.setDouble(3, newAmount);
                ps.setInt(4, bookingId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

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
