package dao;

import model.Booking;

/**
 * The BookingDAO interface defines the methods for performing CRUD operations
 * on Booking objects in the database.
 */
public interface BookingDAO {

    /**
     * Saves a booking to the database.
     *
     * @param booking The Booking object to be saved.
     * @return true if the booking was successfully saved, false otherwise.
     */
    boolean saveBooking(Booking booking);

    /**
     * Deletes a booking from the database.
     *
     * @param bookingId The unique identifier of the booking to be deleted.
     * @return true if the booking was successfully deleted, false otherwise.
     */
    boolean deleteBooking(int bookingId);
}