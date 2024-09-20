package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import controller.RegisterController;

public class RegisterView extends JFrame {
    private JTextField useremailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JLabel messageLabel;
    private JButton registerButton;
    private JButton backButton;
    private RegisterController controller;

    public RegisterView() {
        this.controller = new RegisterController(this); // Truyền tham chiếu đến RegisterView
        this.init(); // Khởi tạo giao diện
        this.setVisible(true);
    }

    // Phương thức khởi tạo giao diện
    public void init() {
        this.setTitle("Đăng Ký");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gọi các phương thức tạo các thành phần UI
        createComponents();

        // Sắp xếp layout cho JFrame
        layoutComponents();
    }

    // Phương thức tạo các thành phần UI
    private void createComponents() {
        // Thiết kế trường nhập email
        useremailField = new JTextField(15);
        useremailField.setFont(new Font("Arial", Font.PLAIN, 14));
        useremailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Thiết kế trường nhập mật khẩu
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Thiết kế trường xác nhận mật khẩu
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Checkbox hiển thị mật khẩu
        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());

        // Thiết kế nút Đăng ký
        registerButton = new JButton("Đăng Ký");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(72, 201, 176));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.addActionListener(e -> controller.handleRegister()); // Gọi trực tiếp từ controller

        // Thiết kế nút Quay lại
        backButton = new JButton("Quay Lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(192, 57, 43));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.addActionListener(e -> controller.disposeView()); // Gọi trực tiếp từ controller

        // Thiết kế nhãn thông báo
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
    }

    // Layout các thành phần UI
    private void layoutComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        formPanel.add(new JLabel("Email đăng nhập:", JLabel.RIGHT));
        formPanel.add(useremailField);
        formPanel.add(new JLabel("Mật khẩu:", JLabel.RIGHT));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Xác nhận mật khẩu:", JLabel.RIGHT));
        formPanel.add(confirmPasswordField);
        formPanel.add(showPasswordCheckBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        this.setLayout(new BorderLayout());
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(messageLabel, BorderLayout.NORTH);
    }

    // Phương thức kiểm tra email
    public boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Phương thức chuyển đổi hiển thị mật khẩu
    private void togglePasswordVisibility() {
        char echoChar = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
        passwordField.setEchoChar(echoChar);
        confirmPasswordField.setEchoChar(echoChar);
    }

    // Các phương thức getter để lấy dữ liệu từ các thành phần UI
    public String getEmail() {
        return useremailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    // Phương thức hiển thị thông báo
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}