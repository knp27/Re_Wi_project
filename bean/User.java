package in.co.codeplanet.rewi.bean;

import java.sql.Time;

public class User{
    private String userName;
    private String email;
    private String password;


    private int otp;
    private String mobileNo;


    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    private String wifiPassword;

    private  int userid;

    private String newPassword;
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public int getOtp() {
        return otp;
    }

    public String getNewPassword() {
        return newPassword;
    }
    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getMobileNo() {
        return mobileNo;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }


}
