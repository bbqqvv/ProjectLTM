package database;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3308/mail_udp"; // Thay đổi URL nếu cần
    private static final String USER = "root"; // Tên người dùng
    private static final String PASSWORD = "Vanbui262004@"; // Mật khẩu

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}