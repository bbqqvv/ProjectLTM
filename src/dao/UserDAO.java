package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import database.DatabaseConnection;
import model.User;

public class UserDAO {

    // Hash mật khẩu bằng SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, email);
            statement.setString(2, hashPassword(password)); // Hash mật khẩu

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("fullname"), // Sửa từ "name" thành "fullname"
                    resultSet.getString("email"),
                    resultSet.getString("password"), // Lưu ý: Không nên trả về mật khẩu
                    resultSet.getString("image_profile"),
                    resultSet.getString("role"),
                    resultSet.getString("bio"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at"),
                    resultSet.getTimestamp("last_login_at"),
                    resultSet.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean registerUser(String email, String password) {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, email);
            statement.setString(2, hashPassword(password)); // Hash mật khẩu
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}