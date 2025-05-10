package view;

import controller.CarController;
import controller.VehicleSpecificationController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Car;

/**
 * The AdminPanel class represents the admin interface for managing cars.
 * It provides functionality to add, update, and delete cars, as well as
 * display a list of cars in a table format.
 */
public class AdminPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;

    private JTable carTable;
    private DefaultTableModel tableModel;

    private JTextField modelField, rentField, seatField;
    private JComboBox<String> fuelBox, transBox, colorBox, statusBox;
    private JButton addButton, deleteButton, updateButton;

    /**
     * Constructs the AdminPanel with the specified CardLayout and container.
     *
     * @param cardLayout The CardLayout used for navigation.
     * @param container  The parent container for the panel.
     */
    public AdminPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new BorderLayout());

        // Initialize and add the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Initialize form fields
        modelField = new JTextField();
        rentField = new JTextField();
        seatField = new JTextField();

        fuelBox = new JComboBox<>(new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{
                "White", "Black", "Gray", "Silver", "Blue", "Red", "Brown",
                "Green", "Beige", "Yellow", "Orange", "Gold", "Purple",
                "Navy", "Maroon", "Bronze", "Turquoise", "Teal"
        });

        statusBox = new JComboBox<>(new String[]{"available", "rented", "service", "retired"});

        // Add form fields to the form panel
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(modelField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Transmission:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(transBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Rent/Day:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(rentField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Seats:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(seatField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(fuelBox, gbc);
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(colorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(statusBox, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Initialize and add the table panel
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Rent", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // Initialize and add the button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add event listeners for buttons and table selection
        addButton.addActionListener(e -> onAddCar());
        deleteButton.addActionListener(e -> onDeleteCar());
        updateButton.addActionListener(e -> onUpdateCar());
        carTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        // Load cars into the table
        loadCars();
    }

    /**
     * Loads the list of cars from the CarController and populates the table.
     */
    private void loadCars() {
        tableModel.setRowCount(0);
        List<Car> cars = CarController.getAllCarsAsObjects();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getId(), car.getModel(),
                    car.getFuelType(), car.getTransmission(),
                    car.getSeatingCapacity(), car.getColor(),
                    car.getRentalPrice(), car.getStatus()
            });
        }
    }

    /**
     * Fills the form fields with the data of the selected car in the table.
     */
    private void fillFormFromTable() {
        int row = carTable.getSelectedRow();
        if (row == -1) return;

        modelField.setText(tableModel.getValueAt(row, 1).toString());
        fuelBox.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        transBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        seatField.setText(tableModel.getValueAt(row, 4).toString());
        colorBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        rentField.setText(tableModel.getValueAt(row, 6).toString());
        statusBox.setSelectedItem(tableModel.getValueAt(row, 7).toString());
    }

    /**
     * Clears the form fields and deselects any selected row in the table.
     */
    private void clearForm() {
        modelField.setText("");
        rentField.setText("");
        seatField.setText("");
        fuelBox.setSelectedIndex(0);
        transBox.setSelectedIndex(0);
        colorBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        carTable.clearSelection();
    }

    /**
     * Handles the "Add" button click event to add a new car.
     * Validates input fields and interacts with the controllers to add the car.
     */
    private void onAddCar() {
        try {
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();
            String color = (String) colorBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();

            boolean specAdded = VehicleSpecificationController.addSpecification(color, fuel, trans, seats);
            if (!specAdded) {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle specification.");
                return;
            }

            int lastSpecId = VehicleSpecificationController.getAllSpecifications().size();
            boolean added = CarController.addCar(model, rent, 0.0, 0, status, lastSpecId);

            if (added) {
                JOptionPane.showMessageDialog(this, "Car successfully added.");
                loadCars();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add car.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rent and seat fields must be valid numbers.");
        }
    }

    /**
     * Handles the "Delete" button click event to delete the selected car.
     * Prompts the user to select a car if none is selected.
     */
    private void onDeleteCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.");
            return;
        }
        int carId = (int) tableModel.getValueAt(row, 0);
        if (CarController.deleteCar(carId)) {
            JOptionPane.showMessageDialog(this, "Car successfully deleted.");
            loadCars();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete car.");
        }
    }

    /**
     * Handles the "Update" button click event to update the selected car.
     * Validates input fields and interacts with the controllers to update the car.
     */
    private void onUpdateCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to update.");
            return;
        }
        try {
            int carId = (int) tableModel.getValueAt(row, 0);
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();
            String color = (String) colorBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();

            boolean carOk = CarController.updateCar(carId, model, rent, status);
            int specId = CarController.getSpecificationIdForCar(carId);
            boolean specOk = false;

            if (specId > 0) {
                specOk = VehicleSpecificationController.updateSpecification(specId, color, fuel, trans, seats);
            }

            if (carOk && specOk) {
                JOptionPane.showMessageDialog(this, "Car successfully updated.");
                loadCars();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update car.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }
}