package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import model.Email;
import model.User;
import controller.EmailController;

public class EmailManagementView {
    JFrame frame;
    private JList<String> emailList;
    private DefaultListModel<String> listModel;
    private User user;
    private EmailController emailController;
    private List<Email> emailListData = new ArrayList<>(); // List to store email data

    public EmailManagementView(User user) {
        this.user = user;
        this.emailController = new EmailController(this);
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
        loadEmails(); // Load received emails when initializing

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

        JButton composeButton = createSidebarButton("Compose Email", "src/images/new-message.png");
        composeButton.addActionListener(e -> openComposeEmailWindow());
        sidebar.add(composeButton);

        JButton viewHistoryButton = createSidebarButton("View History", "src/images/history-book.png");
        viewHistoryButton.addActionListener(e -> viewEmailHistory());
        sidebar.add(viewHistoryButton);

        JButton settingsButton = createSidebarButton("Settings", "src/images/settings.png");
        settingsButton.addActionListener(e -> openSettingsWindow());
        sidebar.add(settingsButton);

        frame.add(sidebar, BorderLayout.WEST);
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        if (iconPath != null) {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        }
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 150, 136));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(180, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 120, 104));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 150, 136));
            }
        });

        return button;
    }

    private void addEmailList() {
        listModel = new DefaultListModel<>();
        emailList = new JList<>(listModel);
        emailList.setBorder(BorderFactory.createTitledBorder("Received Emails"));
        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.setFont(new Font("Arial", Font.PLAIN, 14));
        emailList.setBackground(Color.WHITE);
        emailList.setForeground(Color.BLACK);
        emailList.setSelectionBackground(new Color(173, 216, 230));
        emailList.setSelectionForeground(Color.BLACK);
        JScrollPane listScrollPane = new JScrollPane(emailList);
        frame.getContentPane().add(listScrollPane, BorderLayout.CENTER);

        emailList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = emailList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Email selectedEmail = emailListData.get(index); // Accessing the stored email data
                        if (selectedEmail != null) {
                            showEmailDetails(selectedEmail);
                        }
                    }
                }
            }
        });
    }

    private void showEmailDetails(Email email) {
        String details = String.format("From: %s\nTo: %s\nSubject: %s\n\n%s\n\nDate: %s",
            email.getSenderId(), email.getRecipientEmail(), email.getSubject(), email.getBody(), email.getTimestamp());
        JOptionPane.showMessageDialog(frame, details, "Email Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openComposeEmailWindow() {
        ComposeEmailView composeEmailView = new ComposeEmailView(this); // Opens the compose email window
        composeEmailView.setVisible(true);
    }

    private void viewEmailHistory() {
        JOptionPane.showMessageDialog(frame, "Functionality for viewing email history can be implemented.");
    }

    private void openSettingsWindow() {
        SettingsView settingsView = new SettingsView(this);
        settingsView.setVisible(true);
    }

    public void addEmail(Email email) {
        // Display format for email
        String emailDisplay = String.format("From: %s | Subject: %s | Date: %s",
            email.getSenderId(), email.getSubject(), email.getTimestamp());
        listModel.addElement(emailDisplay); // Add to JList UI
        emailListData.add(email); // Store the email data for further use
    }

    // Fetch emails from EmailController and load them into the list
    private void loadEmails() {
        List<Email> emails = emailController.getEmailsByUserId(user.getId()); // Fetch emails from the controller
        listModel.clear(); // Clear the old list
        emailListData.clear(); // Clear the stored email data
        for (Email email : emails) {
            addEmail(email); // Add each email to the UI list and store its data
        }
    }


    public void refreshEmailList() {
        // Reload emails into the list
        listModel.clear();
        emailListData.clear();
        loadEmails();
    }

    public void showMessage(String response) {
        JOptionPane.showMessageDialog(frame, response, "Response", JOptionPane.INFORMATION_MESSAGE);
    }

    public User getUser() {
        return user;
    }
}
