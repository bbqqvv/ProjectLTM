package controller;

import model.Email;
import view.ComposeEmailView;
import view.EmailManagementView;
import network.EmailSender;
import dao.EmailDao;
import dao.UserDao;

import java.sql.SQLException;

public class EmailController {
    private EmailManagementView emailManagementView;
    private EmailSender emailSender;
    private EmailDao emailDao;
    private UserDao userDao;

    // Constructor used by EmailManagementView
    public EmailController(EmailManagementView emailManagementView, String host, String username, String password) {
        System.out.println("Username: " + username); // Debugging line
        System.out.println("Password: " + password); // Debugging line
        validateCredentials(username, password);
        this.emailManagementView = emailManagementView;
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }
    // Constructor used for sending emails with ComposeEmailView
    public EmailController(ComposeEmailView composeEmailView, String host, String username, String password) {
        validateCredentials(username, password);
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    // Constructor with only credentials
    public EmailController(String host, String username, String password) {
        validateCredentials(username, password);
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    // Method to send email
    public void sendEmail(Email email) {
        if (emailSender == null) {
            System.err.println("Error: emailSender is not initialized");
            return;
        }

        try {
            emailSender.sendEmail(email); // Gửi email
            saveEmailIfValidSender(email); // Kiểm tra và lưu email vào cơ sở dữ liệu
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    // Phương thức kiểm tra và lưu email
    private void saveEmailIfValidSender(Email email) {
        if (userDao.isSenderValid(email.getSenderId())) {
            try {
                emailDao.saveEmail(
                    email.getSenderId(),
                    email.getRecipientEmail(),
                    email.getSubject(),
                    email.getBody(),
                    email.getTimestamp(),
                    email.isRead()
                );
                System.out.println("Email đã được gửi và lưu vào cơ sở dữ liệu.");
            } catch (SQLException e) {
                System.err.println("Failed to save email: " + e.getMessage());
            }
        } else {
            System.err.println("Lỗi: ID người gửi không hợp lệ.");
        }
    }

    // Phương thức kiểm tra thông tin xác thực
    private void validateCredentials(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be empty.");
        }
    }
}