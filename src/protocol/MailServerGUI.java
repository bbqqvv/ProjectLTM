//package protocol;
//
//import javax.swing.*;
//
//import model.Email;
//import view.EmailManagementView;
//import java.awt.*;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MailServerGUI {
//    private static final int PORT = 9876;
//    private DatagramSocket socket;
//    private Map<String, EmailManagementView> userViews; // Quản lý các phiên bản EmailManagementView
//
//    public MailServerGUI() {
//        userViews = new HashMap<>();
//        try {
//            socket = new DatagramSocket(PORT);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        
//        createAndShowGUI();
//        startListening();
//    }
//
//    private void createAndShowGUI() {
//        JFrame frame = new JFrame("Mail Server");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//        frame.setVisible(true);
//    }
//
//    public void addUserView(String email, EmailManagementView view) {
//        userViews.put(email, view);
//    }
//
//    public EmailManagementView getRecipientView(String email) {
//        return userViews.get(email);
//    }
//
//    private void startListening() {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    byte[] receiveData = new byte[1024];
//                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                    socket.receive(receivePacket);
//                    
//                    String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
//                    String[] emailParts = receivedString.split("\n");
//                    
//                    // Parse the email details
//                    String sender = emailParts[0].substring(6).trim(); // Lấy địa chỉ người gửi
//                    String recipient = emailParts[1].substring(4).trim(); // Lấy địa chỉ người nhận
//                    String subject = emailParts[2].substring(9).trim(); // Lấy tiêu đề email
//                    String body = emailParts[3].substring(5).trim(); // Lấy nội dung email
//                    Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // Thay đổi theo nhu cầu
//
//                    // Create the Email object
//                    Email email = new Email(
//                        0, // Giả sử ID = 0 hoặc tự động sinh ID khác
//                        1,recipient,
//                        subject,
//                        body,
//                        timestamp,
//                        false // isRead
//                    );
//
//                    // Thêm email vào danh sách của người nhận
//                    SwingUtilities.invokeLater(() -> {
//                        EmailManagementView recipientView = getRecipientView(recipient); 
//                        if (recipientView != null) {
//                            recipientView.addEmail(email);
//                        }
//                    });
//
//                    // Gửi phản hồi về phía client
//                    String response = "Email received!";
//                    byte[] sendData = response.getBytes();
//                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
//                    socket.send(sendPacket);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(MailServerGUI::new);
//    }
//}
