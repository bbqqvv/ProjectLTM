package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.UserDao;
import view.LoginView;
import view.RegisterView;

public class RegisterController implements ActionListener {
    private RegisterView registerView;
    private UserDao userDao;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
        this.userDao = new UserDao(); // Khởi tạo UserDao
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Đã chuyển logic xử lý vào các phương thức riêng
    }

    // Xử lý đăng ký
    public void handleRegister() {
        String email = registerView.getEmail();
        String password = registerView.getPassword();
        String confirmPassword = registerView.getConfirmPassword();

        if (!registerView.validateEmail(email)) {
            registerView.showMessage("Email không hợp lệ!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerView.showMessage("Mật khẩu không khớp!");
            return;
        }

        if (userDao.registerUser(email, password)) {
            registerView.showMessage("Đăng ký thành công!");
            registerView.dispose(); // Đóng giao diện đăng ký
            new LoginView(); // Mở lại giao diện đăng nhập
        } else {
            registerView.showMessage("Đăng ký thất bại. Email có thể đã tồn tại.");
        }
    }

    // Đóng cửa sổ đăng ký
    public void disposeView() {
        registerView.dispose();
        new LoginView(); // Mở lại giao diện đăng nhập
    }
}