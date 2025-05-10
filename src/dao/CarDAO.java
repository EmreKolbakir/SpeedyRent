package dao;

import model.Car;
import java.util.List;

/**
 * The CarDAO interface defines the operations for accessing and managing car data in the system.
 * It provides a method to retrieve a list of all cars.
 */
public interface CarDAO {

    /**
     * Retrieves a list of all cars in the system.
     *
     * @return A list of Car objects representing all cars.
     */
    List<Car> getAllCars();
}