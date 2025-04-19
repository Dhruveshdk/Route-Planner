# 🚀 Route Planner Project

A Java-based Route Planning System that calculates the **shortest delivery route** between two locations using **Dijkstra’s Algorithm** and generates a **detailed travel report**. It uses a **MySQL database** to store locations and routes and integrates with Java via **JDBC**.

---

## 📌 Features

- 🔁 Dijkstra's Algorithm for shortest path calculation
- 🗺️ Graph stored as adjacency list
- 💡 Cost and time estimation
- 📄 Route report generation (console & file)
- 🛢️ MySQL database integration
- 🔍 Modular Java code using OOP principles

---

## 🛠️ Tech Stack

### 🧩 Technologies Used

- **Java** — main programming language
- **MySQL** — database to store routes and locations
- **JDBC** — for database connectivity
- **Dijkstra’s Algorithm** — shortest path calculation
- **IntelliJ IDEA** — IDE used for development

---

## 🗃️ Project Structure
RoutePlannerProject/
│
├── src/
│   ├── DBConnection.java                  # MySQL database connector
│   ├── Location.java                      # Data class for a location
│   ├── Route.java                         # Data class for a route
│   ├── Graph.java                         # Represents the graph using adjacency list
│   ├── DijkstraAlgorithm.java             # Implements Dijkstra’s algorithm
│   ├── Pair.java                          # Implements Pair class
│   ├── RoutePlanner.java                  # Main logic for loading data and planning routes
│   └── ReportGenerator.java               # Generates route reports
│
├── routePlannerSchema.sql                             # SQL script to create tables
├── README.md                              # Project documentation



---

## 🧠 How It Works

1. **Database Setup**
   - `Locations` table stores location names
   - `Routes` table stores direct connections (with distance and time)

2. **Route Planning**
   - User inputs source & destination names
   - System uses Dijkstra’s Algorithm to find the shortest path (by distance)
   - Calculates total distance, time, and estimated delivery cost

3. **Reporting**
   - Route displayed in the console
   - Option to export report to a `.txt` file

---

---

# Route Planner

## 📝 Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/Route-Planner.git
cd Route-Planner
```

### 2. Setup MySQL
Open your MySQL client and run:
```sql
SOURCE schema.sql;
```
Then insert sample data for Locations and Routes.

### 3. Configure Database Credentials
In `DBConnection.java`, update:
```java
String url = "jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME";
String user = "YOUR_USERNAME";
String password = "YOUR_PASSWORD";
```

### 4. Run the Project
- Open in IntelliJ IDEA
- Run `RoutePlanner.java`


Sample Output:
Loading Route Planner System...
Database connected successfully.
Database connected successfully.
Maximum location ID found: 10
Graph initialized with 11 vertices.
Database connected successfully.
Graph populated with 36 edges.

AVAILABLE LOCATIONS:
----------------------------------------
ID    | Location Name                 
----------------------------------------
1     | Main Distribution Center      
2     | North Warehouse               
3     | South Warehouse               
4     | Downtown Store                
5     | Mall Store                    
6     | Airport Pickup Point          
7     | University Campus             
8     | Hospital Supply               
9     | Industrial Park               
10    | Residential Hub               
----------------------------------------

Enter source location ID:
1
Enter destination location ID:
7

Calculating optimal route...

--- ROUTE FOUND ---
Shortest distance from Main Distribution Center to University Campus is 16 km
Path: Main Distribution Center -> Downtown Store -> University Campus
Database connected successfully.

============= ROUTE REPORT =============
From: Main Distribution Center
To: University Campus
Date: 2025-04-20 02:13:21
---------------------------------------
Route Details:
Main Distribution Center → Downtown Store → University Campus
---------------------------------------
Total Distance: 16.50 km
Estimated Travel Time: 33 min
Estimated Delivery Cost: $8.25
=======================================

Do you want to export this report to a file? (y/n)
y
Database connected successfully.
Report successfully exported to: route_report_1_to_7.txt

🧑‍💻 Authors
Dhruvesh Kamble

📃 License
This project is open-source and free to use under the MIT License.
