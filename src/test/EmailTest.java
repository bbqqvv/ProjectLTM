package test;

import model.Email;
import network.EmailSender;

public class EmailTest {
    public static void main(String[] args) {
        // Thay thế thông tin tài khoản của bạn
        String host = "smtp.gmail.com";
        String username = "vanbui0966467356@gmail.com"; // Địa chỉ email của bạn
        String password = "juwh mgto thzf wlmp";

        // Tạo đối tượng EmailSender
        EmailSender emailSender = new EmailSender(host, username, password);

        // Tạo một email đơn giản để gửi
        Email testEmail = new Email();
        testEmail.setRecipientEmail("vanbq.22itb@vku.udn.vn"); // Địa chỉ email người nhận
        testEmail.setSubject("Test Email");
        testEmail.setBody("This is a test email sent from Java.");

        try {
            // Gửi email
            emailSender.sendEmail(testEmail);
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }
}