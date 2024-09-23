package view;

import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import controller.EmailController;
import dao.EmailDao;
import dao.SessionDao;
import model.Email;
import model.Session;
import model.User;
import network.EmailReceiver;
import network.UdpNotifier;
import until.UserIpMapping;

public class EmailManagementView {
    private JFrame frame;
    private JList<String> emailList;
    private DefaultListModel<String> listModel;
    private User user;
    private Session session;
    private EmailController emailController;
    private List<Email> emailListData = new ArrayList<>();
    private UdpNotifier udpNotifier;
    private boolean isReceivingEmails = false;
	private UserIpMapping userIpMapping;

	public EmailManagementView(User user) {
	    this.user = user;
	    this.session = new Session(user.getId(), getUserIpAddress(), user.getEmail());
	    // Khởi tạo UserIpMapping
	    this.userIpMapping = new UserIpMapping(); // Khởi tạo đối tượng
	    String host = "imap.gmail.com";
	    String username = user.getEmail();
	    String password = user.getPassword();
	    this.emailController = new EmailController(this, host, username, password);
	    initialize();
	    loadEmails();
	}
    private void initialize() {
        frame = new JFrame("Email Management - " + user.getFullname());
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        addHeader();
        addSidebar();
        addSearchFeature();
        addFilterFeature();
        addEmailList();

        frame.setVisible(true);
        frame.setResizable(false);
    }

    private String getUserIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }

    private void loadEmails() {
        if (!isReceivingEmails) {
            showMessage("Starting to receive emails...");
            System.out.println("Initializing email receiver...");
            SwingWorker<Void, Email> worker = new SwingWorker<Void, Email>() {
                @Override
                protected Void doInBackground() {
                    try {
                        EmailDao emailDao = emailController.getEmailDao();
                        SessionDao sessionDao = new SessionDao(); // Khởi tạo SessionDao
                        udpNotifier = new UdpNotifier(session.getIpAddress(), 9876);
                        udpNotifier.sendNotification("Email received");
                        System.out.println("UDP notification sent.");

                        EmailReceiver emailReceiver = new EmailReceiver(EmailManagementView.this, emailDao, sessionDao, "imap.gmail.com", user.getEmail(), user.getPassword(), udpNotifier);
                        emailReceiver.startReceivingEmails();
                        isReceivingEmails = true;
                        System.out.println("Email receiver started.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage("Error loading emails: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void done() {
                    showMessage("Started receiving emails.");
                }
            };
            worker.execute();
        } else {
            showMessage("Already receiving emails.");
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
                        showEmailDetails(selectedEmail);
                    }
                }
            }
        });
    }

    public void refreshEmailList() {
        listModel.clear();
        for (Email email : emailListData) {
            listModel.addElement(email.getSubject());
        }
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

        JButton loadEmailsButton = createSidebarButton("Load Emails");
        loadEmailsButton.addActionListener(e -> loadEmails());
        sidebar.add(loadEmailsButton);

        frame.add(sidebar, BorderLayout.WEST);
    }

    private void addSearchFeature() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            filterEmailList(query);
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
    }

    private void filterEmailList(String query) {
        listModel.clear();
        for (Email email : emailListData) {
            if (email.getSubject().toLowerCase().contains(query)) {
                listModel.addElement(email.getSubject());
            }
        }
    }

    private void addFilterFeature() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        String[] filterOptions = {"All", "Unread", "Important"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);

        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            filterEmailByStatus(selectedFilter);
        });

        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);
        frame.getContentPane().add(filterPanel, BorderLayout.SOUTH);
    }

    private void filterEmailByStatus(String status) {
        listModel.clear();
        for (Email email : emailListData) {
            if (status.equals("All") || 
                (status.equals("Unread") && !email.isRead()) || 
                (status.equals("Important") && email.isImportant())) {
                listModel.addElement(email.getSubject());
            }
        }
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
        new EmailDetailView(email);
    }

    private void openComposeEmailWindow() {
        ComposeEmailView composeEmailView = new ComposeEmailView(this, "smtp.gmail.com", user.getEmail(), user.getPassword(), udpNotifier,userIpMapping );
        composeEmailView.setVisible(true);
    }

    private void deleteSelectedEmail() {
        int selectedIndex = emailList.getSelectedIndex();
        if (selectedIndex >= 0) {
            emailListData.remove(selectedIndex);
            refreshEmailList();
            showMessage("Email deleted successfully.");
        } else {
            showMessage("Please select an email to delete.");
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public void addEmail(Email email) {
        emailListData.add(email);
        refreshEmailList();
        System.out.println("Email added: " + email.getSubject());
    }
    public User getUser() {
        return user; // Trả về đối tượng User
    }
}