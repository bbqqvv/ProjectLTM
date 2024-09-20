package network;

import model.Email;
import view.EmailManagementView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EmailReceiver {
    private DatagramSocket socket;
    private EmailManagementView emailManagementView;

    public EmailReceiver(EmailManagementView emailManagementView) throws Exception {
        this.emailManagementView = emailManagementView;
        socket = new DatagramSocket(9876);
        startReceiving();
    }

    public void startReceiving() {
        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    Email email = deserialize(packet.getData());
                    if (email != null) {
                        emailManagementView.addEmail(email);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception
            }
        }).start();
    }

    private Email deserialize(byte[] data) {
        try (java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data);
             java.io.ObjectInputStream in = new java.io.ObjectInputStream(bis)) {
            return (Email) in.readObject();
        } catch (Exception e) {
            System.err.println("Failed to deserialize email: " + e.getMessage());
            e.printStackTrace(); // Detailed error output
            return null;
        }
    }

    public void stopReceiving() {
        if (socket != null && !socket.isClosed()) {
            socket.close(); // Clean up the socket
        }
    }
}
