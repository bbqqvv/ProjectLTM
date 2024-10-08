package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int senderId;
    private String recipientEmail;
    private String subject;
    private String body;
    private Timestamp timestamp;
    private boolean isRead;
    private boolean isImportant; 
    private static Map<Integer, List<Email>> userEmails = new HashMap<>();

    public Email(int id, int senderId, String recipientEmail, String subject, String body, Timestamp timestamp, boolean isRead, boolean isImportant) {
        this.id = id;
        this.senderId = senderId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.isImportant = isImportant;
    }

    public Email() {
        super();
    }

    public Email(int senderId, String recipientEmail, String subject, String body, Timestamp timestamp, boolean isRead) {
        this.senderId = senderId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public Email(String recipientEmail, String subject, String body) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
        this.isImportant = false;
    }

    public Email(int id, int senderId, String recipientEmail, String subject, String body, Timestamp timestamp,
			boolean isRead) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.recipientEmail = recipientEmail;
		this.subject = subject;
		this.body = body;
		this.timestamp = timestamp;
		this.isRead = isRead;
	}

	public Email(int senderId, String recipientEmail, String subject, String body, boolean isImportant) {
        this.senderId = senderId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
        this.isImportant = isImportant;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", isImportant=" + isImportant +
                '}';
    }

    // Phương thức quản lý email
    private static void addEmail(int userId, Email email) {
        userEmails.computeIfAbsent(userId, k -> new ArrayList<>()).add(email);
    }

    private static String getEmailsForUser(int userId) {
        List<Email> emails = userEmails.getOrDefault(userId, new ArrayList<>());

        if (emails.isEmpty()) {
            return "No emails found";
        }

        StringBuilder sb = new StringBuilder();
        for (Email email : emails) {
            sb.append(email.toString()).append("\n");
        }

        return sb.toString();
    }
}
