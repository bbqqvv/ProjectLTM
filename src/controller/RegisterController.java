package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.UserDAO;
import view.LoginView;
import view.RegisterView;

public class RegisterController implements ActionListener {
    private RegisterView registerView;
    private UserDAO userDAO;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
        this.userDAO = new UserDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Đăng Ký")) {
            register();
        }
    }

    public void register() {
        String email = registerView.getEmail();
        String password = registerView.getPassword();

        if (userDAO.registerUser(email, password)) {
            registerView.showMessage("Đăng ký thành công!");
            registerView.dispose(); // Đóng giao diện đăng ký
            new LoginView(); // Mở lại giao diện đăng nhập
        } else {
            registerView.showMessage("Đăng ký thất bại. Tên người dùng có thể đã tồn tại.");
        }
    }
}