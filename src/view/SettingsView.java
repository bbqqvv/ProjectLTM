package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsView extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JLabel profileImageLabel;
    private EmailManagementView parentView;

    public SettingsView(EmailManagementView parentView) {
        super(parentView.frame, "Settings", true);
        this.parentView = parentView;
        setSize(400, 400);
        setLocationRelativeTo(parentView.frame);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().setBackground(new Color(240, 248, 255)); // Màu nền nhẹ

        // Các thành phần giao diện
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        usernameField.setText(parentView.getUser().getFullname());
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(15);
        emailField.setText(parentView.getUser().getEmail());
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Current Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        currentPasswordField = new JPasswordField(15);
        add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("New Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        newPasswordField = new JPasswordField(15);
        add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Profile Image:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        profileImageLabel = new JLabel("No image selected");
        profileImageLabel.setPreferredSize(new Dimension(50, 50)); // Kích thước cố định cho hình ảnh
        profileImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền cho nhãn
        add(profileImageLabel, gbc);
        
        JButton changeImageButton = new JButton("Change Image");
        changeImageButton.addActionListener(e -> changeProfileImage());
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        add(changeImageButton, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 179, 113)); // Màu xanh lá
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new SaveSettingsAction());
        add(saveButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED); // Màu đỏ
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gbc);
    }

    private void changeProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            profileImageLabel.setText(""); // Xóa văn bản
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image img = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Kích thước hình ảnh
            profileImageLabel.setIcon(new ImageIcon(img)); // Cập nhật hình ảnh
        }
    }

    private class SaveSettingsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newUsername = usernameField.getText();
            String newEmail = emailField.getText();
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            parentView.getUser().setFullname(newUsername);
            parentView.getUser().setEmail(newEmail);
            // Update password logic here if required

            JOptionPane.showMessageDialog(SettingsView.this, "Settings saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
