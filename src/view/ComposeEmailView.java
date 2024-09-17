// ComposeEmailView.java
package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import controller.EmailController;

public class ComposeEmailView extends JFrame {
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea bodyArea;
    private JButton attachButton;
    private JButton sendButton;
    private EmailManagementView emailManagementView;
    private EmailController emailController;
    private File attachment;

    public ComposeEmailView(EmailManagementView emailManagementView) {
        this.emailManagementView = emailManagementView;
        this.emailController = new EmailController(emailManagementView);
        init();
    }

    private void init() {
        // Set up frame
        this.setTitle("Compose New Email");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // Recipient field
        JLabel recipientLabel = new JLabel("To:");
        recipientLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(recipientLabel, gbc);

        recipientField = new JTextField(30);
        recipientField.setFont(textFieldFont);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(recipientField, gbc);

        // Subject field
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(labelFont);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(subjectLabel, gbc);

        subjectField = new JTextField(30);
        subjectField.setFont(textFieldFont);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(subjectField, gbc);

        // Body field
        JLabel bodyLabel = new JLabel("Body:");
        bodyLabel.setFont(labelFont);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        mainPanel.add(bodyLabel, gbc);

        bodyArea = new JTextArea(10, 30);
        bodyArea.setFont(textFieldFont);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(bodyArea);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(scrollPane, gbc);

        // Button panel with improved layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        sendButton = new JButton("Send");
        sendButton.setFont(buttonFont);
        sendButton.setIcon(new ImageIcon("icons/send.png")); // Add an icon for send button
        sendButton.setBackground(new Color(0, 150, 136));
        sendButton.setForeground(Color.WHITE);
        sendButton.setOpaque(true);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(sendButton);

        attachButton = new JButton("Attach File");
        attachButton.setFont(buttonFont);
        attachButton.setIcon(new ImageIcon("icons/attach.png")); // Add an icon for attach button
        attachButton.setBackground(new Color(0, 150, 136));
        attachButton.setForeground(Color.WHITE);
        attachButton.setOpaque(true);
        attachButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(attachButton);

        // Add action listeners for buttons
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    attachment = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "File attached: " + attachment.getName());
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmail();
            }
        });

        // Add components to frame
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void sendEmail() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String body = bodyArea.getText();

        int senderId = getCurrentUserId(); // Get actual user ID

        boolean success;
        if (attachment != null) {
            success = emailController.sendEmailWithAttachment(senderId, recipient, subject, body, attachment);
        } else {
            success = emailController.sendEmail(senderId, recipient, subject, body);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Email sent successfully!");
            emailManagementView.refreshEmailList(); // Update email list
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send email.");
        }
    }

    private int getCurrentUserId() {
        return emailManagementView.getUser().getId(); // Use method to get the actual logged-in user's ID
    }
}
