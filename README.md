# Smart Campus Sensor & Room Management API

**Student:** Vishwa Pathirana
**Student ID:** w2152993
**Module:** 5COSC022C.2 Client-Server Architectures  

---

## 📖 Overview

The Smart Campus API is a RESTful web service developed using **JAX-RS** and **Jersey**. It is designed to support the university’s Smart Campus initiative by allowing facilities managers and automated systems to manage physical locations (**Rooms**), hardware devices (**Sensors**), and historical telemetry (**Sensor Readings**).

### Key Architectural Features

- **Resource-Oriented Design:** A clear resource hierarchy based on real campus entities such as rooms and sensors.
- **Versioned API Entry Point:** All endpoints are exposed under `/api/v1`.
- **In-Memory Data Store:** Uses Java collections instead of a database, as required by the coursework.
- **Sub-Resource Routing:** Supports nested endpoints such as `/sensors/{id}/readings`.
- **Advanced Error Handling:** Custom exceptions are mapped to meaningful HTTP responses like `409`, `422`, `403`, and `500`.
- **Global Logging:** Request and response logging is handled using JAX-RS filters.

### Main Resources

- `GET /`
- `GET /rooms`
- `POST /rooms`
- `GET /rooms/{roomId}`
- `DELETE /rooms/{roomId}`
- `GET /sensors`
- `POST /sensors`
- `GET /sensors/{sensorId}`
- `GET /sensors/{sensorId}/readings`
- `POST /sensors/{sensorId}/readings`

---

## 🛠 Technologies Used

- Java
- JAX-RS
- Jersey
- Apache Tomcat
- Maven
- Apache NetBeans
- Postman

---

## 🚀 Build and Launch Instructions

### Prerequisites

- JDK 17 or higher
- Apache Maven
- Apache Tomcat
- Apache NetBeans (recommended)

### Option 1: Run in NetBeans

1. Open the project in **Apache NetBeans**.
2. Make sure **Apache Tomcat** is selected as the server.
3. Right-click the project and choose **Clean and Build**.
4. Right-click the project and choose **Run**.
5. Open the API discovery endpoint in the browser or Postman:

```text
http://localhost:8080/SmartCampusApi/api/v1
```

### Option 2: Build with Maven and deploy manually

1. Open a terminal in the project folder.
2. Run:

```bash
mvn clean install
```

3. This will generate a `.war` file in the `target/` directory.
4. Copy `SmartCampusApi.war` into the `webapps/` folder of Apache Tomcat.
5. Start Tomcat.
6. Open:

```text
http://localhost:8080/SmartCampusApi/api/v1
```

> If the deployed context path is different on another machine, replace `SmartCampusApi` with the actual deployed application name.

---

## 📡 Sample cURL Commands

### 1. View API discovery endpoint

```bash
curl -i http://localhost:8080/SmartCampusApi/
```

### 2. Get all rooms

```bash
curl -i http://localhost:8080/SmartCampusApi/rooms
```

### 3. Create a new room

```bash
curl -i -X POST http://localhost:8080/SmartCampusApi/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"ENG-101\",\"name\":\"Engineering Lab\",\"capacity\":40}"
```

### 4. Get a room by ID

```bash
curl -i http://localhost:8080/SmartCampusApi/rooms/ENG-101
```

### 5. Register a valid sensor

```bash
curl -i -X POST http://localhost:8080/SmartCampusApi/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":400.0,\"roomId\":\"ENG-101\"}"
```

### 6. Register an invalid sensor with a missing room reference

```bash
curl -i -X POST http://localhost:8080/SmartCampusApi/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"CO2-999\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":410.0,\"roomId\":\"NO-ROOM\"}"
```

### 7. Filter sensors by type

```bash
curl -i "http://localhost:8080/SmartCampusApi/sensors?type=CO2"
```

### 8. Add a new reading to a sensor

```bash
curl -i -X POST http://localhost:8080/SmartCampusApi/sensors/CO2-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\":421.7}"
```

### 9. Get reading history for a sensor

```bash
curl -i http://localhost:8080/SmartCampusApi/sensors/CO2-001/readings
```

### 10. Trigger a room deletion conflict

```bash
curl -i -X DELETE http://localhost:8080/SmartCampusApi/rooms/ENG-101
```

### 11. Trigger the global 500 error mapper

```bash
curl -i http://localhost:8080/SmartCampusApi/debug/crash
```

---

# 📝 Conceptual Report Answers

## Chapter 1: Setup & Discovery

### 1.	Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

• By default, the resource classes in JAX-RS are designed to have request scope, which means that a new object is created for every request received and destroyed when the response has been delivered.

Although this is a good practice, since requests are isolated from one another, instance variables are not maintained between requests. Hence, it is not possible to store application state (e.g., Rooms, Sensors) in these variables.

To overcome this problem, an in-memory data storage is used, usually implemented using static collections. As different threads could access the same data simultaneously, this may lead to race conditions.

To avoid such problems, thread-safe collections like ConcurrentHashMap are used.

### 2. Why is the provision of Hypermedia (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

• The inclusion of hypermedia (HATEOAS) into an API is considered one of the defining aspects of a RESTful API. The reason is that the server provides navigation via links that inform the client of how they should navigate the API.

This minimizes reliance on static documentation, making it easier for clients to explore all the actions they are capable of performing through links included in responses. This also results in lower coupling of the client and the API, enabling the latter to change independently.

---

## Chapter 2: Room Management

### 3. When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

• Sending back just the ID numbers of the rooms keeps the size of the package low, which means there will be lower bandwidth consumption. But the client must send more requests to obtain information about other aspects of the rooms.

Sending back complete object details means a larger package size, but the client only needs one request.

In this case, sending back complete objects is more desirable because the database contains few items.

### 4. Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

• Yes, DELETE is an idempotent operation. 
For the DELETE request for the room:

First request deletes the room.
Further identical requests result in a 404 Not Found response as the room does not exist anymore.

Despite the difference in responses, there is no change in the end state of the system due to the first request only. Hence, there are no side effects on making further requests.

---

## Chapter 3: Sensors & Filtering

### 5. We explicitly use the `@Consumes(MediaType.APPLICATION_JSON)` annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as `text/plain` or `application/xml`. How does JAX-RS handle this mismatch?

• The @Consumes(MediaType.APPLICATION_JSON) annotation denotes that this API accepts JSON-format request bodies only.

In the event that any other type of data, for instance, "text/plain" or "application/xml," comes in from the client side, there would be no matching message body reader found by the JAX-RS framework to handle that incoming request.

Therefore, the request is automatically denied, resulting in an HTTP 415 Unsupported Media Type status being sent back by the server to the client.

### 6. You implemented this filtering using `@QueryParam`. Contrast this with an alternative design where the type is part of the URL path (e.g. `/api/v1/sensors/type/CO2`). Why is the query parameter approach generally considered superior for filtering and searching collections?

• Filtering with @QueryParam is considered better since it considers filters as an optional restriction upon a collection resource.

It allows adding various restrictions in one URI as follows:

/api/v1/sensors?type=CO2&status=ACTIVE

While using embedded filter within the path will make URI less flexible and result in a variety of endpoints like:

/sensors/type/CO2

As a result, it can be concluded that filtering should be done via query parameter as it looks more elegant and RESTful.
---

## Chapter 4: Sub-Resources

### 7. Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., `sensors/{id}/readings/{rid}`) in one massive controller class?

• Sub-Resource Locator Pattern helps to improve the design of the API by offloading nested resources into different classes.

This method is achieved by using a particular class (SensorResource) that will delegate calls made to another class (SensorReadingResource) which is responsible for the sub-resources.

Advantages include:

Enhances modularity and separation of concerns
Easier to maintain and read code
Easy to scale for big projects
Lower complexity in each class

---

## Chapter 5: Error Handling & Logging

### 8. Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

• The HTTP 422 (Unprocessable Entity) code can be used in cases where the syntax of the request is correct but the semantics of the request are wrong.

For instance, when a client requests to add a new sensor by sending a proper JSON request but mentions a room that does not exist, the error is caused by the content of the request, and not the endpoint itself.

A 404 Not Found would suggest that the endpoint is not available, but this is not true in this case.  

### 9. From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

• The exposure of the Java stack trace in an internal manner can expose various system-related sensitive information such as:

Names of classes and methods
Paths to files and directories
Frameworks used and their versions
Execution path of the application

Such sensitive information can be leveraged for crafting targeted attacks. To prevent such an exposure, APIs must ensure that JSON-based error responses are provided.

### 10. Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting `Logger.info()` statements inside every single resource method?

• JAX-RS Filters allow the programmer to implement cross-cutting functionality like logging in a centralized manner.

If we choose to do the logging via filters, then:

It becomes globally applicable to all methods 
There is no code repetition 
The resource method becomes cleaner since it only handles the business logic.

However, if we write the logging code in each method, we will end up repeating ourselves and making our code inconsistent.

---

## ✅ Video Demonstration Checklist

The video demonstrates:

- `GET /` discovery endpoint
- `GET /rooms`
- `POST /rooms`
- `GET /rooms/{id}`
- valid `POST /sensors`
- invalid `POST /sensors` with missing `roomId` dependency
- sensor filtering with `?type=...`
- `POST /sensors/{id}/readings`
- `GET /sensors/{id}/readings`
- `403 Forbidden` for a maintenance sensor
- `409 Conflict` for deleting a room with linked sensors
- clean `500 Internal Server Error` with no stack trace exposed

---

## 🔗 GitHub Repository

Public GitHub repository link:

```text
https://github.com/SteffaniSilva/SmartCampusAPI.git
```

---

## Notes

- This project only uses **JAX-RS**, as required by the coursework.
- No database was used.
- All data is stored in memory using Java collections.
- The API was tested using Postman.
