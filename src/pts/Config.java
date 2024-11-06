package pts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Config {

    private final Databaseconnection dbConnection = new Databaseconnection();

    // Method to add a record to the database (insert operation)
    public void addRecord(String sql, Object... values) {
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // Method to view records (select operation)
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Print column headers
            for (String header : columnHeaders) {
                System.out.print(header + "\t");
            }
            System.out.println();

            // Print rows of data
            while (rs.next()) {
                for (String colName : columnNames) {
                    System.out.print(rs.getString(colName) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Method to update a record (update operation)
    public void updateRecords(String sql, Object... values) {
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // Method to delete a record (delete operation)
    public void deleteRecords(String sql, Object... values) {
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    // Method to get a single value from the database (for example, retrieving last inserted ID)
    public int getSingleValue(String sql, Object... params) {
        int result = 0;
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);  // Get the first column of the result
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving single value: " + e.getMessage());
        }
        return result;
    }

    // Method to add a new owner to the database
    public void addOwner(VehicleOwners owner) {
        String sql = "INSERT INTO vehicle_owners (owner_name, owner_address, owner_phone, owner_email) VALUES (?, ?, ?, ?)";
        addRecord(sql, owner.getOwnerName(), owner.getOwnerAddress(), owner.getOwnerPhone(), owner.getOwnerEmail());
    }
}
