package network;

import model.Email;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
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

    public void sendEmail(Email email, String attachmentPath) {
        try {
            System.out.println("Preparing to send email to " + email.getRecipientEmail());
            Properties props = createEmailProperties();
            Session session = createEmailSession(props);
            Message message = createEmailMessage(session, email, attachmentPath);
            Transport.send(message);
            System.out.println("Email sent successfully to " + email.getRecipientEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading attachment file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
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

    private Message createEmailMessage(Session session, Email email, String attachmentPath) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipientEmail()));
        message.setSubject(email.getSubject());

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(email.getBody(), "UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        if (isAttachmentValid(attachmentPath)) {
            addAttachment(multipart, attachmentPath);
        }

        message.setContent(multipart);
        return message;
    }

    private boolean isAttachmentValid(String attachmentPath) {
        boolean isValid = attachmentPath != null && !attachmentPath.isEmpty() && new File(attachmentPath).exists();
        if (!isValid) {
            System.out.println("Attachment is invalid or does not exist: " + attachmentPath);
        }
        return isValid;
    }
    private void addAttachment(Multipart multipart, String attachmentPath) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        try {
            attachmentPart.attachFile(new File(attachmentPath));
            multipart.addBodyPart(attachmentPart);
        } catch (IOException e) {
            System.err.println("Error attaching file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}