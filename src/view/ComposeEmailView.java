package view;

import controller.EmailController;
import model.Email;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class ComposeEmailView extends JFrame {
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea bodyArea;
    private EmailManagementView emailManagementView;
    private EmailController emailController;

    public ComposeEmailView(EmailManagementView emailManagementView, String host, String username, String password) {
        this.emailManagementView = emailManagementView;
        this.emailController = new EmailController(this, host, username, password);
        setTitle("Compose Email");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 250, 250)); // Subtle background color

        recipientField = new JTextField();
        subjectField = new JTextField();
        bodyArea = new JTextArea();
        JButton sendButton = new JButton("Send");

        // Customize button
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(new Color(46, 204, 113));
        sendButton.setForeground(Color.WHITE);
        sendButton.setIcon(new ImageIcon("send_icon.png")); // Use an icon
        sendButton.setFocusPainted(false);

        sendButton.addActionListener(e -> sendEmail());

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10)); // Adjusted layout and spacing
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        panel.setBackground(Color.WHITE); // Panel background

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(toLabel);
        panel.add(recipientField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(subjectLabel);
        panel.add(subjectField);

        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(bodyLabel);
        panel.add(new JScrollPane(bodyArea));

        panel.add(sendButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void sendEmail() {
        String recipient = recipientField.getText().trim();
        String subject = subjectField.getText().trim();
        String body = bodyArea.getText().trim();
        int senderId = emailManagementView.getUser().getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (recipient.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Email email = new Email(senderId, recipient, subject, body, timestamp, false);
        emailController.sendEmail(email);
        emailManagementView.addEmail(email);
        dispose();
    }
}
