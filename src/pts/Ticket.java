
package pts;

import java.sql.*;
import java.util.Scanner;

public class Ticket {

    public static void issueParkingTicket(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Select a vehicle to issue a ticket for:");
        // List registered vehicles
        String query = "SELECT id, license_plate, model, owner_name FROM vehicles";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("+-----+------------------+----------------+----------------+");
            System.out.println("| ID  | License Plate    | Model          | Owner Name     |");
            System.out.println("+-----+------------------+----------------+----------------+");

            while (rs.next()) {
                System.out.printf("| %-3d | %-16s | %-14s | %-14s |\n",
                        rs.getInt("id"),
                        rs.getString("license_plate"),
                        rs.getString("model"),
                        rs.getString("owner_name"));
            }
            System.out.println("+-----+------------------+----------------+----------------+");

            // Select a vehicle
            int vehicleId = getValidVehicleId(conn, scanner);

            // Enter fine amount
            System.out.print("Enter fine amount: ");
            double fineAmount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            // Issue the parking ticket
            String insertTicketQuery = "INSERT INTO parking_tickets (vehicle_id, issue_time, fine_amount) "
                    + "VALUES (?, CURRENT_TIMESTAMP, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(insertTicketQuery)) {
                stmt2.setInt(1, vehicleId);
                stmt2.setDouble(2, fineAmount);
                stmt2.executeUpdate();
            }
            System.out.println("Parking ticket issued successfully!");
        }
    }

    public static void viewTicketHistory(Connection conn) throws SQLException {
        String query = "SELECT pt.ticket_id, v.license_plate, v.model, v.owner_name, pt.issue_time, pt.fine_amount, pt.status, pt.exit_time "
                + "FROM parking_tickets pt JOIN vehicles v ON pt.vehicle_id = v.id";
        try (PreparedStatement stmt = conn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("+-----+------------------+----------------+----------------+-------------------+-------------+--------+-------------------+");
            System.out.println("| ID  | License Plate    | Model          | Owner Name     | Issue Time        | Fine Amount | Status | Exit Time         |");
            System.out.println("+-----+------------------+----------------+----------------+-------------------+-------------+--------+-------------------+");

            while (rs.next()) {
                System.out.printf("| %-3d | %-16s | %-14s | %-14s | %-17s | %-11.2f | %-6s | %-17s |\n",
                        rs.getInt("ticket_id"),
                        rs.getString("license_plate"),
                        rs.getString("model"),
                        rs.getString("owner_name"),
                        rs.getString("issue_time"),
                        rs.getDouble("fine_amount"),
                        rs.getString("status"),
                        rs.getString("exit_time"));
            }
            System.out.println("+-----+------------------+----------------+----------------+-------------------+-------------+--------+-------------------+");
        }
    }

    public static void payTicket(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ticket ID to pay: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the ticket exists and is unpaid
        String query = "SELECT ticket_id, fine_amount FROM parking_tickets WHERE ticket_id = ? AND status = 'unpaid'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Invalid ticket ID or ticket already paid.");
                    return;
                }

                double fineAmount = rs.getDouble("fine_amount");
                System.out.printf("Ticket Fine Amount: %.2f\n", fineAmount);

                // Get payment amount from user
                System.out.print("Enter payment amount: ");
                double payment = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                // Process payment and update ticket status
                if (payment < fineAmount) {
                    System.out.println("Insufficient payment.");
                } else {
                    // Update the ticket status to 'paid'
                    String updateTicketQuery = "UPDATE parking_tickets SET status = 'paid' WHERE ticket_id = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(updateTicketQuery)) {
                        stmt2.setInt(1, ticketId);
                        stmt2.executeUpdate();
                    }

                    // Record payment in ticket_history
                    String insertPaymentHistoryQuery = "INSERT INTO ticket_history (ticket_id, payment_time, amount_paid) "
                            + "VALUES (?, CURRENT_TIMESTAMP, ?)";
                    try (PreparedStatement stmt3 = conn.prepareStatement(insertPaymentHistoryQuery)) {
                        stmt3.setInt(1, ticketId);
                        stmt3.setDouble(2, payment);
                        stmt3.executeUpdate();
                    }

                    System.out.println("Ticket paid successfully!");
                }
            }
        }
    }

    // Helper method to get a valid vehicle ID
    private static int getValidVehicleId(Connection conn, Scanner scanner) {
        System.out.print("Enter vehicle ID: ");
        int vehicleId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate that the vehicle exists
        String query = "SELECT id FROM vehicles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Invalid vehicle ID. Please try again.");
                    return getValidVehicleId(conn, scanner);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error querying vehicle ID: " + e.getMessage());
        }

        return vehicleId;
    }
}
