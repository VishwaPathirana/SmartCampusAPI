# SmartCampus Sensor & Room Management API

This project is a RESTful web service built with Java and JAX-RS (Jakarta RESTful Web Services). It is designed to manage campus infrastructure including rooms and sensors.

## Features
- **Discovery Endpoint**: Entry point for API metadata.
- **Room Management**: CRUD operations for rooms with safety constraints.
- **Sensor Tracking**: Registry for various sensors (Temperature, CO2, etc.).
- **Historical Data**: Nested readings for sensors with historical logging.
- **Error Handling**: Custom exception mapping for cleaner API responses.
- **Logging**: Request and response tracking filters.

## Technology Stack
- **Language**: Java 11+
- **Framework**: JAX-RS (Jersey)
- **Build Tool**: Maven
- **Server**: Compatible with Apache Tomcat 10+, GlassFish, or Payara.

## How to Run in NetBeans
1.  **Open Project**:
    - Launch NetBeans.
    - Go to `File` > `Open Project`.
    - Select the folder containing the `pom.xml` file.
2.  **Build**:
    - Right-click the project `SmartCampusAPI` and select `Build`.
3.  **Run**:
    - Right-click the project and select `Run`.
    - Choose your preferred server (e.g., Apache Tomcat or GlassFish).
    - The API will be available at `http://localhost:8080/SmartCampusAPI/api/v1`.

## Sample CURL Commands

### 1. Discovery
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id":"LIB-301", "name":"Library Quiet Study", "capacity":50}'
```

### 3. Register a Sensor
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id":"TEMP-001", "type":"Temperature", "status":"ACTIVE", "roomId":"LIB-301"}'
```

### 4. Add a Reading
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
     -H "Content-Type: application/json" \
     -d '{"value":22.5}'
```

### 5. Filter Sensors by Type
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature
```
