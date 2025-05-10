package controller;

import util.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CardController class provides methods to manage card-related operations in the database.
 * It includes functionality to add, delete, update, and retrieve card information.
 */
public class CardController {

    /**
     * Adds a new card to the database and links it to a user.
     *
     * @param userId The unique identifier of the user to link the card to.
     * @param brand The brand of the card (e.g., Visa, MasterCard).
     * @param number The card number.
     * @param expDate The expiration date of the card.
     * @param nameOnCard The name printed on the card.
     * @return true if the card was successfully added and linked to the user, false otherwise.
     */
    public static boolean addCard(int userId, String brand, String number, String expDate, String nameOnCard) {
        Connection conn = null;
        PreparedStatement psCard = null;
        PreparedStatement psBrings = null;
        ResultSet rs = null;

        try {
            conn = Srent_DB.getConnection();
            if (conn == null) return false;

            String insertCard = "INSERT INTO Card (card_brand, card_number, exp_date, name_on_card) VALUES (?, ?, ?, ?)";
            psCard = conn.prepareStatement(insertCard, Statement.RETURN_GENERATED_KEYS);
            psCard.setString(1, brand);
            psCard.setString(2, number);
            psCard.setString(3, expDate);
            psCard.setString(4, nameOnCard);
            psCard.executeUpdate();

            rs = psCard.getGeneratedKeys();
            if (rs.next()) {
                int cardId = rs.getInt(1);
                String linkCard = "INSERT INTO brings (user_id, card_id) VALUES (?, ?)";
                psBrings = conn.prepareStatement(linkCard);
                psBrings.setInt(1, userId);
                psBrings.setInt(2, cardId);
                psBrings.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psCard != null) psCard.close(); } catch (Exception ignored) {}
            try { if (psBrings != null) psBrings.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return false;
    }

    /**
     * Deletes a card from the database.
     *
     * @param cardId The unique identifier of the card to delete.
     * @return true if the card was successfully deleted, false otherwise.
     */
    public static boolean deleteCard(int cardId) {
        String sql = "DELETE FROM Card WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of cards associated with a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of strings representing the cards linked to the user.
     */
    public static List<String> getCardsByUser(int userId) {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT c.card_id, c.card_brand, c.card_number, c.exp_date, c.name_on_card " +
                "FROM Card c JOIN brings b ON c.card_id = b.card_id WHERE b.user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String card = String.format("Card ID %d: %s ending in ****%s (Exp: %s, Name: %s)",
                        rs.getInt("card_id"),
                        rs.getString("card_brand"),
                        rs.getString("card_number").substring(12),
                        rs.getDate("exp_date"),
                        rs.getString("name_on_card"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Updates the details of an existing card in the database.
     *
     * @param cardId The unique identifier of the card to update.
     * @param brand The updated brand of the card.
     * @param number The updated card number.
     * @param expDate The updated expiration date of the card.
     * @param nameOnCard The updated name printed on the card.
     * @return true if the card was successfully updated, false otherwise.
     */
    public static boolean updateCard(int cardId, String brand, String number, String expDate, String nameOnCard) {
        String sql = "UPDATE Card SET card_brand = ?, card_number = ?, exp_date = ?, name_on_card = ? WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setString(2, number);
            ps.setString(3, expDate);
            ps.setString(4, nameOnCard);
            ps.setInt(5, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the details of a specific card by its unique identifier.
     *
     * @param cardId The unique identifier of the card to retrieve.
     * @return A string representing the card details, or "Card not found." if no card is found.
     */
    public static String getCardById(int cardId) {
        String sql = "SELECT * FROM Card WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("%s ending in ****%s | Exp: %s | Holder: %s",
                        rs.getString("card_brand"),
                        rs.getString("card_number").substring(12),
                        rs.getDate("exp_date"),
                        rs.getString("name_on_card"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Card not found.";
    }
}