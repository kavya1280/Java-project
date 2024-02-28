import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    static Connection con = null;
    static Statement st;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db?characterEncoding=utf8", "root",
                    "");

            createTables(); // Creating necessary tables if not exists

            int choice;
            do {
                System.out.println("===== Hospital Management System =====");
                System.out.println("1. Doctor Registration");
                System.out.println("2. Patient Registration");
                System.out.println("3. Doctor Login");
                System.out.println("4. Patient Login");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        doctorRegistration();
                        break;
                    case 2:
                        patientRegistration();
                        break;
                    case 3:
                        doctorLogin();
                        break;
                    case 4:
                        patientLogin();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            try {
                if (con != null)
                    con.close();
                if (st != null)
                    st.close();
                if (sc != null)
                    sc.close();
            } catch (SQLException e) {
                System.out.println("Error in closing resources: " + e);
            }
        }
    }

    // Method to create necessary tables in the database
    static void createTables() throws SQLException {
        st = con.createStatement();
        String doctorTableQuery = "CREATE TABLE IF NOT EXISTS Doctor (username VARCHAR(50), PASSWORD VARCHAR(50), Address VARCHAR(100), Specialty VARCHAR(100), Contactno VARCHAR(15))";
        String patientTableQuery = "CREATE TABLE IF NOT EXISTS Patient (Pname VARCHAR(50), PASSWORD VARCHAR(50), Address VARCHAR(100), Age INT, Contactno VARCHAR(15))";
        String appointmentTableQuery = "CREATE TABLE IF NOT EXISTS Appointment (AppointmentID INT AUTO_INCREMENT PRIMARY KEY, DoctorUsername VARCHAR(50), PatientName VARCHAR(50), AppointmentDate DATE)";

        st.executeUpdate(doctorTableQuery);
        st.executeUpdate(patientTableQuery);
        st.executeUpdate(appointmentTableQuery);
    }

    // Method for doctor registration
    static void doctorRegistration() throws SQLException {
        System.out.println("===== Doctor Registration =====");
        System.out.println("Enter your username: ");
        String username = sc.nextLine();
        System.out.println("Enter your PASSWORD: ");
        String password = sc.nextLine();
        System.out.println("Enter your Address : ");
        String address = sc.nextLine();
        System.out.println("Enter your Specialty : ");
        String specialty = sc.nextLine();
        System.out.println("Enter your Contactno: ");
        String contactNo = sc.nextLine();

        String query = "INSERT INTO Doctor(username, PASSWORD, Address, Specialty, Contactno) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, address);
        pstmt.setString(4, specialty);
        pstmt.setString(5, contactNo);
        pstmt.executeUpdate();
        System.out.println("Doctor registered successfully!");
    }

    // Method for patient registration
    static void patientRegistration() throws SQLException {
        System.out.println("===== Patient Registration =====");
        System.out.println("Enter your Username: ");
        String username = sc.nextLine();
        System.out.println("Enter your PASSWORD: ");
        String password = sc.nextLine();
        System.out.println("Enter your Address : ");
        String address = sc.nextLine();
        System.out.println("Enter your Age : ");
        int age = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter your Contactno: ");
        String contactNo = sc.nextLine(); // Input as string

        String query = "INSERT INTO Patient(Pname, PASSWORD, Address, Age, Contactno) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, address);
        pstmt.setInt(4, age);
        pstmt.setString(5, contactNo);
        pstmt.executeUpdate();
        System.out.println("Patient registered successfully!");
    }

    // Method for doctor login
    static void doctorLogin() throws SQLException {
        System.out.println("===== Doctor Login =====");
        System.out.println("Enter your Username: ");
        String username = sc.nextLine();
        System.out.println("Enter your PASSWORD: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM Doctor WHERE username = ? AND PASSWORD = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Login successful! Welcome Doctor " + username);
            showDoctorAppointments(username);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    // Method to show all appointments for a doctor
    static void showDoctorAppointments(String doctorUsername) throws SQLException {
        String query = "SELECT * FROM Appointment WHERE DoctorUsername = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, doctorUsername);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("===== Appointments =====");
        while (rs.next()) {
            int appointmentID = rs.getInt("AppointmentID");
            String patientName = rs.getString("PatientName");
            Date appointmentDate = rs.getDate("AppointmentDate");
            System.out.println("Appointment ID: " + appointmentID + ", Patient Name: " + patientName
                    + ", Appointment Date: " + appointmentDate);
        }
    }

    // Method for patient login
    static void patientLogin() throws SQLException {
        System.out.println("===== Patient Login =====");
        System.out.println("Enter your Username: ");
        String username = sc.nextLine();
        System.out.println("Enter your PASSWORD: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM Patient WHERE Pname = ? AND PASSWORD = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Login successful! Welcome Patient " + username);
            showAvailableDoctors();
            makeAppointment(username);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    // Method to show all available doctors
    static void showAvailableDoctors() throws SQLException {
        String query = "SELECT * FROM Doctor";
        ResultSet rs = st.executeQuery(query);

        System.out.println("===== Available Doctors =====");
        while (rs.next()) {
            String doctorUsername = rs.getString("username");
            String specialty = rs.getString("Specialty");
            System.out.println("Doctor Username: " + doctorUsername + ", Specialty: " + specialty);
        }
    }

    // Method for making appointment
    static void makeAppointment(String patientName) throws SQLException {
        System.out.println("Enter Doctor's Username for Appointment: ");
        String doctorUsername = sc.nextLine();
        System.out.println("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = sc.nextLine();

        String query = "INSERT INTO Appointment(DoctorUsername, PatientName, AppointmentDate) VALUES (?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, doctorUsername);
        pstmt.setString(2, patientName);
        pstmt.setString(3, appointmentDate);
        pstmt.executeUpdate();
        System.out.println("Appointment booked successfully!");
    }
}
