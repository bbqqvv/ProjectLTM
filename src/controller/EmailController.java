package controller;

import java.sql.SQLException;
import java.util.List;

import dao.EmailDao;
import dao.UserDao;
import model.Email;
import network.EmailSender;
import view.ComposeEmailView;
import view.EmailManagementView;

public class EmailController {
    private final EmailManagementView emailManagementView;
    private final EmailSender emailSender;
    private final EmailDao emailDao;
    private final UserDao userDao;

    // Constructor cho EmailManagementView
    public EmailController(EmailManagementView emailManagementView, String host, String username, String password) {
        validateCredentials(username, password);
        this.emailManagementView = emailManagementView;
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    // Constructor cho ComposeEmailView
    public EmailController(ComposeEmailView composeEmailView, String host, String username, String password) {
        validateCredentials(username, password);
        this.emailManagementView = composeEmailView.getEmailManagementView();
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    public boolean sendEmail(Email email, String attachmentPath) {
        if (emailSender == null) {
            System.err.println("Error: emailSender is not initialized");
            return false;
        }

        // Kiểm tra tính hợp lệ của địa chỉ email
        if (!isValidEmail(email.getRecipientEmail())) {
            System.err.println("Error: Invalid recipient email address.");
            return false;
        }

        // Kiểm tra tệp đính kèm
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            System.out.println("Preparing to send email with attachment: " + attachmentPath);
        } else {
            System.out.println("Preparing to send email without attachment.");
        }

        try {
            // Gửi email với tệp đính kèm nếu có
            emailSender.sendEmail(email, attachmentPath);
            saveEmailIfValidSender(email);
            return true; // Thành công
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // Có thể thêm thông báo cho người dùng tại đây
            return false; // Thất bại
        }
    }

    // Phương thức kiểm tra tính hợp lệ của địa chỉ email
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public void saveEmailIfValidSender(Email email) {
        if (userDao.isSenderValid(email.getSenderId())) {
            try {
                System.out.println("Saving email with subject: " + email.getSubject());
                System.out.println("Body: " + email.getBody()); // Ghi log body
                emailDao.saveEmail(email);
                System.out.println("Email đã được gửi và lưu vào cơ sở dữ liệu.");
            } catch (SQLException e) {
                System.err.println("Failed to save email: " + e.getMessage());
            }
        } else {
            System.err.println("Lỗi: ID người gửi không hợp lệ.");
        }
    }

    private void validateCredentials(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be empty.");
        }
    }

    public List<Email> loadEmailsFromDatabase(int userId) {
        return emailDao.getEmailsByUserId(userId);
    }

    public List<Email> loadSentEmailsFromDatabase(int userId) {
        return emailDao.getSentEmailsByUserId(userId); // Gọi phương thức để lấy email đã gửi
    }

    public EmailDao getEmailDao() {
        return emailDao;
    }
}
