package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

public class SessionDao {
    public String getIpAddressByEmail(String email) {
        String query = "SELECT ip_address FROM sessions WHERE user_id = (SELECT id FROM users WHERE email = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("ip_address");
                } else {
                    System.out.println("No IP address found for email: " + email);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Trả về null nếu không tìm thấy
    }
}