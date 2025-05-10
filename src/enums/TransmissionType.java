package enums;

/**
 * The TransmissionType enum represents the types of car transmissions available in the system.
 * It provides a method to retrieve a user-friendly display name for each transmission type.
 */
public enum TransmissionType {
    MANUAL, // Represents a manual transmission.
    AUTOMATIC, // Represents an automatic transmission.
    SEMI_AUTOMATIC; // Represents a semi-automatic transmission.

    /**
     * Retrieves the display name for the transmission type.
     *
     * @return A user-friendly name for the transmission type.
     * @throws IllegalArgumentException if the TransmissionType is unknown.
     */
    public String getDisplayName() {
        switch (this) {
            case MANUAL:
                return "Manual";
            case AUTOMATIC:
                return "Automatic";
            case SEMI_AUTOMATIC:
                return "Semi-Automatic";
            default:
                throw new IllegalArgumentException("Unknown TransmissionType: " + this);
        }
    }
}