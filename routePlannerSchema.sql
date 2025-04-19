-- Create and use the database
CREATE DATABASE IF NOT EXISTS RoutePlanner_db;
USE RoutePlanner_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS Routes;
DROP TABLE IF EXISTS Locations;

-- Create Locations Table
CREATE TABLE Locations (
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    latitude FLOAT,
    longitude FLOAT
);

-- Create Routes Table
CREATE TABLE Routes (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    start_location INT,
    end_location INT,
    distance_km FLOAT,
    travel_time_min INT,
    FOREIGN KEY (start_location) REFERENCES Locations(location_id),
    FOREIGN KEY (end_location) REFERENCES Locations(location_id)
);

-- Insert 10 Locations
INSERT INTO Locations (name, latitude, longitude) VALUES
('Main Distribution Center', 40.7128, -74.0060),  -- 1
('North Warehouse', 40.8128, -74.1060),           -- 2
('South Warehouse', 40.6128, -73.9060),           -- 3
('Downtown Store', 40.7528, -73.9860),            -- 4
('Mall Store', 40.6828, -73.8760),                -- 5
('Airport Pickup Point', 40.6413, -73.7781),      -- 6
('University Campus', 40.7287, -73.7919),         -- 7
('Hospital Supply', 40.7689, -73.9655),           -- 8
('Industrial Park', 40.6631, -74.0433),           -- 9
('Residential Hub', 40.6782, -73.9442);           -- 10

-- Insert Direct Routes
-- Main Distribution Center connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(1, 2, 12.5, 18),    -- Main DC to North Warehouse
(1, 3, 13.7, 20),    -- Main DC to South Warehouse
(1, 4, 5.2, 15),     -- Main DC to Downtown Store
(1, 8, 7.8, 16);     -- Main DC to Hospital Supply

-- North Warehouse connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(2, 7, 15.3, 22),    -- North Warehouse to University
(2, 8, 9.1, 14);     -- North Warehouse to Hospital Supply

-- South Warehouse connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(3, 5, 8.4, 12),     -- South Warehouse to Mall Store
(3, 9, 6.7, 10),     -- South Warehouse to Industrial Park
(3, 10, 7.2, 11);    -- South Warehouse to Residential Hub

-- Downtown Store connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(4, 7, 11.3, 18),    -- Downtown Store to University
(4, 8, 3.5, 9);      -- Downtown Store to Hospital Supply

-- Mall Store connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(5, 6, 5.9, 12),     -- Mall Store to Airport Pickup
(5, 10, 4.2, 8);     -- Mall Store to Residential Hub

-- Airport Pickup Point connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(6, 7, 10.4, 15),    -- Airport to University
(6, 10, 8.8, 18);    -- Airport to Residential Hub

-- University Campus connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(7, 8, 8.2, 14);     -- University to Hospital Supply

-- Hospital Supply connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(8, 4, 3.5, 10);     -- Hospital Supply to Downtown Store (slight difference for one-way street example)

-- Industrial Park connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(9, 1, 14.5, 19),    -- Industrial Park to Main DC
(9, 10, 9.3, 16);    -- Industrial Park to Residential Hub

-- Residential Hub connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(10, 5, 4.3, 9);     -- Residential Hub to Mall Store

-- Add reverse routes for bidirectional graph (with slightly different metrics for realism - e.g., one-way streets, traffic patterns)
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min)
SELECT 
    end_location, 
    start_location, 
    -- Add small random variations to distance and time for the return trip (more realistic)
    distance_km * (0.95 + RAND() * 0.1), 
    travel_time_min * (0.95 + RAND() * 0.1)
FROM Routes 
WHERE NOT EXISTS (
    SELECT 1 FROM Routes r2 
    WHERE r2.start_location = Routes.end_location 
    AND r2.end_location = Routes.start_location
);

-- Select and verify the data
SELECT * FROM Locations;
SELECT * FROM Routes;