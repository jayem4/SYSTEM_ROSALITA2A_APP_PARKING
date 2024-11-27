package pts;

import java.sql.*;
import java.util.Scanner;

public class ParkingSystem {
    private static final String DATABASE_URL = "jdbc:sqlite:Parking.db";

    public static void main(String[] args) {
        try (Connection conn = DatabaseUtils.connect()) {
            // Create tables if they don't exist
            DatabaseUtils.createTablesIfNotExists(conn);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Parking Ticket System");
                System.out.println("1. Register Vehicle");
                System.out.println("2. Issue Parking Ticket");
                System.out.println("3. View Ticket History");
                System.out.println("4. Pay Ticket");
                System.out.println("5. Generate Report");
                System.out.println("6. Exit");

                // Validate the user input for menu choice
                int choice = -1;
                while (choice < 1 || choice > 6) {
                    System.out.print("Select an option: ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();  // Consume the newline character
                        if (choice < 1 || choice > 6) {
                            System.out.println("Invalid choice. Please select a number between 1 and 6.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number between 1 and 6.");
                        scanner.nextLine();  // Consume invalid input
                    }
                }

                switch (choice) {
                    case 1:
                        Vehicle.registerVehicle(conn, scanner);
                        break;
                    case 2:
                        Ticket.issueParkingTicket(conn, scanner);
                        break;
                    case 3:
                        Ticket.viewTicketHistory(conn);
                        break;
                    case 4:
                        Ticket.payTicket(conn, scanner);
                        break;
                    case 5:
                        generateReport(conn);  // Call generateReport method
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        // This case will never be reached due to validation above.
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void generateReport(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) AS total_tickets, SUM(fine_amount) AS total_fines FROM parking_tickets WHERE status = 'paid'";

        try (PreparedStatement stmt = conn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {
            
            // Print header of the table
            System.out.println("+-------------------------+--------------------------+");
            System.out.println("| Total Tickets Paid      | Total Fines Collected    |");
            System.out.println("+-------------------------+--------------------------+");

            if (rs.next()) {
                int totalTickets = rs.getInt("total_tickets");
                double totalFines = rs.getDouble("total_fines");

                // Format the results in the table format
                System.out.printf("| %-23d | %-24.2f |\n", totalTickets, totalFines);
            }

            // Print the footer of the table
            System.out.println("+-------------------------+--------------------------+");
        }
    }
}
