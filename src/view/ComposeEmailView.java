package view;

import java.awt.*;
import java.io.File;
import java.net.InetAddress;
import java.sql.Timestamp;
import javax.swing.*;
import controller.EmailController;
import model.Email;
import network.UdpNotifier;
import until.UserIpMapping;

public class ComposeEmailView extends JFrame {
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea bodyArea;
    private EmailManagementView emailManagementView;
    private EmailController emailController;
    private UdpNotifier udpNotifier;
    private JButton attachButton;
    private JButton removeAttachmentButton;
    private String attachedFilePath; // Đường dẫn tệp đính kèm
    private JLabel attachmentLabel; // Nhãn hiển thị thông tin tệp đính kèm
    private UserIpMapping userIpMapping;

    // Constructor cập nhật để nhận UserIpMapping
    public ComposeEmailView(EmailManagementView emailManagementView, String host, String username, String password, UdpNotifier udpNotifier, UserIpMapping userIpMapping) {
        this.emailManagementView = emailManagementView;
        this.emailController = new EmailController(this, host, username, password);
        this.udpNotifier = udpNotifier;
        this.userIpMapping = userIpMapping; // Đảm bảo đối tượng này không null
        setTitle("Compose Email");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 250, 250));

        recipientField = new JTextField();
        subjectField = new JTextField();
        bodyArea = new JTextArea();
        attachButton = createAttachButton();
        removeAttachmentButton = createRemoveAttachmentButton();
        JButton sendButton = createSendButton();
        attachmentLabel = new JLabel("No file attached");

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        addComponentsToPanel(panel);
        panel.add(attachmentLabel);
        panel.add(attachButton);
        panel.add(removeAttachmentButton);
        panel.add(sendButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createAttachButton() {
        JButton attachButton = new JButton("Attach File");
        attachButton.setFont(new Font("Arial", Font.BOLD, 16));
        attachButton.setBackground(new Color(52, 152, 219));
        attachButton.setForeground(Color.WHITE);
        attachButton.setFocusPainted(false);
        attachButton.addActionListener(e -> attachFile());
        return attachButton;
    }

    private JButton createRemoveAttachmentButton() {
        JButton removeButton = new JButton("Remove Attachment");
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setBackground(new Color(231, 76, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeAttachment());
        return removeButton;
    }

    private JButton createSendButton() {
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(new Color(46, 204, 113));
        sendButton.setForeground(Color.WHITE);
        sendButton.setIcon(new ImageIcon("send_icon.png"));
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(e -> sendEmail());
        return sendButton;
    }

    private void addComponentsToPanel(JPanel panel) {
        panel.add(createLabel("To:"));
        panel.add(recipientField);
        panel.add(createLabel("Subject:"));
        panel.add(subjectField);
        panel.add(createLabel("Body:"));
        panel.add(new JScrollPane(bodyArea));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void attachFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            attachedFilePath = selectedFile.getAbsolutePath();
            attachmentLabel.setText("Attached: " + selectedFile.getName());
            JOptionPane.showMessageDialog(this, "Attached: " + attachedFilePath);
        }
    }

    private void removeAttachment() {
        attachedFilePath = null;
        attachmentLabel.setText("No file attached");
        JOptionPane.showMessageDialog(this, "Attachment removed.");
    }

    private void sendEmail() {
        String recipient = recipientField.getText().trim();
        String subject = subjectField.getText().trim();
        String body = bodyArea.getText().trim();

        if (!isValidEmail(recipient)) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (subject.isEmpty() || body.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Email email = createEmail(recipient, subject, body);
        boolean isSent = emailController.sendEmail(email, attachedFilePath);

        if (isSent) {
            // Kiểm tra nếu emailManagementView không null và có phương thức addEmail
            if (emailManagementView != null) {
                emailManagementView.addEmail(email);
            } else {
                System.err.println("Error: emailManagementView is null or does not have addEmail method.");
            }
            
            // Gửi thông báo về địa chỉ IP
            handleIpNotification(recipient);
        }
        dispose();
    }
    private void handleIpNotification(String recipient) {
        String recipientIpAddress = userIpMapping.getIpAddress(recipient);
        if (recipientIpAddress != null) {
            System.out.println("Recipient IP Address: " + recipientIpAddress);
            if (isIpReachable(recipientIpAddress)) {
                udpNotifier.sendNotification("New email sent to: " + recipient + " at IP: " + recipientIpAddress);
                System.out.println("Notification sent to IP: " + recipientIpAddress);
            } else {
                System.out.println("IP address is not reachable: " + recipientIpAddress);
            }
        } else {
            udpNotifier.sendNotification("New email sent to: " + recipient + " (IP not found)");
            System.out.println("IP address not found for recipient: " + recipient);
        }
    }
    private boolean isIpReachable(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(2000); // Thời gian timeout là 2 giây
        } catch (Exception e) {
            System.err.println("Error checking IP reachability: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private Email createEmail(String recipient, String subject, String body) {
        int senderId = emailManagementView.getUser().getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new Email(senderId, recipient, subject, body, timestamp, false);
    }

    public EmailManagementView getEmailManagementView() {
        return emailManagementView;
    }
}