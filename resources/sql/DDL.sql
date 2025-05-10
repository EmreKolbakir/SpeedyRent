-- Drop the database if it already exists to ensure a clean slate.
DROP DATABASE IF EXISTS srent;

-- Create a new database named 'srent'.
CREATE DATABASE srent;

-- Switch to the 'srent' database for subsequent operations.
USE srent;

-- Create the 'user' table to store user information.
-- Includes user ID, username, and password.
CREATE TABLE user (
                      user_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each user.
                      username VARCHAR(50),                   -- Username of the user.
                      password VARCHAR(50)                    -- Password of the user.
);

-- Create the 'Admin' table to store admin-specific information.
-- Links to the 'user' table via a foreign key.
CREATE TABLE Admin (
                       user_id INT PRIMARY KEY,                -- Unique identifier for the admin (same as user ID).
                       salary DOUBLE,                          -- Salary of the admin.
                       FOREIGN KEY (user_id) REFERENCES user(user_id) -- Foreign key linking to the 'user' table.
);

-- Create the 'Customer' table to store customer-specific information.
-- Links to the 'user' table via a foreign key.
CREATE TABLE Customer (
                          user_id INT PRIMARY KEY,                -- Unique identifier for the customer (same as user ID).
                          occupation VARCHAR(100),                -- Occupation of the customer.
                          FOREIGN KEY (user_id) REFERENCES user(user_id) -- Foreign key linking to the 'user' table.
);

-- Create the 'Card' table to store card information.
-- Includes card details such as brand, number, expiration date, and name on the card.
CREATE TABLE Card (
                      card_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each card.
                      card_brand ENUM('Visa','Mastercard','Maestro','Troy','Other'), -- Card brand.
                      card_number CHAR(16),                   -- Card number (16 characters).
                      exp_date DATE,                          -- Expiration date of the card.
                      name_on_card VARCHAR(60)                -- Name printed on the card.
);

-- Create the 'VehicleSpecification' table to store vehicle specifications.
-- Includes details such as color, fuel type, transmission type, and seating capacity.
CREATE TABLE VehicleSpecification (
                                      specification_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each specification.
                                      color VARCHAR(20),                               -- Color of the vehicle.
                                      fuel_type VARCHAR(20),                           -- Fuel type of the vehicle.
                                      transmission_type VARCHAR(20),                   -- Transmission type of the vehicle.
                                      seating_capacity INT                             -- Seating capacity of the vehicle.
);

-- Create the 'Car' table to store car information.
-- Includes details such as model, daily rent, deposit, mileage, and status.
CREATE TABLE Car (
                     car_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each car.
                     model VARCHAR(50),                     -- Model of the car.
                     daily_rent DOUBLE,                     -- Daily rental price of the car.
                     deposit DOUBLE,                        -- Deposit amount for the car.
                     mileage INT,                           -- Mileage of the car.
                     vehicle_status VARCHAR(20)             -- Status of the car (e.g., available, rented, etc.).
);

-- Create the 'has' table to establish a relationship between cars and their specifications.
CREATE TABLE has (
                     car_id INT,                            -- Unique identifier of the car.
                     specification_id INT,                  -- Unique identifier of the specification.
                     FOREIGN KEY (car_id) REFERENCES Car(car_id), -- Foreign key linking to the 'Car' table.
                     FOREIGN KEY (specification_id) REFERENCES VehicleSpecification(specification_id) -- Foreign key linking to the 'VehicleSpecification' table.
);

-- Create the 'manages' table to establish a relationship between admins and cars they manage.
CREATE TABLE manages (
                         user_id INT,                           -- Unique identifier of the admin.
                         car_id INT,                            -- Unique identifier of the car.
                         FOREIGN KEY (car_id) REFERENCES Car(car_id) -- Foreign key linking to the 'Car' table.
);

-- Create the 'Booking' table to store booking information.
-- Includes details such as start and end dates, status, deposit, amount, and drive option.
CREATE TABLE Booking (
                         booking_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each booking.
                         start_date DATE,                           -- Start date of the booking.
                         end_date DATE,                             -- End date of the booking.
                         booking_status VARCHAR(20),                -- Status of the booking (e.g., confirmed, cancelled, etc.).
                         secure_deposit DOUBLE,                     -- Deposit amount for the booking.
                         amount DOUBLE,                             -- Total amount for the booking.
                         drive_option ENUM('self','chauffeur') DEFAULT 'self', -- Drive option for the booking.
                         reading INT,                               -- Odometer reading at the start of the booking.
                         date_out DATE                              -- Date the car is taken out.
);

-- Create the 'makes' table to establish a relationship between users and their bookings.
CREATE TABLE makes (
                       user_id INT,                               -- Unique identifier of the user.
                       booking_id INT,                            -- Unique identifier of the booking.
                       PRIMARY KEY (user_id, booking_id),         -- Composite primary key for the relationship.
                       FOREIGN KEY (user_id) REFERENCES user(user_id), -- Foreign key linking to the 'user' table.
                       FOREIGN KEY (booking_id) REFERENCES Booking(booking_id) -- Foreign key linking to the 'Booking' table.
);

-- Create the 'reserves' table to establish a relationship between bookings and cars.
CREATE TABLE reserves (
                          booking_id INT,                            -- Unique identifier of the booking.
                          car_id INT,                                -- Unique identifier of the car.
                          FOREIGN KEY (booking_id) REFERENCES Booking(booking_id), -- Foreign key linking to the 'Booking' table.
                          FOREIGN KEY (car_id) REFERENCES Car(car_id) -- Foreign key linking to the 'Car' table.
);