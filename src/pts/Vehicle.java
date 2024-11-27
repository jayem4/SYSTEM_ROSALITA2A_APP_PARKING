package pts;

import java.sql.*;
import java.util.Scanner;

public class Vehicle {

    public static void registerVehicle(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter vehicle license plate: ");
        String licensePlate = scanner.nextLine().trim();
        while (licensePlate.isEmpty() || licensePlate.length() > 20) {
            System.out.println("License plate cannot be empty and must be at most 20 characters.");
            System.out.print("Enter vehicle license plate: ");
            licensePlate = scanner.nextLine().trim();
        }

        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine().trim();
        while (model.isEmpty()) {
            System.out.println("Model cannot be empty.");
            System.out.print("Enter vehicle model: ");
            model = scanner.nextLine().trim();
        }

        System.out.print("Enter owner's name: ");
        String ownerName = scanner.nextLine().trim();
        while (ownerName.isEmpty()) {
            System.out.println("Owner's name cannot be empty.");
            System.out.print("Enter owner's name: ");
            ownerName = scanner.nextLine().trim();
        }

        String insertVehicleQuery = "INSERT INTO vehicles (license_plate, model, owner_name) "
                + "VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertVehicleQuery)) {
            stmt.setString(1, licensePlate);
            stmt.setString(2, model);
            stmt.setString(3, ownerName);
            stmt.executeUpdate();
        }
        System.out.println("Vehicle registered successfully!");
    }
}
