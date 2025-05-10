package view;

import controller.CarController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CarListPanel class represents the user interface for displaying and filtering
 * a list of available cars. It allows users to apply filters, sort cars, and initiate
 * the car rental process.
 */
public class CarListPanel extends JPanel {
    private final int currentUserId; // ID of the currently logged-in user.

    private final CardLayout cardLayout; // Layout manager for navigating between panels.
    private final JPanel container; // Parent container holding this panel.

    private JTable carTable; // Table to display the list of cars.
    private JTextField minPriceField, maxPriceField; // Fields for filtering by price range.
    private JTextField minSeatField, maxSeatField; // Fields for filtering by seat capacity.
    private JComboBox<String> fuelBox, transBox, colorBox, sortBox; // Dropdowns for filtering and sorting.
    private JButton applyButton, resetButton, rentButton; // Buttons for applying filters, resetting, and renting.

    private List<Car> allCars; // List of all available cars.

    /**
     * Constructs the CarListPanel with the specified parameters.
     *
     * @param cardLayout    The CardLayout for navigating between panels.
     * @param container     The parent container holding this panel.
     * @param currentUserId The ID of the currently logged-in user.
     */
    public CarListPanel(CardLayout cardLayout, JPanel container, int currentUserId) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.currentUserId = currentUserId;
        setLayout(new BorderLayout());

        // Initialize the filter panel at the top.
        JPanel filterPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        fuelBox = new JComboBox<>(new String[]{"All", "Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"All", "Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{"All", "Black", "White", "Red", "Blue", "Grey"});
        sortBox = new JComboBox<>(new String[]{"Sort by Price ↑", "Sort by Price ↓"});

        minPriceField = new JTextField(8);
        maxPriceField = new JTextField(8);
        minSeatField = new JTextField(8);
        maxSeatField = new JTextField(8);

        // Add filter components to the filter panel.
        filterPanel.add(new JLabel("Fuel Type:"));
        filterPanel.add(fuelBox);
        filterPanel.add(new JLabel("Transmission Type:"));
        filterPanel.add(transBox);

        filterPanel.add(new JLabel("Min Price:"));
        filterPanel.add(minPriceField);
        filterPanel.add(new JLabel("Max Price:"));
        filterPanel.add(maxPriceField);

        filterPanel.add(new JLabel("Min Seats:"));
        filterPanel.add(minSeatField);
        filterPanel.add(new JLabel("Max Seats:"));
        filterPanel.add(maxSeatField);

        filterPanel.add(new JLabel("Color:"));
        filterPanel.add(colorBox);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortBox);

        applyButton = new JButton("Apply Filters");
        resetButton = new JButton("Reset Filters");

        filterPanel.add(resetButton);
        filterPanel.add(applyButton);
        filterPanel.add(new JLabel());
        filterPanel.add(new JLabel());

        add(filterPanel, BorderLayout.NORTH);

        // Initialize the table panel at the center.
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        carTable = new JTable(model);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(carTable);
        add(scrollPane, BorderLayout.CENTER);

        // Hide the ID column.
        carTable.getColumnModel().getColumn(0).setMinWidth(0);
        carTable.getColumnModel().getColumn(0).setMaxWidth(0);
        carTable.getColumnModel().getColumn(0).setWidth(0);

        // Initialize the bottom panel with the rent button.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rentButton = new JButton("Rent");
        bottomPanel.add(rentButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add event listeners for buttons and filters.
        applyButton.addActionListener(e -> applyFilters());
        resetButton.addActionListener(e -> {
            fuelBox.setSelectedIndex(0);
            transBox.setSelectedIndex(0);
            colorBox.setSelectedIndex(0);
            sortBox.setSelectedIndex(0);
            minPriceField.setText("");
            maxPriceField.setText("");
            minSeatField.setText("");
            maxSeatField.setText("");
            applyFilters();
        });
        rentButton.addActionListener(e -> onRent());

        loadCars();

        // Reload cars when the panel is shown.
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadCars();
            }
        });
    }

    /**
     * Loads the list of available cars from the CarController and applies filters.
     */
    private void loadCars() {
        allCars = CarController.getAvailableCarsAsObjects();
        applyFilters();
    }

    /**
     * Applies the selected filters and sorting to the list of cars.
     */
    private void applyFilters() {
        String fuel = (String) fuelBox.getSelectedItem();
        String trans = (String) transBox.getSelectedItem();
        String color = (String) colorBox.getSelectedItem();
        String sort = (String) sortBox.getSelectedItem();

        double minPrice = parseDouble(minPriceField.getText(), 0);
        double maxPrice = parseDouble(maxPriceField.getText(), Double.MAX_VALUE);
        int minSeats = parseInt(minSeatField.getText(), 0);
        int maxSeats = parseInt(maxSeatField.getText(), Integer.MAX_VALUE);

        List<Car> filtered = allCars.stream()
                .filter(c -> fuel.equals("All") || c.getFuelType().equalsIgnoreCase(fuel))
                .filter(c -> trans.equals("All") || c.getTransmission().equalsIgnoreCase(trans))
                .filter(c -> color.equals("All") || c.getColor().equalsIgnoreCase(color))
                .filter(c -> c.getRentalPrice() >= minPrice && c.getRentalPrice() <= maxPrice)
                .filter(c -> c.getSeatingCapacity() >= minSeats && c.getSeatingCapacity() <= maxSeats)
                .collect(Collectors.toList());

        if (sort.equals("Sort by Price ↑")) {
            filtered.sort((a, b) -> Double.compare(a.getRentalPrice(), b.getRentalPrice()));
        } else {
            filtered.sort((a, b) -> Double.compare(b.getRentalPrice(), a.getRentalPrice()));
        }

        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        model.setRowCount(0);
        for (Car c : filtered) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getModel(),
                    c.getFuelType(),
                    c.getTransmission(),
                    c.getSeatingCapacity(),
                    c.getColor(),
                    c.getRentalPrice(),
                    c.getStatus()
            });
        }
    }

    /**
     * Parses a string into a double. Returns a default value if parsing fails.
     *
     * @param text       The string to parse.
     * @param defaultVal The default value to return if parsing fails.
     * @return The parsed double or the default value.
     */
    private double parseDouble(String text, double defaultVal) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Parses a string into an integer. Returns a default value if parsing fails.
     *
     * @param text       The string to parse.
     * @param defaultVal The default value to return if parsing fails.
     * @return The parsed integer or the default value.
     */
    private int parseInt(String text, int defaultVal) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Handles the "Rent" button click event. Navigates to the booking panel
     * for the selected car.
     */
    private void onRent() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.");
            return;
        }

        int carId = (int) carTable.getValueAt(row, 0);
        Car selected = allCars.stream().filter(c -> c.getId() == carId).findFirst().orElse(null);

        if (selected != null) {
            BookingPanel bookingPanel = new BookingPanel(cardLayout, container, selected, currentUserId);
            container.add(bookingPanel, "booking");
            cardLayout.show(container, "booking");
        }
    }
}