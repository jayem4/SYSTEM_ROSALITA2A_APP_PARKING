package pts;

import java.sql.*;

public class DatabaseUtils {

    private static final String DATABASE_URL = "jdbc:sqlite:Parking.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    public static void createTablesIfNotExists(Connection conn) throws SQLException {
        // Create vehicles table if it doesn't exist
        String createVehiclesTable = "CREATE TABLE IF NOT EXISTS vehicles ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "license_plate TEXT NOT NULL, "
                + "model TEXT NOT NULL, "
                + "owner_name TEXT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createVehiclesTable);
        }

        // Create parking_tickets table if it doesn't exist
        String createParkingTicketsTable = "CREATE TABLE IF NOT EXISTS parking_tickets ("
                + "ticket_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "vehicle_id INTEGER NOT NULL, "
                + "issue_time TEXT NOT NULL, "
                + "fine_amount REAL NOT NULL, "
                + "status TEXT DEFAULT 'unpaid', "
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicles(id))";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createParkingTicketsTable);
        }

        // Create ticket_history table if it doesn't exist (for tracking ticket payments)
        String createTicketHistoryTable = "CREATE TABLE IF NOT EXISTS ticket_history ("
                + "history_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ticket_id INTEGER NOT NULL, "
                + "payment_time TEXT NOT NULL, "
                + "amount_paid REAL NOT NULL, "
                + "FOREIGN KEY (ticket_id) REFERENCES parking_tickets(ticket_id))";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTicketHistoryTable);
        }
    }
}
