package controller;

import model.Email;
import view.ComposeEmailView;
import view.EmailManagementView;
import network.EmailSender;
import dao.EmailDao;
import dao.UserDao;

import java.sql.SQLException;
import java.util.List;

public class EmailController {
    private final EmailManagementView emailManagementView;
    private final EmailSender emailSender;
    private final EmailDao emailDao;
    private final UserDao userDao;

    // Constructor cho EmailManagementView
    public EmailController(EmailManagementView emailManagementView, String host, String username, String password) {
        validateCredentials(username, password);
        this.emailManagementView = emailManagementView; // Khởi tạo emailManagementView
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    // Constructor cho ComposeEmailView
    public EmailController(ComposeEmailView composeEmailView, String host, String username, String password) {
        validateCredentials(username, password);
        this.emailManagementView = composeEmailView.getEmailManagementView(); // Lấy emailManagementView từ ComposeEmailView
        this.emailSender = new EmailSender(host, username, password);
        this.emailDao = new EmailDao();
        this.userDao = new UserDao();
    }

    public void sendEmail(Email email) {
        if (emailSender == null) {
            System.err.println("Error: emailSender is not initialized");
            return;
        }

        try {
            emailSender.sendEmail(email);
            saveEmailIfValidSender(email);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void saveEmailIfValidSender(Email email) {
        if (userDao.isSenderValid(email.getSenderId())) {
            try {
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

	public EmailDao getEmailDao() {
		// TODO Auto-generated method stub
		return emailDao;
	}

}
