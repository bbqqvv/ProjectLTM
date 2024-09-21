package network;

import model.Email;
import view.EmailManagementView;
import dao.EmailDao;

import javax.mail.*;
import java.sql.Timestamp;
import java.util.Properties;

public class EmailReceiver {
    private EmailManagementView emailManagementView;
    private EmailDao emailDao;
    private String host;
    private String username;
    private String password;
    private static final int MAX_EMAILS = 50; // Giới hạn số lượng email nhận

    public EmailReceiver(EmailManagementView emailManagementView, EmailDao emailDao, String host, String username, String password) {
        this.emailManagementView = emailManagementView;
        this.emailDao = emailDao;
        this.host = host;  // IMAP host (ví dụ: "imap.gmail.com")
        this.username = username;  // Địa chỉ email
        this.password = password;  // Mật khẩu ứng dụng (được tạo từ Google)
    }

    public void receiveEmailsViaIMAP() {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.ssl.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imap");
            store.connect(username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            int emailCount = Math.min(messages.length, MAX_EMAILS); // Giới hạn số lượng email

            for (int i = messages.length - emailCount; i < messages.length; i++) {
                Email email = createEmailFromMessage(messages[i]);
                emailDao.saveEmail(email);  // Lưu email vào cơ sở dữ liệu
                emailManagementView.addEmail(email);  // Hiển thị email lên giao diện
            }

            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
            emailManagementView.showMessage("Error receiving emails: " + e.getMessage());
        }
    }

    private Email createEmailFromMessage(Message message) throws Exception {
        String subject = message.getSubject();
        String from = message.getFrom()[0].toString();
        String content = message.getContent().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Giả định senderId là id của người dùng hiện tại (cần lấy từ User)
        int senderId = getUserIdFromEmail(from); // Một phương thức bạn cần định nghĩa

        return new Email(senderId, username, subject, content, timestamp, false);
    }

    private int getUserIdFromEmail(String email) {
        // Xử lý logic để lấy ID người dùng từ email
        // Trả về ID giả định cho ví dụ
        return 1; // Thay đổi theo cách bạn lưu trữ ID người dùng
    }
}
