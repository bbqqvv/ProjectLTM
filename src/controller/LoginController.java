package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import model.Email;
import model.User;
import network.EmailSender;
import until.UserIpMapping;
import view.EmailManagementView;
import view.LoginView;
import view.RegisterView;
import dao.UserDao;

public class LoginController implements ActionListener {
    private LoginView loginView;
    private UserDao userDao;
//    private UserIpMapping userIpMapping; // Thêm trường UserIpMapping

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDao = new UserDao();
//        this.userIpMapping = new UserIpMapping(); // Khởi tạo UserIpMapping

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Đăng Nhập")) {
            handleLogin();
        } else if (command.equals("Đăng Ký")) {
            handleRegister();
        }
    }

    
    
    private void handleLogin() {
        String email = loginView.getEmail();
        String password = loginView.getPassword();

        // Kiểm tra thông tin không trống
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Email và mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(loginView, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Xác thực tài khoản Gmail
        if (!authenticate(email, password)) {
            JOptionPane.showMessageDialog(loginView, "Sai email hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = new User(email,password);
        // Nếu xác thực thành công, kiểm tra người dùng trong cơ sở dữ liệu
        User user1 = userDao.getUser(email, password); // Lấy thông tin người dùng từ DB

        // Lấy địa chỉ IP và tạo token phiên làm việc
        String userIpAddress = getUserIpAddress();
        String sessionToken = generateSessionToken(); // Hàm tạo token phiên

        // Lưu phiên làm việc cùng với địa chỉ IP
      userDao.saveSession(user1.getId(), sessionToken, userIpAddress); // Lưu thông tin phiên
//      userIpMapping.addMapping(email, userIpAddress);

        JOptionPane.showMessageDialog(loginView, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        // Khởi tạo EmailManagementView với đúng thông tin
        new EmailManagementView(user);
        loginView.dispose(); // Đóng giao diện đăng nhập
    }




    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean authenticate(String email, String password) {
        try {
            EmailSender emailSender = new EmailSender("smtp.gmail.com", email, password);
            // Gửi một email thử nghiệm để xác thực
            Email testEmail = new Email();
            testEmail.setRecipientEmail(email);
            testEmail.setSubject("Test Email");
            testEmail.setBody("This is a test email to verify authentication.");

            // Gọi sendEmail với tham số tệp đính kèm là null
            emailSender.sendEmail(testEmail, null); // Truyền đường dẫn tệp đính kèm là null
            return true; // Nếu không có exception, xác thực thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getUserIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress(); // Lấy địa chỉ IP local
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String generateSessionToken() {
        // Hàm tạo token phiên, bạn có thể sử dụng UUID hoặc phương pháp khác
        return java.util.UUID.randomUUID().toString();
    }
    private void handleRegister() {
        // Mở giao diện đăng ký
        new RegisterView();
        loginView.dispose(); // Đóng giao diện đăng nhập
    }
 


}
