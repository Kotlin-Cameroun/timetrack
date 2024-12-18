# TimeTrack - Backend (Spring Boot)

## Project Structure

The project is organized into multiple packages to clearly separate responsibilities and improve maintainability. Below is a detailed explanation of each package and file.


### 1. **config**  
This package contains all the necessary configurations for securing and managing the project.

- **JwtAuthFilter**:  
  A filter to intercept and verify JWT tokens in every HTTP request. It ensures that only authenticated requests are allowed.

- **JwtService**:  
  Service for generating, validating, and extracting information from JWT tokens. It handles the core logic related to authentication using JWT.

- **SecurityConfig**:  
  Spring Boot security configuration. Defines secured routes, filters, and authentication strategies.


### 2. **controllers**  
This package contains the controllers that handle HTTP requests for the main entities.

- **AttendanceController**:  
  Controller to manage operations related to employee attendance (create, update, retrieve, etc.).

- **EmployeeController**:  
  Controller to manage operations related to employees (create, update, retrieve, delete).


### 3. **dto** (Data Transfer Object)  
This package contains DTO classes used to transfer data between the client and the server.

- **CreateAttendanceDTO**:  
  DTO used to create a new attendance entry.

- **EmployeeRequest.kt**:  
  DTO for requests related to creating or updating an employee.

- **UpdateAttendanceDTO**:  
  DTO used to update an existing attendance entry.


### 4. **exception**  
This package contains classes for globally handling exceptions in the project.

- **GlobalExceptionHandler**:  
  Handles all exceptions in a centralized manner. Provides consistent and appropriate error responses for clients.


### 5. **models**  
This package contains the main database entities.

- **Attendance**:  
  Entity representing an attendance record with attributes such as the ID, dates, and links to employees.

- **Employee**:  
  Entity representing an employee with details like ID, name, and other relevant information.


### 6. **repository**  
This package contains repository interfaces to access data in the database.

- **AttendanceRepository**:  
  Interface to handle CRUD operations for the **Attendance** entity.

- **EmployeeRepository**:  
  Interface to handle CRUD operations for the **Employee** entity.


### 7. **services**  
This package contains services that manage business logic.

- **AttendanceService**:  
  Service to manage business logic related to employee attendance.

- **EmployeeService**:  
  Service to manage business logic related to employees.

- **TimetrackApplication.kt**:  
  The main entry point for the Spring Boot application.


### 8. **resources**  
The **resources** package contains static files and configuration files necessary for the project.

- **static**:  
  Directory for storing static files, if any (images, scripts, etc.).



## Key Project Highlights

1. **Architecture**:  
   The project follows a clear MVC (Model-View-Controller) architecture.  
   - **Controllers** handle HTTP requests.  
   - **Services** contain business logic.  
   - **Repositories** manage data access.

2. **Security**:  
   Authentication and authorization are implemented using JWT with the **JwtAuthFilter** and **JwtService** classes.

3. **Error Handling**:  
   A global exception handler ensures clean and consistent error messages for clients.

---

## Running the Project

1. Clone the project:  
   ```bash
   git clone https://github.com/Kotlin-Cameroun/timetrack.git
   ```

2. Configure your PostgreSQL database in the **application.properties** file.

3. Start the application:  
   ```bash
   ./gradlew bootRun
   ```

4. The API will be accessible at:  
   ```
   http://localhost:8082
   ```


## Authors

- **[@Icode]** - Lead Developer & Business Optimization  
- **Kotlin Community Cameroon**
