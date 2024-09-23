package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpNotifier {
    private String ipAddress;
    private int port;

    public UdpNotifier(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void sendNotification(String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(ipAddress);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            
            System.out.println("Sending notification: " + message + " to " + ipAddress + ":" + port);
            socket.send(packet);
            System.out.println("Notification sent successfully.");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}