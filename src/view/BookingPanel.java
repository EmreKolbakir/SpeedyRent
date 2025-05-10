package view;

import controller.BookingController;
import controller.CarController;
import model.Car;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingPanel extends JPanel {

    private final CardLayout cardLayout; // Layout manager for navigating between panels.
    private final JPanel container; // Parent container holding this panel.
    private final Car selectedCar; // The car selected for booking.
    private final int currentUserId; // ID of the currently logged-in user.

    private JSpinner spinnerStartDate; // Spinner for selecting the booking start date.
    private JSpinner spinnerEndDate; // Spinner for selecting the booking end date.
    private JCheckBox chkGPS; // Checkbox for selecting GPS as an additional service.
    private JCheckBox chkChildSeat; // Checkbox for selecting a child seat as an additional service.
    private JCheckBox chkInsurance; // Checkbox for selecting insurance as an additional service.
    private JLabel lblTotalCost; // Label to display the total cost of the booking.
    private JButton btnConfirm; // Button to confirm the booking.
    private JButton btnCancel; // Button to cancel the booking.

    // Service rates per day
    private static final double GPS_RATE = 10.0; // Daily rate for GPS service.
    private static final double CHILD_SEAT_RATE = 5.0; // Daily rate for child seat service.
    private static final double INSURANCE_RATE = 20.0; // Daily rate for insurance service.

    /**
     * Constructs the BookingPanel with the specified parameters.
     *
     * @param cardLayout    The CardLayout for navigating between panels.
     * @param container     The parent container holding this panel.
     * @param selectedCar   The car selected for booking.
     * @param currentUserId The ID of the currently logged-in user.
     */
    public BookingPanel(CardLayout cardLayout,
                        JPanel container,
                        Car selectedCar,
                        int currentUserId) {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final Car selectedCar;
    private final int currentUserId;

    private JSpinner spinnerStartDate;
    private JSpinner spinnerEndDate;
    private JCheckBox chkGPS;
    private JCheckBox chkChildSeat;
    private JCheckBox chkInsurance;
    private JLabel lblTotalCost;
    private JButton btnConfirm;
    private JButton btnCancel;

    private static final double GPS_RATE = 10.0;
    private static final double CHILD_SEAT_RATE = 5.0;
    private static final double INSURANCE_RATE = 20.0;

    public BookingPanel(CardLayout cardLayout, JPanel container, Car selectedCar, int currentUserId) {

        this.cardLayout = cardLayout;
        this.container = container;
        this.selectedCar = selectedCar;
        this.currentUserId = currentUserId;

        setLayout(new BorderLayout(10, 10));
        initComponents();
        registerListeners();
        updateTotalCost();
    }

    /**
     * Initializes the components of the BookingPanel.
     * Sets up the layout, form fields, and buttons.
     */
    private void initComponents() {
        JLabel title = new JLabel("Booking: " + selectedCar.getModel(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Start Date:"));
        spinnerStartDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "yyyy-MM-dd"));
        formPanel.add(spinnerStartDate);

        formPanel.add(new JLabel("End Date:"));
        spinnerEndDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerEndDate.setEditor(new JSpinner.DateEditor(spinnerEndDate, "yyyy-MM-dd"));
        formPanel.add(spinnerEndDate);

        formPanel.add(new JLabel("Additional Services:"));
        JPanel servicesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkGPS = new JCheckBox("GPS");
        chkChildSeat = new JCheckBox("Child Seat");
        chkInsurance = new JCheckBox("Insurance");
        servicesPanel.add(chkGPS);
        servicesPanel.add(chkChildSeat);
        servicesPanel.add(chkInsurance);
        formPanel.add(servicesPanel);

        formPanel.add(new JLabel("Total Cost:"));
        lblTotalCost = new JLabel();
        lblTotalCost.setFont(lblTotalCost.getFont().deriveFont(Font.BOLD));
        formPanel.add(lblTotalCost);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnConfirm = new JButton("Confirm Booking");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers event listeners for the components.
     * Updates the total cost when inputs change and handles button actions.
     */
    private void registerListeners() {
        spinnerStartDate.addChangeListener(e -> updateTotalCost());
        spinnerEndDate.addChangeListener(e -> updateTotalCost());
        chkGPS.addItemListener(e -> updateTotalCost());
        chkChildSeat.addItemListener(e -> updateTotalCost());
        chkInsurance.addItemListener(e -> updateTotalCost());

        btnConfirm.addActionListener(e -> doConfirmBooking());
        btnCancel.addActionListener(e -> cardLayout.show(container, "carlist"));
    }

    /**
     * Updates the total cost of the booking based on selected dates and services.
     */
    private void updateTotalCost() {
        LocalDate start = ((Date) spinnerStartDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) spinnerEndDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days < 1) days = 1;

        double base = selectedCar.getRentalPrice() * days;
        double services = 0;
        if (chkGPS.isSelected()) services += GPS_RATE * days;
        if (chkChildSeat.isSelected()) services += CHILD_SEAT_RATE * days;
        if (chkInsurance.isSelected()) services += INSURANCE_RATE * days;

        double total = base + services;
        lblTotalCost.setText(String.format("%.2f", total));
    }

    /**
     * Confirms the booking by invoking the BookingController.
     * Updates the car's status and displays a success or error message.
     */
    private void doConfirmBooking() {
        if (!CarController.isCarAvailable(selectedCar.getId())) {
            JOptionPane.showMessageDialog(this,
                    "Sorry, this car is no longer available.",
                    "Unavailable", JOptionPane.WARNING_MESSAGE);
            cardLayout.show(container, "carlist");
            return;
        }

        LocalDate start = ((Date) spinnerStartDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) spinnerEndDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        String startStr = start.toString();
        String endStr = end.toString();

        double totalCost = Double.parseDouble(lblTotalCost.getText().replace("$", "").replace(",", "."));


        double totalCost;
        try {
            String costStr = lblTotalCost.getText().replace("$", "").trim();
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(costStr);
            totalCost = number.doubleValue();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid total cost format.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        boolean success = BookingController.createBooking(
                currentUserId,
                selectedCar.getId(),
                startStr,
                endStr,

                0.0, // deposit
                totalCost,
                "self", // drive option
                0, // initial odometer
                startStr // date out

                0.0,
                totalCost,
                "self",
                0,
                startStr

        );

        if (success) {

            // Car status becomes "reserved"

            CarController.updateCar(selectedCar.getId(), selectedCar.getModel(), selectedCar.getRentalPrice(), "reserved");
            JOptionPane.showMessageDialog(this,

                    "Booking confirmed!\nTotal: " + lblTotalCost.getText(),

                    "Booking confirmed!\nTotal: $" + totalCost,

                    "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(container, "carlist");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}