package until;

import dao.SessionDao;

public class UserIpMapping {
    private SessionDao sessionDao = new SessionDao();

    public String getIpAddress(String email) {
        System.out.println("Retrieving IP address for email: " + email);
        String ipAddress = sessionDao.getIpAddressByEmail(email);
        
        if (ipAddress != null) {
            System.out.println("Found IP address for " + email + ": " + ipAddress);
        } else {
            System.out.println("No IP address found for: " + email);
        }
        
        return ipAddress; // Trả về địa chỉ IP hoặc null
    }
}