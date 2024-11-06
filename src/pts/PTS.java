package pts;

import java.util.Scanner;

public class PTS {

    private final Config conf = new Config();

    public void addTicket() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine();

        System.out.print("Enter Vehicle Type: ");
        String vehicleType = scanner.nextLine();

        System.out.print("Enter Fine Amount: ");
        int fineAmount = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Owner Name: ");
        String ownerName = scanner.nextLine();

        System.out.print("Enter Owner Address: ");
        String ownerAddress = scanner.nextLine();

        System.out.print("Enter Owner Phone: ");
        String ownerPhone = scanner.nextLine();

        System.out.print("Enter Owner Email: ");
        String ownerEmail = scanner.nextLine();

        VehicleOwners owner = new VehicleOwners(ownerName, ownerAddress, ownerPhone, ownerEmail);
        conf.addOwner(owner);

        String getOwnerIdSql = "SELECT last_insert_rowid()";
        int ownerId = conf.getSingleValue(getOwnerIdSql);  // Use getSingleValue, which now returns an int

        String ticketSql = "INSERT INTO tickets (license_plate, owner_id, vehicle_type, fine_amount) VALUES (?, ?, ?, ?)";
        conf.addRecord(ticketSql, licensePlate, ownerId, vehicleType, fineAmount);
    }

    public void viewTickets() {
        String sql = "SELECT * FROM tickets";
        String[] headers = {"Ticket ID", "License Plate", "Owner ID", "Vehicle Type", "Fine Amount"};
        String[] columns = {"ticket_id", "license_plate", "owner_id", "vehicle_type", "fine_amount"};
        conf.viewRecords(sql, headers, columns);
    }

    public void updateTicket() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Ticket ID to Update: ");
        int ticketId = scanner.nextInt();

        System.out.print("Enter New Fine Amount: ");
        int newFine = scanner.nextInt();

        String sql = "UPDATE tickets SET fine_amount = ? WHERE ticket_id = ?";
        conf.updateRecords(sql, newFine, ticketId);
    }

    public void deleteTicket() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Ticket ID to Delete: ");
        int ticketId = scanner.nextInt();

        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        conf.deleteRecords(sql, ticketId);
    }

    public static void main(String[] args) {
        PTS pts = new PTS();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Parking Ticketing System ---");
            System.out.println("1. Add Ticket");
            System.out.println("2. View Tickets");
            System.out.println("3. Update Ticket");
            System.out.println("4. Delete Ticket");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    pts.addTicket();
                    break;
                case 2:
                    pts.viewTickets();
                    break;
                case 3:
                    pts.updateTicket();
                    break;
                case 4:
                    pts.deleteTicket();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
