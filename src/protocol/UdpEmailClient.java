package protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpEmailClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private static final int SERVER_PORT = 9876;

    public UdpEmailClient(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Error creating DatagramSocket: " + e.getMessage());
        }
    }

    public void sendEmail(String sender, String recipient, String subject, String content) {
        if (socket == null || socket.isClosed()) {
            System.err.println("Socket is not available.");
            return;
        }

        String emailMessage = String.format(
            "From: %s\nTo: %s\nSubject: %s\nContent: %s",
            sender, recipient, subject, content
        );

        byte[] buffer = emailMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);

        try {
            socket.send(packet);
            System.out.println("Email sent to " + recipient);
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Socket closed.");
        }
    }
}