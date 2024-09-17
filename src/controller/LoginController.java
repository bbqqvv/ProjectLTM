package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.UserDAO;
import model.User;
import view.EmailManagementView;
import view.LoginView;

public class LoginController implements ActionListener {
    private LoginView loginView;
    private UserDAO userDAO;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDAO = new UserDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Đăng Nhập")) {
            login();
        }
    }

    public void login() {
        String email = loginView.getEmail();
        String password = loginView.getPassword();
        User user = userDAO.getUser(email, password);

        if (user != null) {
            loginView.showMessage("Đăng nhập thành công!");
            new EmailManagementView(user); // Mở giao diện quản lý email
            loginView.dispose(); // Đóng giao diện đăng nhập
        } else {
            loginView.showMessage("Tên đăng nhập hoặc mật khẩu không hợp lệ.");
        }
    }
}