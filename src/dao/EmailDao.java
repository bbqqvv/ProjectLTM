package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.Email; // Giả định bạn đã tạo lớp Email

public class EmailDao {

	public void saveEmail(int senderId, String recipientEmail, String subject, String body, Timestamp timestamp, boolean isRead) throws SQLException {
	    String query = "INSERT INTO emails (sender_id, recipient_email, subject, body, timestamp, is_read) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        
	        preparedStatement.setInt(1, senderId);
	        preparedStatement.setString(2, recipientEmail);
	        preparedStatement.setString(3, subject);
	        preparedStatement.setString(4, body);
	        preparedStatement.setTimestamp(5, timestamp);
	        preparedStatement.setBoolean(6, isRead);
	        preparedStatement.executeUpdate();
	    }
	}


	public List<Email> getEmailsByUserId(int userId) {
	    List<Email> emails = new ArrayList<>();
	    String query = "SELECT * FROM emails WHERE sender_id = ? OR recipient_email = ?";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	         
	        // Fetch the user's email based on userId
	        String userEmail = getUserEmailById(userId); // Assuming you have a method to get the email

	        preparedStatement.setInt(1, userId);
	        preparedStatement.setString(2, userEmail); 

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            Email email = new Email(
	                resultSet.getInt("sender_id"),
	                resultSet.getString("recipient_email"),
	                resultSet.getString("subject"),
	                resultSet.getString("body"),
	                resultSet.getTimestamp("timestamp"),
	                resultSet.getBoolean("is_read")
	            );
	            emails.add(email);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return emails;
	}

	// Example method to fetch user's email by user ID (you can modify this based on actual DB structure)
	private String getUserEmailById(int userId) {
	    // Retrieve user's email from database based on userId (example implementation)
	    String email = null;
	    String query = "SELECT email FROM users WHERE id = ?";
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        
	        preparedStatement.setInt(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            email = resultSet.getString("email");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return email;
	}

    public void saveAttachment(int emailId, String fileName, String filePath) {
        String query = "INSERT INTO attachments (email_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, emailId);
            stmt.setString(2, fileName);
            stmt.setString(3, filePath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}