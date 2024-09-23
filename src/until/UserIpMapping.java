package until;

import java.util.HashMap;
import java.util.Map;

public class UserIpMapping {
    private Map<String, String> emailToIpMap = new HashMap<>();

    public void addMapping(String email, String ipAddress) {
        System.out.println("Adding mapping: " + email + " -> " + ipAddress);
        emailToIpMap.put(email, ipAddress);
    }

    public String getIpAddress(String email) {
        String ipAddress = emailToIpMap.get(email);
        if (ipAddress == null) {
            System.out.println("No IP address found for: " + email);
        } else {
            System.out.println("Found IP address for " + email + ": " + ipAddress);
        }
        return ipAddress;
    }
}