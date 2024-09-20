package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User getUser(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Kiểm tra kết nối cơ sở dữ liệu
            if (connection == null) {
                System.err.println("Không thể kết nối tới cơ sở dữ liệu.");
                return null;
            }

            statement.setString(1, email);
            statement.setString(2, hashPassword(password)); // Hash mật khẩu

            // In ra các giá trị đang được truy vấn
            System.out.println("Email: " + email);
            System.out.println("Hashed Password: " + hashPassword(password));

            ResultSet resultSet = statement.executeQuery();

            // Kiểm tra số lượng hàng trả về
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("fullname"),
                    resultSet.getString("email"),
                    resultSet.getString("password"), // Chú ý: Không nên trả mật khẩu trong đối tượng User
                    resultSet.getString("image_profile"),
                    resultSet.getString("role"),
                    resultSet.getString("bio"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at"),
                    resultSet.getTimestamp("last_login_at"),
                    resultSet.getBoolean("is_active")
                );
            }

            System.out.println("Number of rows returned: " + rowCount);

        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Lỗi bất thường: " + e.getMessage());
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

    public boolean registerUser(String email, String password) {
        if (isEmailExists(email)) {
            System.err.println("Email đã tồn tại.");
            return false; // Không thể đăng ký vì email đã tồn tại
        }

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
    
    public boolean isSenderValid(int senderId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setInt(1, senderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Trả về true nếu sender_id tồn tại
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
            stmt.setString(3, user.getPassword()); // Hash password if necessary
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
}