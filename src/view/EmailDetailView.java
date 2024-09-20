package view;

import model.Email;

import javax.swing.*;
import java.awt.*;

public class EmailDetailView {
    private JFrame frame;
    private JTextArea emailContent;

    public EmailDetailView(Email email) {
        initialize(email);
    }

    private void initialize(Email email) {
        frame = new JFrame("Email Details");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tiêu đề email
        JLabel emailHeader = new JLabel(String.format("From: %s  |  To: %s  |  Date: %s",
                email.getSenderId(), email.getRecipientEmail(), email.getTimestamp()));
        emailHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(emailHeader, BorderLayout.NORTH);

        // Nội dung email
        emailContent = new JTextArea(email.getBody());
        emailContent.setFont(new Font("Arial", Font.PLAIN, 14));
        emailContent.setWrapStyleWord(true);
        emailContent.setLineWrap(true);
        emailContent.setEditable(false); // Mặc định là không thể chỉnh sửa
        JScrollPane scrollPane = new JScrollPane(emailContent);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Nút để đóng hoặc chỉnh sửa
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> frame.dispose());
        JButton editButton = new JButton("Chỉnh sửa");
        editButton.addActionListener(e -> enableEditMode());

        buttonPanel.add(closeButton);
        buttonPanel.add(editButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Cho phép người dùng chỉnh sửa nội dung email
    private void enableEditMode() {
        emailContent.setEditable(true);
        emailContent.setBackground(new Color(255, 255, 224)); // Thay đổi màu nền để người dùng biết họ có thể chỉnh sửa
    }
}
