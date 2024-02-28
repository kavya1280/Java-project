# Hospital Management System

## Introduction
The Hospital Management System is a Java-based application designed to streamline administrative tasks in a hospital setting. It provides functionalities for doctor and patient registration, appointment scheduling, and managing patient records.

## Getting Started
To run the Hospital Management System, follow these steps:

1. Install Java Development Kit (JDK) if not already installed.
2. Set up a MySQL database by following these steps:
   - Install MySQL if not already installed.
   - Create a new database named `hospital_db`.
   - Create the necessary tables using the following SQL queries:
     ```sql
     CREATE TABLE IF NOT EXISTS Doctor (
         username VARCHAR(50),
         PASSWORD VARCHAR(50),
         Address VARCHAR(100),
         Specialty VARCHAR(100),
         Contactno VARCHAR(15)
     );

     CREATE TABLE IF NOT EXISTS Patient (
         Pname VARCHAR(50),
         PASSWORD VARCHAR(50),
         Address VARCHAR(100),
         Age INT,
         Contactno VARCHAR(15)
     );

     CREATE TABLE IF NOT EXISTS Appointment (
         AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
         DoctorUsername VARCHAR(50),
         PatientName VARCHAR(50),
         AppointmentDate DATE
     );
     ```
   - Ensure to configure the connection parameters in the code (`HospitalManagementSystem.java`).
3. Compile the Java source files using `javac HospitalManagementSystem.java`.
4. Run the compiled program using `java HospitalManagementSystem`.

## Usage
Upon running the program, you will be presented with a menu where you can choose different options:

1. Doctor Registration: Register a new doctor.
2. Patient Registration: Register a new patient.
3. Doctor Login: Log in as a doctor to view appointments.
4. Patient Login: Log in as a patient to book appointments.
5. Exit: Exit the program.



