package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.EmailDao;
import model.Email;
import view.EmailManagementView;

public class EmailController {
    private EmailManagementView emailView;

    public EmailController(EmailManagementView emailView) {
        this.emailView = emailView;
    }

    // Method to send a simple email without attachment
    public boolean sendEmail(int senderId, String recipient, String subject, String body) {
        try {
            DatagramSocket socket = new DatagramSocket();
            String message = "SEND EMAIL " + senderId + " " + recipient + " " + subject + " " + body;
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 9876);
            socket.send(packet);

            // Receive response from server
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            socket.close();

            if (response.trim().contains("Success")) {
                // Create and save the email to the database
                Email email = new Email(
                    senderId,
                    recipient,
                    subject,
                    body,
                    new Timestamp(System.currentTimeMillis()),
                    false
                );
                EmailDao emailDao = new EmailDao();
                emailDao.saveEmail(
                    email.getSenderId(),
                    email.getRecipientEmail(),
                    email.getSubject(),
                    email.getBody(),
                    email.getTimestamp(),
                    email.isRead()
                );
                emailView.showMessage("Email sent and saved successfully!");
                return true;
            } else {
                emailView.showMessage("Failed to send email.");
                return false;
            }
        } catch (Exception ex) {
            emailView.showMessage("Error: " + ex.getMessage());
            return false;
        }
    }

    // Method to retrieve emails by user ID via UDP
    public List<Email> getEmailsByUserId(int id) {
        List<Email> emails = new ArrayList<>();
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000); // 5-second timeout

            String request = "GET EMAILS " + id;
            byte[] buffer = request.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 9876);
            socket.send(packet);

            // Increased buffer size
            byte[] receiveBuffer = new byte[8192]; 
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

            // Print server response for debugging
            System.out.println("Server response: " + response);

            // Parsing the response
            String[] emailData = response.split("\n");
            for (String emailEntry : emailData) {
                String[] parts = emailEntry.split(";");
                if (parts.length >= 6) {
                    int senderId = Integer.parseInt(parts[0]);
                    String recipientEmail = parts[1];
                    String subject = parts[2];
                    String body = parts[3];
                    Timestamp timestamp = Timestamp.valueOf(parts[4]);
                    boolean isRead = Boolean.parseBoolean(parts[5]);

                    Email email = new Email(senderId, recipientEmail, subject, body, timestamp, isRead);
                    emails.add(email);
                }
            }
            socket.close();
        } catch (Exception ex) {
            emailView.showMessage("Connection error: " + ex.getMessage());
        }
        return emails;
    }


    // Method to send an email with an attachment
    public boolean sendEmailWithAttachment(int senderId, String recipient, String subject, String body, File attachment) {
        try {
            DatagramSocket socket = new DatagramSocket();
            String message = "SEND EMAIL WITH ATTACHMENT " + senderId + " " + recipient + " " + subject + " " + body;
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 9876);
            socket.send(packet);

            // Send the attachment as a byte stream
            FileInputStream fis = new FileInputStream(attachment);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] attachmentBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(attachmentBuffer)) != -1) {
                bos.write(attachmentBuffer, 0, bytesRead);
            }
            byte[] attachmentBytes = bos.toByteArray();
            DatagramPacket attachmentPacket = new DatagramPacket(attachmentBytes, attachmentBytes.length, InetAddress.getByName("localhost"), 9876);
            socket.send(attachmentPacket);

            // Receive response from server
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            socket.close();

            if ("Success".equalsIgnoreCase(response.trim())) {
                // Save email with attachment to database
                Email email = new Email(senderId, recipient, subject, body, new Timestamp(System.currentTimeMillis()), false);
                EmailDao emailDao = new EmailDao();
                int emailId = emailDao.saveEmail(
                    email.getSenderId(),
                    email.getRecipientEmail(),
                    email.getSubject(),
                    email.getBody(),
                    email.getTimestamp(),
                    email.isRead()
                );

                // Save attachment to the database
                emailDao.saveAttachment(emailId, attachment.getName(), attachment.getPath());

                emailView.showMessage("Email with attachment sent and saved successfully!");
                return true;
            } else {
                emailView.showMessage("Failed to send email.");
                return false;
            }
        } catch (Exception ex) {
            emailView.showMessage("Error: " + ex.getMessage());
            return false;
        }
    }

    // Method to get an email from the list based on its index
    public Email getEmailByIndex(int index, List<Email> emailListData) {
        if (index >= 0 && index < emailListData.size()) {
            return emailListData.get(index);
        }
        return null;
    }
}
