package model;

/**
 * The Car class represents a car in the system with various attributes such as brand, model, color, and rental details.
 * It provides methods to retrieve the car's information.
 */
public class Car {
    private int id; // The unique identifier for the car.
    private String brand; // The brand of the car (e.g., Toyota, Ford).
    private String model; // The model of the car (e.g., Corolla, Mustang).
    private String color; // The color of the car.
    private String fuelType; // The type of fuel the car uses (e.g., Gasoline, Diesel, Electric).
    private String transmission; // The transmission type of the car (e.g., Automatic, Manual).
    private int seatingCapacity; // The number of seats available in the car.
    private double rentalPrice; // The rental price of the car per day.
    private String status; // The current status of the car (e.g., "available", "reserved").

    /**
     * Constructs a Car object with the specified attributes.
     *
     * @param id              The unique identifier for the car.
     * @param brand           The brand of the car.
     * @param model           The model of the car.
     * @param color           The color of the car.
     * @param fuelType        The type of fuel the car uses.
     * @param transmission    The transmission type of the car.
     * @param seatingCapacity The number of seats available in the car.
     * @param rentalPrice     The rental price of the car per day.
     * @param status          The current status of the car.
     */
    public Car(int id, String brand, String model, String color, String fuelType, String transmission, int seatingCapacity, double rentalPrice, String status) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.rentalPrice = rentalPrice;
        this.status = status;
    }

    /**
     * Retrieves the unique identifier of the car.
     *
     * @return The car's ID.
     */
    public int getId() { return id; }

    /**
     * Retrieves the brand of the car.
     *
     * @return The car's brand.
     */
    public String getBrand() { return brand; }

    /**
     * Retrieves the model of the car.
     *
     * @return The car's model.
     */
    public String getModel() { return model; }

    /**
     * Retrieves the color of the car.
     *
     * @return The car's color.
     */
    public String getColor() { return color; }

    /**
     * Retrieves the type of fuel the car uses.
     *
     * @return The car's fuel type.
     */
    public String getFuelType() { return fuelType; }

    /**
     * Retrieves the transmission type of the car.
     *
     * @return The car's transmission type.
     */
    public String getTransmission() { return transmission; }

    /**
     * Retrieves the seating capacity of the car.
     *
     * @return The car's seating capacity.
     */
    public int getSeatingCapacity() { return seatingCapacity; }

    /**
     * Retrieves the rental price of the car per day.
     *
     * @return The car's rental price.
     */
    public double getRentalPrice() { return rentalPrice; }

    /**
     * Retrieves the current status of the car.
     *
     * @return The car's status.
     */
    public String getStatus() { return status; }
}