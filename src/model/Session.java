package model;

public class Session {
    private Long id;                 // Khóa chính
    private Long userId;            // ID của người dùng
    private String sessionToken;     // Token phiên
    private String ipAddress;        // Địa chỉ IP
    private String loginTime;        // Thời gian đăng nhập
    private String logoutTime;       // Thời gian đăng xuất

    // Constructors
    public Session() {
    	
    }

    public Session(Long id, Long userId, String sessionToken, String ipAddress, String loginTime, String logoutTime) {
        this.id = id;
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }
    public Session(int i, String ipAddress, String sessionToken) {
        this.userId = (long) i;
        this.ipAddress = ipAddress;
        this.sessionToken = sessionToken;
        this.loginTime = java.time.LocalDateTime.now().toString(); // Thiết lập thời gian đăng nhập hiện tại
        this.logoutTime = null; // Mặc định là null
    }

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }
    
    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userId=" + userId +
                ", sessionToken='" + sessionToken + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", logoutTime='" + logoutTime + '\'' +
                '}';
    }

    public void logout() {
        this.logoutTime = java.time.LocalDateTime.now().toString(); // Cập nhật thời gian đăng xuất
    }

}
