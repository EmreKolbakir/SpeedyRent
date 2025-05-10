package enums;

/**
 * The UserRole enum represents the roles a user can have in the system.
 * It provides a method to retrieve a user-friendly display name for each role.
 */
public enum UserRole {
    ADMIN, // Represents an administrator role.
    GUEST; // Represents a guest role.

    /**
     * Retrieves the display name for the user role.
     *
     * @return A user-friendly name for the role.
     * @throws IllegalArgumentException if the UserRole is unknown.
     */
    public String getDisplayName() {
        switch (this) {
            case ADMIN:
                return "Administrator";
            case GUEST:
                return "Guest";
            default:
                throw new IllegalArgumentException("Unknown UserRole: " + this);
        }
    }
}