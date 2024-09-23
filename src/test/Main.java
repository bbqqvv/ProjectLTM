package test;

import controller.LoginController;
import database.DatabaseConnection;
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo kết nối cơ sở dữ liệu
        DatabaseConnection databaseConnection = new DatabaseConnection();
        
        // Khởi tạo giao diện đăng nhập
        LoginView loginView = new LoginView();
        
        // Khởi tạo controller và liên kết với view và database
        LoginController loginController = new LoginController(loginView);
        
        // Thiết lập action listener cho các nút trong LoginView
        loginView.setLoginButtonActionListener(loginController);
        loginView.setRegisterButtonActionListener(loginController);
        
        // Đảm bảo giao diện hiển thị
        loginView.setVisible(true);
    }
}