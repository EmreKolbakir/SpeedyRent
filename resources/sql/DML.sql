
-- Insert sample data into the 'user' table.
-- Each row represents a user with their details such as ID, gender, email, address, creation date, first name, last name, and username.
INSERT INTO user (user_id, gender, email, address, created_at, first_name, last_name, username)
VALUES
    (1, 'M', 'john.doe@example.com',  '221B Baker St, London',        NOW(), 'John',  'Doe',   'jdoe'),
    (2, 'F', 'ayse.kara@example.com', 'İstiklal Cd. No:12, İstanbul', NOW(), 'Ayşe',  'Kara',  'akar'),
    (3, 'M', 'li.wang@example.com',   '88 Nanjing Rd, Shanghai',      NOW(), 'Li',    'Wang',  'lwang'),
    (4, 'F', 'elena.rossi@example.com', 'Via Roma 10, Milano', NOW(), 'Elena', 'Rossi', 'erossi'),
    (5, 'M', 'ahmet.demir@example.com', 'Anıttepe Mah., Ankara', NOW(), 'Ahmet', 'Demir', 'ademir'),
    (6, 'F', 'sofia.garcia@example.com', 'Calle Mayor 22, Madrid', NOW(), 'Sofia', 'Garcia', 'sgarcia'),
    (7, 'X', 'alex.kim@example.com', 'Gangnam-gu, Seoul', NOW(), 'Alex', 'Kim', 'akim');

-- Insert sample data into the 'customer' table.
-- Each row links a user to their occupation.
INSERT INTO customer (user_id, occupation)
VALUES
    (1, 'Fütürist'),
    (2, 'Düşünür'),
    (3, 'Çapulcu'),
    (4, 'Mimar'),
    (5, 'Yazılım Mühendisi'),
    (6, 'Veri Analisti'),
    (7, 'Gezgin');

-- Insert sample data into the 'booking' table.
-- Each row represents a booking with details such as start and end dates, deposit, amount, drive option, odometer reading, and date out.
INSERT INTO booking
(start_date,  end_date,   secure_deposit, amount, drive_option, reading, date_out)
VALUES  ('2025-05-01','2025-05-07', 500.00,        1200.00,'self',      45500,   '2025-05-01'),
        ('2025-06-12','2025-06-14', 250.00,        4500.00,'chauffeur',  72210,   '2025-06-12'),
        ('2025-07-20','2025-07-27', 800.00,        1900.00,'self',      NULL,    NULL),
        ('2025-08-10', '2025-08-12', 300.00,  1100.00, 'self',     38900, '2025-08-10'),  -- booking_id 4
        ('2025-09-01', '2025-09-05', 700.00,  2600.00, 'chauffeur', NULL,  NULL),        -- booking_id 5
        ('2025-10-15', '2025-10-22', 850.00,  3300.00, 'self',     51000, '2025-10-15'), -- booking_id 6
        ('2025-11-02', '2025-11-07', 400.00,  1700.00, 'self',     47200, '2025-11-02');

-- Insert data into the 'makes' table to link users to their bookings.
-- Each row represents a relationship between a user and a booking.
INSERT INTO makes (user_id, booking_id) VALUES
                                            (1, 1),   -- user 1   → booking 1
                                            (1, 3),   -- user 1   → booking 3
                                            (2, 2),   -- user 2   → booking 2
                                            (4, 4),   -- Elena Rossi → booking 4
                                            (5, 5),   -- Ahmet Demir → booking 5
                                            (6, 6),   -- Sofia Garcia → booking 6
                                            (1, 7);   -- Alex Kim     → booking 7

-- Insert sample data into the 'car' table.
-- Each row represents a car with details such as model, daily rent, deposit, mileage, and status.
INSERT INTO car (model, daily_rent, deposit, mileage, vehicle_status)
VALUES
    ('Peugeot 208 1.2 PureTech',     36.00, 350.00, 58700, 'available'),
    ('Volkswagen Passat 1.5 TSI',    62.00, 600.00, 42300, 'available'),
    ('Hyundai Tucson 1.6',           73.00, 700.00, 47800, 'rented'),
    ('Citroën C4 Cactus 1.2',        41.00, 400.00, 51200, 'available'),
    ('Toyota Camry 2.5 Hybrid',      92.00, 850.00, 28900, 'available'),
    ('Honda Jazz 1.3 i-VTEC',        39.00, 350.00, 68300, 'available'),
    ('Ford Kuga 1.5 EcoBoost',       70.00, 680.00, 40100, 'service'),
    ('Renault Megane 1.3 TCe',       49.00, 500.00, 37600, 'available'),
    ('Fiat Panda 1.2',               29.00, 250.00, 72300, 'available'),
    ('BMW X1 1.5 sDrive',            98.00, 850.00, 31400, 'rented'),
    ('Mercedes-Benz GLA 200',       112.00, 950.00, 28700, 'available'),
    ('Nissan Micra 1.0 IG-T',        34.00, 300.00, 64500, 'available'),
    ('Skoda Octavia 1.0 TSI',        52.00, 500.00, 48800, 'service'),
    ('Mitsubishi ASX 1.6',           54.00, 500.00, 46300, 'available'),
    ('Kia Ceed 1.4 MPI',             46.00, 450.00, 51000, 'available'),
    ('Mazda CX-3 2.0',               67.00, 680.00, 42900, 'rented'),
    ('Suzuki Swift 1.2',             31.00, 280.00, 81200, 'available'),
    ('Jeep Compass 1.4',             85.00, 750.00, 39200, 'available'),
    ('Seat Arona 1.0 TSI',           47.00, 460.00, 47300, 'available'),
    ('Audi Q2 1.0 TFSI',             89.00, 800.00, 31700, 'available'),
    ('Opel Insignia 1.5 Turbo',      63.00, 600.00, 50200, 'available'),
    ('Ford Fiesta 1.1',              35.00, 300.00, 69800, 'retired'),
    ('Dacia Sandero 1.0',            32.00, 290.00, 77400, 'available'),
    ('Tesla Model Y Long Range',    140.00, 1100.00, 15800, 'rented'),
    ('Volkswagen T-Roc 1.5',         76.00, 700.00, 34500, 'available'),
    ('Peugeot 2008 1.2 PureTech',    59.00, 550.00, 36800, 'available'),
    ('BMW 118i',                     85.00, 800.00, 30900, 'service'),
    ('Volvo V40 1.5 T3',             69.00, 700.00, 38900, 'available'),
    ('Alfa Romeo Stelvio 2.0T',     125.00, 1100.00, 21000, 'available'),
    ('Toyota Aygo 1.0',              30.00, 250.00, 85200, 'retired'),
    ('Renault Talisman 1.6',         60.00, 600.00, 43100, 'available'),
    ('Honda CR-V 2.0 Hybrid',       110.00, 950.00, 29700, 'rented'),
    ('Citroën C3 1.2',               37.00, 350.00, 62200, 'available'),
    ('volkswagen t-roc',             50.00, 370.00, 92900, 'available'),
    ('Lexus NX 300h',               130.00, 1100.00, 22500, 'available');

-- Insert data into the 'admin' table.
-- Each row links a user to their salary as an admin.
INSERT INTO admin (user_id, salary)
VALUES
    (3, 9200.00),  -- Li Wang
    (5, 8800.00);  -- Ahmet Demir

-- Clear existing data from the 'VehicleSpecification' table.
DELETE FROM VehicleSpecification;

-- Insert sample data into the 'VehicleSpecification' table.
-- Each row represents a vehicle specification with details such as color, fuel type, transmission type, and seating capacity.
INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity)
VALUES
    ('Red',        'petrol',     'manual',    5),
    ('Black',      'diesel',     'automatic', 4),
    ('White',      'hybrid',     'automatic', 5),
    ('Blue',       'gasoline',   'automatic', 4),
    ('Grey',       'petrol',     'automatic', 5),
    ('Silver',     'diesel',     'manual',    4),
    ('Green',      'gasoline',   'automatic', 2),
    ('Yellow',     'petrol',     'manual',    2),
    ('Orange',     'hybrid',     'manual',    5),
    ('Beige',      'diesel',     'automatic', 7),
    ('Brown',      'petrol',     'automatic', 5),
    ('Navy',       'hybrid',     'automatic', 5),
    ('Maroon',     'diesel',     'manual',    4),
    ('Purple',     'gasoline',   'automatic', 2),
    ('White',      'petrol',     'manual',    4),
    ('Black',      'hybrid',     'manual',    5),
    ('Blue',       'diesel',     'automatic', 7),
    ('Green',      'petrol',     'automatic', 5),
    ('Silver',     'gasoline',   'automatic', 4),
    ('Red',        'hybrid',     'automatic', 6),
    ('Cyan',       'petrol',     'automatic', 4),
    ('Dark Grey',  'diesel',     'manual',    5),
    ('Ivory',      'hybrid',     'manual',    4),
    ('Turquoise',  'gasoline',   'automatic', 5),
    ('Coral',      'petrol',     'automatic', 2),
    ('Amber',      'diesel',     'manual',    4),
    ('Teal',       'gasoline',   'manual',    5),
    ('Charcoal',   'hybrid',     'automatic', 5),
    ('Olive',      'diesel',     'automatic', 7),
    ('Mint',       'petrol',     'manual',    5),
    ('Lavender',   'hybrid',     'automatic', 5),
    ('Gold',       'gasoline',   'manual',    4),
    ('Steel Blue', 'diesel',     'automatic', 6),
    ('Champagne',  'petrol',     'manual',    4);

-- Clear existing data from the 'has' table.
DELETE FROM has;

-- Insert data into the 'has' table to link cars to their specifications.
-- Each row represents a relationship between a car and a specification.
INSERT INTO has (car_id, specification_id)
VALUES
    (1,  1),   (2,  2),   (3,  3),   (4,  4),   (5,  5),   (6,  6),
    (7,  7),   (8,  8),   (9,  9),   (10, 10),  (11, 11),  (12, 12),
    (13, 13),  (14, 14),  (15, 15),  (16, 16),  (17, 17),  (18, 18),
    (19, 19),  (20, 20),  (21, 21),  (22, 22),  (23, 23),  (24, 24),
    (25, 25),  (26, 26),  (27, 27),  (28, 28),  (29, 29),  (30, 30),
    (31, 31),  (32, 32),  (33, 33),  (34, 34);
=======
-- === Örnek Kullanıcılar ===
INSERT INTO user (username, password) VALUES 
('admin1', 'adminpass'),
('customer1', 'custpass'),
('customer2', 'custpass');

-- === Admin ===
INSERT INTO Admin (user_id, salary) VALUES 
(1, 8000.00);

-- === Customer ===
INSERT INTO Customer (user_id, occupation) VALUES 
(2, 'Engineer'),
(3, 'Student');

-- === Kartlar (isteğe bağlı kullanılabilir) ===
INSERT INTO Card (card_brand, card_number, exp_date, name_on_card) VALUES
('Visa', '1111222233334444', '2027-12-31', 'Customer One'),
('Mastercard', '5555666677778888', '2026-11-30', 'Customer Two');

-- === Teknik Özellikler ===
INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity) VALUES
('Black', 'Gasoline', 'Automatic', 5),
('White', 'Electric', 'Automatic', 4),
('Red', 'Diesel', 'Manual', 7);

-- === Araçlar ===
INSERT INTO Car (model, daily_rent, deposit, mileage, vehicle_status) VALUES
('Toyota Corolla', 500.0, 1000.0, 25000, 'available'),
('Tesla Model 3', 1200.0, 2500.0, 10000, 'available'),
('Renault Traffic', 800.0, 1500.0, 40000, 'available');

-- === Araçlara teknik özellik ata ===
INSERT INTO has (car_id, specification_id) VALUES 
(1, 1),
(2, 2),
(3, 3);

-- === Admin araç yönetimi (isteğe bağlı) ===
INSERT INTO manages (user_id, car_id) VALUES 
(1, 1),
(1, 2),
(1, 3);
