package view;

import java.awt.*;
import javax.swing.*;
import controller.LoginController;
import java.util.regex.Pattern;

public class LoginView extends JFrame {
    private JTextField useremailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JCheckBox showPasswordCheckBox;

    public LoginView() {
        init();
    }

    private void init() {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font headerFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        JLabel header = new JLabel("ĐĂNG NHẬP");
        header.setFont(headerFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userEmailLabel = new JLabel("Email đăng nhập:");
        userEmailLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(userEmailLabel, gbc);

        useremailField = new JTextField(20);
        gbc.gridx = 1;
        this.add(useremailField, gbc);

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        this.add(passwordField, gbc);

        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Hiện mật khẩu
            } else {
                passwordField.setEchoChar('•'); // Ẩn mật khẩu
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(showPasswordCheckBox, gbc);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Đăng Nhập");
        JButton registerButton = new JButton("Đăng Ký");
        loginButton.setFont(buttonFont);
        registerButton.setFont(buttonFont);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(buttonPanel, gbc);

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 5;
        this.add(messageLabel, gbc);

        // Thêm ActionListener cho nút
        LoginController controller = new LoginController(this);
        loginButton.addActionListener(e -> {
            if (validateEmail(useremailField.getText())) {
                controller.login(); // Gọi phương thức đăng nhập
            } else {
                showMessage("Email không hợp lệ!");
            }
        });
        registerButton.addActionListener(e -> {
            new RegisterView(); // Mở giao diện đăng ký
            this.setVisible(false); // Ẩn giao diện đăng nhập
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

    public String getEmail() {
        return useremailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
