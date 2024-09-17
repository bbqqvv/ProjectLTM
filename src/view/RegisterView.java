package view;

import java.awt.*;
import javax.swing.*;
import controller.RegisterController;
import java.util.regex.Pattern;

public class RegisterView extends JFrame {
    private JTextField useremailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField; // Trường mật khẩu xác nhận
    private JLabel messageLabel;
    private JCheckBox showPasswordCheckBox;

    public RegisterView() {
        init();
    }

    private void init() {
        this.setSize(400, 350); // Tăng kích thước để phù hợp với các trường mới
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font headerFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        JLabel header = new JLabel("ĐĂNG KÝ");
        header.setFont(headerFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel useremailLabel = new JLabel("Email đăng nhập:");
        useremailLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(useremailLabel, gbc);

        useremailField = new JTextField(15);
        gbc.gridx = 1;
        this.add(useremailField, gbc);

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        this.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        this.add(confirmPasswordField, gbc);

        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.addActionListener(e -> {
            char echoChar = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echoChar);
            confirmPasswordField.setEchoChar(echoChar); // Cập nhật trường xác nhận
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(showPasswordCheckBox, gbc);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Đăng Ký");
        JButton backButton = new JButton("Quay Lại");
        registerButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        this.add(buttonPanel, gbc);

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 6;
        this.add(messageLabel, gbc);

        // Thêm ActionListener cho nút
        RegisterController controller = new RegisterController(this);
        registerButton.addActionListener(e -> {
            if (validateEmail(useremailField.getText()) && 
                new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                controller.register(); // Gọi phương thức đăng ký
            } else if (!validateEmail(useremailField.getText())) {
                showMessage("Email không hợp lệ!");
            } else {
                showMessage("Mật khẩu không khớp!");
            }
        });
        backButton.addActionListener(e -> {
            this.dispose(); // Đóng giao diện đăng ký
            new LoginView(); // Mở lại giao diện đăng nhập
        });

        this.setVisible(true);
    }

    // Method to validate email format
    private boolean validateEmail(String email) {
        // Basic regex pattern for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public String getUsername() {
        return useremailField.getText();
    }

    public String getEmail() {
        return useremailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
