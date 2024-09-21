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
        getContentPane().setBackground(new Color(250, 250, 250));

        recipientField = new JTextField();
        subjectField = new JTextField();
        bodyArea = new JTextArea();
        JButton sendButton = createSendButton();

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        addComponentsToPanel(panel);
        panel.add(sendButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
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

    public EmailManagementView getEmailManagementView() {
        return emailManagementView;
    }

}
