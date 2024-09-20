package view;

import controller.EmailController;
import model.Email;
import model.User;
import network.EmailReceiver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmailManagementView {
    private JFrame frame;
    private JList<String> emailList;
    private DefaultListModel<String> listModel;
    private User user;
    private EmailController emailController;
    private List<Email> emailListData = new ArrayList<>();

    public EmailManagementView(User user) {
        this.user = user;
        String host = "smtp.gmail.com"; // Địa chỉ máy chủ SMTP Gmail
        String username = user.getEmail(); 
        String password = user.getPassword(); 

        // Kiểm tra thông tin xác thực
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email và mật khẩu không được để trống.");
        }

        this.emailController = new EmailController(host, username, password);

        try {
            EmailReceiver emailReceiver = new EmailReceiver(this);
            emailReceiver.startReceiving();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Email Management - " + user.getFullname());
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        addHeader();
        addSidebar();
        addEmailList();
        loadEmails();

        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void addHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 150, 136));
        JLabel welcomeLabel = new JLabel("Email Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
    }

    private void addSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new FlowLayout(FlowLayout.LEFT));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(240, 240, 240));

        JButton composeButton = createSidebarButton("Compose Email", null);
        composeButton.addActionListener(e -> openComposeEmailWindow());
        sidebar.add(composeButton);

        JButton deleteButton = createSidebarButton("Delete Email", null);
        deleteButton.addActionListener(e -> deleteSelectedEmail());
        sidebar.add(deleteButton);

        frame.add(sidebar, BorderLayout.WEST);
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 150, 136));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void addEmailList() {
        listModel = new DefaultListModel<>();
        emailList = new JList<>(listModel);
        emailList.setBorder(BorderFactory.createTitledBorder("Received Emails"));
        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane listScrollPane = new JScrollPane(emailList);
        frame.getContentPane().add(listScrollPane, BorderLayout.CENTER);

        emailList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = emailList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Email selectedEmail = emailListData.get(index);
                        if (selectedEmail != null) {
                            showEmailDetails(selectedEmail);
                        }
                    }
                }
            }
        });
    }

    private void showEmailDetails(Email email) {
        // Tách cửa sổ hiển thị chi tiết email
        new EmailDetailView(email);
    }

    private void openComposeEmailWindow() {
        String host = "smtp.gmail.com"; // Địa chỉ máy chủ SMTP Gmail
        String username = user.getEmail(); 
        String password = user.getPassword(); 

        ComposeEmailView composeEmailView = new ComposeEmailView(this, host, username, password);
        composeEmailView.setVisible(true);
    }

    public void addEmail(Email email) {
        String emailDisplay = String.format("From: %s | Subject: %s | Date: %s",
                email.getSenderId(), email.getSubject(), email.getTimestamp());
        listModel.addElement(emailDisplay);
        emailListData.add(email);
    }

    public User getUser() {
        return this.user;
    }

    private void deleteSelectedEmail() {
        int selectedIndex = emailList.getSelectedIndex();
        if (selectedIndex >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(frame, "Bạn có chắc muốn xóa email này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                listModel.remove(selectedIndex);
                emailListData.remove(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Vui lòng chọn email để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadEmails() {
        // Giả lập tải email
        // Thực tế, bạn sẽ lấy từ cơ sở dữ liệu hoặc nguồn khác
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
