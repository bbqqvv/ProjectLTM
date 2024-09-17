package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerView extends JFrame {
    private JTextArea logArea;
    private DatagramSocket socket;

    public ServerView() {
        init();
    }

    private void init() {
        this.setTitle("Server Management");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        this.add(startButton, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void startServer() {
        try {
            socket = new DatagramSocket(9876);
            logArea.append("Server started...\n");

            new Thread(() -> {
                byte[] receiveData = new byte[1024];

                while (true) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        logArea.append("Received: " + message + "\n");
                        
                        // Phân tích yêu cầu gửi email
                        String response = handleEmailRequest(message);
                        
                        DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(),
                                receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(responsePacket);
                        
                        logArea.append("Sent: " + response + "\n");
                    } catch (Exception ex) {
                        logArea.append("Error: " + ex.getMessage() + "\n");
                    }
                }
            }).start();
        } catch (Exception ex) {
            logArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    // Xử lý yêu cầu gửi email
    private String handleEmailRequest(String message) {
        // Giả sử message có định dạng: "SEND EMAIL subject body"
        String[] parts = message.split(" ", 3);
        if (parts.length == 3 && "SEND".equals(parts[0]) && "EMAIL".equals(parts[1])) {
            String subject = parts[2];
            // Xử lý gửi email (có thể là gọi một service gửi email thực tế)
            // Đây là ví dụ đơn giản trả về thông báo gửi email thành công
            return "Email sent with subject: " + subject  +  " Success";
        }
        return "Invalid request";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerView::new);
    }
}
