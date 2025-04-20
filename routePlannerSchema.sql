-- Create and use the database
CREATE DATABASE IF NOT EXISTS PuneRoutePlanner_db;
USE PuneRoutePlanner_db;

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

-- Insert 5 Pune Locations
INSERT INTO Locations (name, latitude, longitude) VALUES
('Koregaon Park', 18.5362, 73.8940),    -- 1
('Hinjewadi IT Park', 18.5912, 73.7380), -- 2
('Shivaji Nagar', 18.5308, 73.8478),     -- 3
('Kothrud', 18.5074, 73.8077),           -- 4
('Viman Nagar', 18.5679, 73.9143);       -- 5

-- Insert Routes between all locations (undirected graph - each location connects to every other)

-- Koregaon Park connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(1, 2, 22.4, 55),    -- Koregaon Park to Hinjewadi
(1, 3, 6.1, 25),     -- Koregaon Park to Shivaji Nagar
(1, 4, 11.5, 35),    -- Koregaon Park to Kothrud
(1, 5, 7.8, 22);     -- Koregaon Park to Viman Nagar

-- Hinjewadi connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(2, 3, 16.8, 45),    -- Hinjewadi to Shivaji Nagar
(2, 4, 12.3, 35),    -- Hinjewadi to Kothrud
(2, 5, 25.7, 65);    -- Hinjewadi to Viman Nagar

-- Shivaji Nagar connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(3, 4, 5.5, 20),     -- Shivaji Nagar to Kothrud
(3, 5, 9.4, 30);     -- Shivaji Nagar to Viman Nagar

-- Kothrud connections
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min) VALUES
(4, 5, 14.2, 40);    -- Kothrud to Viman Nagar

-- Add reverse routes for complete undirected graph
INSERT INTO Routes (start_location, end_location, distance_km, travel_time_min)
SELECT 
    end_location, 
    start_location, 
    distance_km,    -- Keep same distance for undirected graph
    travel_time_min -- Keep same time for undirected graph
FROM Routes 
WHERE NOT EXISTS (
    SELECT 1 FROM Routes r2 
    WHERE r2.start_location = Routes.end_location 
    AND r2.end_location = Routes.start_location
);

-- Select and verify the data
SELECT * FROM Locations;
SELECT * FROM Routes;
