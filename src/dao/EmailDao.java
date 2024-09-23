package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.Email;

public class EmailDao {

    public void saveEmail(Email email) throws SQLException {
        if (!isSenderValid(email.getSenderId())) {
            throw new SQLException("Sender ID is invalid.");
        }

        String query = "INSERT INTO emails (sender_id, recipient_email, subject, body, timestamp, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, email.getSenderId());
            preparedStatement.setString(2, email.getRecipientEmail());
            preparedStatement.setString(3, email.getSubject());
            preparedStatement.setString(4, email.getBody());
            preparedStatement.setTimestamp(5, email.getTimestamp());
            preparedStatement.setBoolean(6, email.isRead());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving email: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
        }
    }

    public List<Email> getSentEmailsByUserId(int userId) {
        List<Email> sentEmails = new ArrayList<>();
        String query = "SELECT * FROM emails WHERE sender_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sentEmails.add(mapResultSetToEmail(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sent emails: " + e.getMessage());
        }
        return sentEmails;
    }

    private boolean isSenderValid(int senderId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, senderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking sender validity: " + e.getMessage());
            return false;
        }
    }

    private String getEmailById(int userId) {
        String query = "SELECT email FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving email by user ID: " + e.getMessage());
        }
        return null;
    }

    public List<Email> getEmailsByUserId(int userId) {
        List<Email> emails = new ArrayList<>();
        String query = "SELECT * FROM emails WHERE sender_id = ? OR recipient_email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String userEmail = getEmailById(userId);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                emails.add(mapResultSetToEmail(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving emails by user ID: " + e.getMessage());
        }
        return emails;
    }

    // Phương thức riêng để ánh xạ ResultSet thành đối tượng Email
    private Email mapResultSetToEmail(ResultSet resultSet) throws SQLException {
        return new Email(
            resultSet.getInt("id"),
            resultSet.getInt("sender_id"),
            resultSet.getString("recipient_email"),
            resultSet.getString("subject"),
            resultSet.getString("body"),
            resultSet.getTimestamp("timestamp"),
            resultSet.getBoolean("is_read")
        );
    }
}