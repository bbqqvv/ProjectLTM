package view;

import javax.swing.*;
import java.awt.*;

public class EmailDetailView extends JDialog {
    private JTextArea textArea;
    private JLabel senderLabel;
    private JLabel profileImageLabel;

    public EmailDetailView(Frame owner, String emailDetails, String senderName, ImageIcon senderImage) {
        super(owner, "Email Details", true);
        setSize(400, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel cho thông tin người gửi
        JPanel senderPanel = new JPanel();
        senderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Nhãn ảnh đại diện
        profileImageLabel = new JLabel();
        if (senderImage != null) {
            Image img = senderImage.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            profileImageLabel.setIcon(new ImageIcon(img));
        } else {
            profileImageLabel.setText("No image");
        }
        senderPanel.add(profileImageLabel);

        // Nhãn tên người gửi
        senderLabel = new JLabel(senderName);
        senderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        senderPanel.add(senderLabel);

        add(senderPanel, BorderLayout.NORTH);

        // Khu vực chi tiết email
        textArea = new JTextArea(emailDetails);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Nút đóng
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}