package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import model.Email;

public class EmailDetailView {
    private JFrame frame;
    private JTextPane emailContent; // Thay đổi thành JTextPane

    public EmailDetailView(Email email) {
        initialize(email);
    }

    private void initialize(Email email) {
        frame = new JFrame("Chi Tiết Email");
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tiêu đề email
        JLabel emailHeader = new JLabel(String.format("<html><b>Từ:</b> %s | <b>Đến:</b> %s | <b>Ngày:</b> %s</html>",
                email.getSenderId(), email.getRecipientEmail(), email.getTimestamp()));
        emailHeader.setFont(new Font("Arial", Font.BOLD, 16));
        emailHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(emailHeader, BorderLayout.NORTH);

        // Nội dung email
        emailContent = new JTextPane(); // Sử dụng JTextPane
        emailContent.setContentType("text/html");

        // Xây dựng nội dung HTML với CSS
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; }")
                .append("h3 { color: #333; }")
                .append("p { font-size: 14px; color: #555; }")
                .append("a { color: #1a73e8; text-decoration: none; }")
                .append("a:hover { text-decoration: underline; }")
                .append(".email-body { background-color: #f1f1f1; padding: 15px; border-radius: 5px; }")
                .append("</style></head><body>")
                .append("<div class='email-body'>")
                .append("<h3>").append(email.getSubject()).append("</h3>")
                .append(convertLinksToHTML(email.getBody()))
                .append("</div>")
                .append("</body></html>");

        emailContent.setText(htmlContent.toString());
        emailContent.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(emailContent);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Nút để đóng
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Phương thức chuyển đổi liên kết thành thẻ <a> trong HTML
    private String convertLinksToHTML(String body) {
        String regex = "(https?://\\S+)";
        return body.replaceAll(regex, "<a href=\"$1\" target=\"_blank\">$1</a>")
                   .replaceAll("\n", "<br>");
    }
}

