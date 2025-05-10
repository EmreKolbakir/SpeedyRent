package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.Srent_DB;

/**
 * The VehicleSpecificationController class provides methods to manage vehicle specifications
 * in the database, including adding, updating, deleting, and retrieving specifications.
 */
public class VehicleSpecificationController {

    /**
     * Adds a new vehicle specification to the database.
     *
     * @param color The color of the vehicle.
     * @param fuelType The type of fuel used by the vehicle.
     * @param transmissionType The type of transmission used by the vehicle.
     * @param seatingCapacity The seating capacity of the vehicle.
     * @return true if the specification was successfully added, false otherwise.
     */
    public static boolean addSpecification(String color, String fuelType, String transmissionType, int seatingCapacity) {
        String sql = "INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity) VALUES (?, ?, ?, ?)";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, color);
            ps.setString(2, fuelType);
            ps.setString(3, transmissionType);
            ps.setInt(4, seatingCapacity);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing vehicle specification in the database.
     *
     * @param specId The unique identifier of the specification to update.
     * @param color The updated color of the vehicle.
     * @param fuelType The updated type of fuel used by the vehicle.
     * @param transmissionType The updated type of transmission used by the vehicle.
     * @param seatingCapacity The updated seating capacity of the vehicle.
     * @return true if the specification was successfully updated, false otherwise.
     */
    public static boolean updateSpecification(int specId, String color, String fuelType, String transmissionType, int seatingCapacity) {
        String sql = "UPDATE VehicleSpecification SET color = ?, fuel_type = ?, transmission_type = ?, seating_capacity = ? WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, color);
            ps.setString(2, fuelType);
            ps.setString(3, transmissionType);
            ps.setInt(4, seatingCapacity);
            ps.setInt(5, specId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a vehicle specification from the database.
     *
     * @param specId The unique identifier of the specification to delete.
     * @return true if the specification was successfully deleted, false otherwise.
     */
    public static boolean deleteSpecification(int specId) {
        String sql = "DELETE FROM VehicleSpecification WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, specId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of all vehicle specifications from the database.
     *
     * @return A list of strings representing all vehicle specifications.
     */
    public static List<String> getAllSpecifications() {
        List<String> specs = new ArrayList<>();
        String sql = "SELECT * FROM VehicleSpecification";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String spec = String.format("Spec ID %d: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getInt("specification_id"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
                specs.add(spec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specs;
    }

    /**
     * Retrieves a specific vehicle specification by its unique identifier.
     *
     * @param specId The unique identifier of the specification to retrieve.
     * @return A string representing the vehicle specification, or "Specification not found." if no specification is found.
     */
    public static String getSpecificationById(int specId) {
        String sql = "SELECT * FROM VehicleSpecification WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, specId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("Color: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Specification not found.";
    }
}