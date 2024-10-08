package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseConnection;
import model.User;

public class UserDao {

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

    public boolean saveUser(User user) {
        if (isEmailExists(user.getEmail())) {
            System.err.println("Email đã tồn tại.");
            return false;
        }

        String query = "INSERT INTO users (fullname, email, password, image_profile, role, bio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, user.getFullname());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword())); // Hash mật khẩu
            statement.setString(4, user.getImageProfile());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getBio());
            
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, email);
            statement.setString(2, hashPassword(password)); // Hash mật khẩu

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                	    resultSet.getInt("id"),
                        resultSet.getString("fullname"),
                        resultSet.getString("email"),
                        null, // Don't return password for security reasons
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

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Trả về true nếu email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET fullname = ?, email = ?, password = ?, bio = ?, image_profile = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullname());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword())); // Hash mật khẩu
            stmt.setString(4, user.getBio());
            stmt.setString(5, user.getImageProfile());
            stmt.setInt(6, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSenderValid(int senderId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, senderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean exists = resultSet.getInt(1) > 0;
                if (!exists) {
                    System.err.println("Lỗi: Người gửi với ID " + senderId + " không tồn tại.");
                }
                return exists;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users"; // Make sure 'users' table has 'ip_address' field
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("fullname"),
                    resultSet.getString("email"),
                    null, // Don't return password for security reasons
                    resultSet.getString("image_profile"),
                    resultSet.getString("role"),
                    resultSet.getString("bio"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at"),
                    resultSet.getTimestamp("last_login_at"),
                    resultSet.getBoolean("is_active")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    
    public void saveSession(int userId, String sessionToken, String ipAddress) {
        if (isUserExists(userId)) {
            String sql = "INSERT INTO sessions (user_id, session_token, ip_address) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, userId);
                stmt.setString(2, sessionToken);
                stmt.setString(3, ipAddress);
                stmt.executeUpdate();
                System.out.println("Session saved successfully for user ID: " + userId);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to save session for user ID " + userId + ": " + e.getMessage());
            }
        } else {
            System.err.println("User ID " + userId + " không tồn tại, không thể lưu session.");
        }
    }

    public boolean isUserExists(int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // True nếu user tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tồn tại
    }



    public boolean registerUser(String email, String password) {
        User newUser = new User(email, password);
        return saveUser(newUser);
    }

    public User getUserById(int senderId) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, senderId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("fullname"),
                    resultSet.getString("email"),
                    null, // Không trả về mật khẩu vì lý do bảo mật
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
    
    
    public String getEmailById(int userId) {
        String email = null;
        String query = "SELECT email FROM users WHERE id = ?"; // Giả sử bảng là 'users' và có cột 'email'

        try (Connection conn = DatabaseConnection.getConnection(); // Kết nối tới cơ sở dữ liệu
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email"); // Lấy email từ kết quả
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi
        }

        return email; // Trả về email hoặc null nếu không tìm thấy
    }

}
