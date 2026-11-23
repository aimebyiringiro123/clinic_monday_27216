Clinic Management System

A REST API for managing a medical clinic: patients, doctors, offices, specializations, appointments and prescriptions. Built with Spring Boot, Spring Data JPA (Hibernate) and JPQL, with a PostgreSQL database.

This project was built step by step to practise three things:

Technology	Role in the project
Spring Boot	Runs the web server and maps URLs to Java methods
JPA / Hibernate	Maps Java classes (entities) to database tables
JPQL	Queries written against Java classes and fields instead of tables and columns
Tech stack
Java 17+
Spring Boot 4.x (Spring Web, Spring Data JPA)
Hibernate 7
PostgreSQL
Lombok
Maven
Postman (API testing)
Architecture

The project uses four layers. A request never skips a layer.

Postman / Browser
      |
 Controller   -> receives HTTP requests, chooses the HTTP status
      |
  Service     -> business rules, returns a message / list / Optional
      |
 Repository   -> database access (derived queries and JPQL)
      |
  Database

Package layout (kigali.clinick.com.clinickmanagentsystem):

model/        entities and the AppointmentStatus enum
repository/   Spring Data repositories (derived queries + @Query JPQL)
services/     business logic
controller/   REST endpoints

Code conventions:

Entities are used directly as request and response bodies (no DTOs).
Services return a List, an Optional or a String message and do not throw exceptions.
Controllers map the service message to an HTTP status with if/else.
Data model
Entity	Fields
Patient	id, firstName, lastName, ssNumber (unique)
Doctor	id, firstName, lastName, office
Office	id, name, number, location
Specialization	id, name
Appointment	id, appointmentDate, status, reason, patient, doctor
Prescription	id (UUID), medicineName, dosage, durationDays, issuedDate, appointment

AppointmentStatus: SCHEDULED, CONFIRMED, COMPLETED, CANCELLED

Relationships
Relationship	Type	Owning side
Patient - Appointment	One-to-Many	Appointment (patient_id)
Doctor - Appointment	One-to-Many	Appointment (doctor_id)
Doctor - Office	One-to-One	Doctor (office_id)
Doctor - Specialization	Many-to-Many	Doctor (@JoinTable, table doctor_specialization)
Appointment - Prescription	One-to-Many	Prescription (appointment_id)

The owning side is the entity whose table stores the foreign key (or defines the join table). The other side uses mappedBy. Changes to a many-to-many link are only written when made on the owning side, so the doctor's list is modified and the doctor is saved.

Getting started
Prerequisites
JDK 17 or higher
Maven
PostgreSQL running locally
Configuration

Create a PostgreSQL database (for example clinick_management_db) and set src/main/resources/application.properties:

properties
server.port=8003

spring.datasource.url=jdbc:postgresql://localhost:5432/clinick_management_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.error.include-message=always
Run
bash
mvn spring-boot:run

The API is available at http://localhost:8003.

API reference

Base URL: http://localhost:8003/api

CRUD

Patients, doctors, offices, specializations and appointments expose standard create and read endpoints (for example POST /api/patients/save, GET /api/patients/getAll). Appointments also support read one, update and delete.

Part A: doctors and offices
ID	Method and path	Description	Query type
A1	PUT /doctors/{doctorId}/specializations/{specializationId}	Add a specialization to a doctor (404, 409, 200)	Many-to-many list add, save owner
A2	DELETE /doctors/{doctorId}/specializations/{specializationId}	Remove only the link (404, 409, 200)	Many-to-many list remove, save owner
A3	GET /offices/free	Offices no doctor occupies, sorted by number	JPQL (NOT EXISTS)
A4	PATCH /doctors/{doctorId}/office?officeNumber=104	Move a doctor to another office (404, 409, 200)	Business rule + derived existsBy
Part B: appointment rules
ID	Method and path	Description	Query type
B1	POST /appointments/save	No past dates; new appointments always start as SCHEDULED	Business rule
B2	PATCH /appointments/{id}/status?status=CONFIRMED	Status changes only along allowed moves	Business rule (enum ==)
B3	PATCH /appointments/{id}/reschedule?newDate=2026-10-30	Reschedule with several checks (404, 400, 409)	Derived (existsBy, And, StatusNot)

Allowed status moves:

Current status	Allowed new status
SCHEDULED	CONFIRMED, CANCELLED
CONFIRMED	COMPLETED, CANCELLED
COMPLETED, CANCELLED	none (final)
Part C: prescriptions
ID	Method and path	Description	Query type
C1	POST /prescriptions/save?appointmentId=4	Write a prescription (only for COMPLETED appointments)	New entity, @ManyToOne, rule
C2	GET /prescriptions/appointment/{appointmentId}	Prescriptions of an appointment	Derived (findBy)
C3	GET /prescriptions/patient/{ssNumber}	Prescription history of a patient, newest first	JPQL (path navigation)
C4	PUT /prescriptions/update/{id}, DELETE /prescriptions/delete/{id}	Update, delete, and protect appointments that have prescriptions	CRUD + derived existsBy
Part D: agenda and reports
ID	Method and path	Description	Query type
D1	GET /doctors/{doctorId}/agenda?date=2026-10-20	Doctor's agenda for a day, without cancelled ones	Derived (And, StatusNot)
D2	GET /patients/{ssNumber}/upcoming	Upcoming SCHEDULED or CONFIRMED appointments	Derived (GreaterThanEqual, In, OrderBy)
D3	GET /doctors/search?name=gis	Search doctors by first or last name	JPQL (LIKE, LOWER, OR)
D4	GET /dashboard/summary	Clinic counters as one JSON object	count(), derived countBy, reuses A3
Bonus
Method and path	Description	Query type
GET /prescriptions/stats/top-medicines	Most prescribed medicines	JPQL (GROUP BY, COUNT, ORDER BY)
GET /doctors/workload?start=2026-10-01&end=2026-10-31	Completed appointments per doctor in a date range	JPQL (JOIN, BETWEEN, GROUP BY)
HTTP status codes used
Code	Meaning in this project
200	Success
201	Created
400	Business rule violated or invalid input
404	Doctor, patient, office, appointment or prescription not found
409	Conflict (duplicate link, office taken, doctor already booked, appointment has prescriptions)
Example requests

Create a prescription:

http
POST /api/prescriptions/save?appointmentId=4
Content-Type: application/json

{
  "medicineName": "Amoxicillin",
  "dosage": "1 tablet, 3 times a day",
  "durationDays": 7
}

Response: 201 Created

Prescription saved successfully

Prescribe for a non-completed appointment: 400 Bad Request

Prescriptions are only for COMPLETED appointments
Key concepts demonstrated
Derived queries: Spring builds the query from the method name (findByStatusOrderByAppointmentDateAsc, existsByDoctorIdAndAppointmentDateAndStatusNot, countByStatus).
JPQL with @Query: JOIN, NOT EXISTS subqueries, LIKE, GROUP BY, HAVING-style aggregation, path navigation without a join.
Many-to-many handling: owning side, join table and @Transactional with lazy loading.
Business rules in the service layer: status flow, safe booking, rescheduling, protection against deleting appointments that have prescriptions.
Consistent error handling: services return messages and controllers translate them into HTTP statuses.
Testing

All endpoints were tested in Postman, with at least one success and one failing case (400, 404 or 409) per requirement. The Postman collection is included in the repository root as clinic-quiz2.postman_collection.json, with one request per requirement named by its ID (A1, A2, ... D4).


Author

Byiringiro Ingabire Aime - WEB-TECHNOLOGIES / AUCA
