package network;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import dao.EmailDao;
import dao.SessionDao; // Thêm import cho SessionDao
import model.Email;
import view.EmailManagementView;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailReceiver {
    private static final Logger logger = Logger.getLogger(EmailReceiver.class.getName());
    
    private EmailManagementView emailManagementView;
    private EmailDao emailDao;
    private SessionDao sessionDao; // Thêm trường SessionDao
    private String host;
    private String username;
    private String password;
    private UdpNotifier udpNotifier;
    private boolean running = true;
    private Set<String> receivedEmailSubjects = new HashSet<>();
    private final int CHECK_INTERVAL = 60000; // Kiểm tra mỗi 60 giây

    public EmailReceiver(EmailManagementView emailManagementView, EmailDao emailDao, SessionDao sessionDao, 
            String host, String username, String password, UdpNotifier udpNotifier) {
this.emailManagementView = emailManagementView;
this.emailDao = emailDao;
this.sessionDao = sessionDao;
this.host = host;
this.username = username;
this.password = password;
this.udpNotifier = udpNotifier;

if (this.sessionDao == null) {
System.err.println("SessionDao is null in EmailReceiver constructor!");
}
}
    public void startReceivingEmails() {
        new Thread(() -> {
            while (running) {
                try {
                    receiveEmailsViaIMAP();
                    Thread.sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Email receiver thread interrupted: ", e);
                    running = false;
                }
            }
        }).start();
    }

    public void stopReceivingEmails() {
        running = false;
    }

    private void receiveEmailsViaIMAP() {
        try (Store store = connectToMailServer()) {
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            for (Message message : messages) {
                Email email = createEmailFromMessage(message);
                if (email != null && receivedEmailSubjects.add(email.getSubject())) {
                    emailManagementView.addEmail(email);
                    
                    // Bỏ qua việc lấy địa chỉ IP
                    String senderEmail = getEmailFromUserId(email.getSenderId());
                    
                    // Gửi thông báo mà không cần địa chỉ IP
                    udpNotifier.sendNotification("Received email from: " + senderEmail);

                    emailManagementView.refreshEmailList();
                }
            }

            emailFolder.close(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error receiving emails: ", e);
            emailManagementView.showMessage("Error receiving emails: " + e.getMessage());
        }
    }

    private Store connectToMailServer() throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore("imap");
        store.connect(username, password);
        return store;
    }

    private Email createEmailFromMessage(Message message) throws Exception {
        String subject = message.getSubject();
        String from = message.getFrom()[0].toString();
        String body = getBodyFromMimeMessage(message);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        int senderId = getUserIdFromEmail(from);
        return new Email(senderId, username, subject, body, timestamp, false);
    }

    private String getBodyFromMimeMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            return getBodyFromMimeMultipart((MimeMultipart) message.getContent());
        }
        return "";
    }

    private String getBodyFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getBodyFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    private int getUserIdFromEmail(String email) {
        // Logic để lấy ID người dùng từ email
        return 1; // Thay đổi theo cách bạn lưu trữ ID người dùng
    }

    private String getEmailFromUserId(int userId) {
        // Logic để lấy email từ ID người dùng
        return "example@example.com"; // Thay đổi theo cách bạn lưu trữ email
    }
}