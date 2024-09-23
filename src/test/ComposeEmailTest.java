package test;

import network.UdpNotifier;
import view.ComposeEmailView;

public class ComposeEmailTest {
    public static void main(String[] args) {
        // Tạo địa chỉ IP và cổng cho UdpNotifier
        String ipAddress = "localhost"; // hoặc địa chỉ IP của máy nhận
        int port = 9876; // cổng UDP

        // Khởi tạo UdpNotifier
        UdpNotifier udpNotifier = new UdpNotifier(ipAddress, port);

        // Khởi tạo giao diện ComposeEmailView
        ComposeEmailView composeEmailView = new ComposeEmailView(null, "host", "username", "password", udpNotifier);
        
        // Đảm bảo giao diện hiển thị
        composeEmailView.setVisible(true);
    }
}
