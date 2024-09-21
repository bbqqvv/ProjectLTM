package view;

import controller.EmailController;
import model.Email;
import model.User;
import network.EmailReceiver;
import dao.EmailDao;

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
        String host = "imap.gmail.com"; // Địa chỉ máy chủ IMAP Gmail
        String username = user.getEmail();
        String password = user.getPassword();

        this.emailController = new EmailController(this, host, username, password);

        initialize();
        loadEmails();
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

        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void loadEmails() {
        try {
            EmailDao emailDao = emailController.getEmailDao(); // Giả định bạn có phương thức này
            EmailReceiver emailReceiver = new EmailReceiver(this, emailDao, "imap.gmail.com", user.getEmail(), user.getPassword());
            emailReceiver.receiveEmailsViaIMAP();
            loadEmailsFromDatabase();
        } catch (Exception e) {
            showMessage("Error loading emails: " + e.getMessage());
        }
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

        JButton composeButton = createSidebarButton("Compose Email");
        composeButton.addActionListener(e -> openComposeEmailWindow());
        sidebar.add(composeButton);

        JButton deleteButton = createSidebarButton("Delete Email");
        deleteButton.addActionListener(e -> deleteSelectedEmail());
        sidebar.add(deleteButton);

        frame.add(sidebar, BorderLayout.WEST);
    }

    private JButton createSidebarButton(String text) {
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

    private void showEmailDetails(Email email) {
        new EmailDetailView(email); // Hiển thị chi tiết email
    }

    private void openComposeEmailWindow() {
        ComposeEmailView composeEmailView = new ComposeEmailView(this, "smtp.gmail.com", user.getEmail(), user.getPassword());
        composeEmailView.setVisible(true);
    }

    public void addEmail(Email email) {
        String emailDisplay = String.format("<html><strong>From:</strong> %s <br><strong>Subject:</strong> %s <br><strong>Date:</strong> %s</html>",
                email.getSenderId(), email.getSubject(), email.getTimestamp());
        
        listModel.addElement(emailDisplay);
        emailListData.add(email);
        emailController.saveEmailIfValidSender(email); // Lưu email vào cơ sở dữ liệu
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
            showMessage("Vui lòng chọn email để xóa.");
        }
    }

    private void loadEmailsFromDatabase() {
        List<Email> emails = emailController.loadEmailsFromDatabase(user.getId());
        for (Email email : emails) {
            addEmail(email);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
