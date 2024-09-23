package test;

import network.UdpNotifier;

public class TestUdpSender {
    public static void main(String[] args) {
        UdpNotifier notifier = new UdpNotifier("192.168.1.100", 12345); // Thay đổi địa chỉ IP và cổng
        notifier.sendNotification("Test message");
    }
}