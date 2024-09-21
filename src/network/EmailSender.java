package network;

import model.Email;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private String host;
    private String username;
    private String password;

    public EmailSender(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password; // This should be the app-specific password
    }

    public void sendEmail(Email email) {
        try {
            Properties props = createEmailProperties();
            Session session = createEmailSession(props);

            Message message = createEmailMessage(session, email);
            Transport.send(message);
            System.out.println("Email sent to " + email.getRecipientEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Properties createEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Session createEmailSession(Properties props) {
        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Message createEmailMessage(Session session, Email email) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipientEmail()));
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        return message;
    }
}
