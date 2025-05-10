package enums;

/**
 * The FuelType enum represents the types of fuel available for vehicles in the system.
 * It provides a method to retrieve a user-friendly display name for each fuel type.
 */
public enum FuelType {

    PETROL, // Represents petrol fuel type.
    DIESEL, // Represents diesel fuel type.
    ELECTRIC, // Represents electric fuel type.
    HYBRID, // Represents hybrid fuel type.
    CNG, // Represents compressed natural gas (CNG) fuel type.
    LPG; // Represents liquefied petroleum gas (LPG) fuel type.

    /**
     * Retrieves the display name for the fuel type.
     *
     * @return A user-friendly name for the fuel type.
     * @throws IllegalArgumentException if the FuelType is unknown.
     */
    public String getDisplayName() {
        switch (this) {
            case PETROL:
                return "Petrol";
            case DIESEL:
                return "Diesel";
            case ELECTRIC:
                return "Electric";
            case HYBRID:
                return "Hybrid";
            case CNG:
                return "Compressed Natural Gas (CNG)";
            case LPG:
                return "Liquefied Petroleum Gas (LPG)";
            default:
                throw new IllegalArgumentException("Unknown FuelType: " + this);
        }
    }
}