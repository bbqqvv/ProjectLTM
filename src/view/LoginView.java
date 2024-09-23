package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import network.UdpNotifier;
import until.UserIpMapping;

public class LoginView extends JFrame {
    private JTextField useremailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton registerButton;
	private UdpNotifier udpNotifier;
	private UserIpMapping userIpMapping;

    public LoginView() {
        init();
    }

    private void init() {
        // Frame settings
        this.setSize(650, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        // Background color
        this.getContentPane().setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);

        // Add components
        addHeader(gbc);
        addEmailField(gbc);
        addPasswordField(gbc);
        addShowPasswordCheckBox(gbc);
        addButtons(gbc);
        addMessageLabel(gbc);

        this.setVisible(true);
    }

    private void addHeader(GridBagConstraints gbc) {
        JLabel header = new JLabel("ĐĂNG NHẬP");
        header.setFont(new Font("Arial", Font.BOLD, 30));
        header.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(header, gbc);
        gbc.gridwidth = 1;
    }

    private void addEmailField(GridBagConstraints gbc) {
        JLabel userEmailLabel = new JLabel("Email đăng nhập:");
        userEmailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(userEmailLabel, gbc);

        useremailField = new JTextField(20);
        useremailField.setFont(new Font("Arial", Font.PLAIN, 16));
        useremailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridx = 1;
        this.add(useremailField, gbc);
    }

    private void addPasswordField(GridBagConstraints gbc) {
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridx = 1;
        this.add(passwordField, gbc);
    }

    private void addShowPasswordCheckBox(GridBagConstraints gbc) {
        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordCheckBox.setBackground(new Color(250, 250, 250));
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '•');
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(showPasswordCheckBox, gbc);
    }

    private void addButtons(GridBagConstraints gbc) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(250, 250, 250));

        loginButton = new JButton("Đăng Nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
        });

        registerButton = new JButton("Đăng Ký");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(39, 174, 96));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(46, 204, 113));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(39, 174, 96));
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(buttonPanel, gbc);
    }

    private void addMessageLabel(GridBagConstraints gbc) {
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(messageLabel, gbc);
    }

    // Validate email format
    public boolean validateEmail(String email) {
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

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    // Set action listeners for buttons
    public void setLoginButtonActionListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setRegisterButtonActionListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Xác thực tài khoản Gmail
    public boolean authenticate(String email, String password) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });

            // Thử kết nối
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.close();
            return true; // Đăng nhập thành công

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Đăng nhập thất bại
        }
    }

    public void launchComposeEmailView(EmailManagementView emailManagementView) {
        String host = "smtp.gmail.com";
        String username = getEmail();
        String password = getPassword();

        ComposeEmailView composeEmailView = new ComposeEmailView(emailManagementView, host, username, password, udpNotifier,userIpMapping );
        composeEmailView.setVisible(true); // Hiển thị cửa sổ soạn email
    }
}
