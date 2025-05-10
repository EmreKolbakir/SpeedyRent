package model;

/**
 * The Booking class represents a booking made by a user for a car.
 * It includes details such as the user ID, car ID, start date, and end date of the booking.
 */
public class Booking {
    private final int userId; // The ID of the user who made the booking.
    private final int carId; // The ID of the car being booked.
    private final String startDate; // The start date of the booking.
    private final String endDate; // The end date of the booking.

    /**
     * Constructs a Booking object with the specified user ID, car ID, start date, and end date.
     *
     * @param userId    The ID of the user who made the booking.
     * @param carId     The ID of the car being booked.
     * @param startDate The start date of the booking.
     * @param endDate   The end date of the booking.
     */
    public Booking(int userId, int carId, String startDate, String endDate) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Retrieves the ID of the user who made the booking.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Retrieves the ID of the car being booked.
     *
     * @return The car ID.
     */
    public int getCarId() {
        return carId;
    }

    /**
     * Retrieves the start date of the booking.
     *
     * @return The start date of the booking.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Retrieves the end date of the booking.
     *
     * @return The end date of the booking.
     */
    public String getEndDate() {
        return endDate;
    }
}