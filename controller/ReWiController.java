package in.co.codeplanet.rewi.controller;

import in.co.codeplanet.rewi.bean.EmailDetails;
import in.co.codeplanet.rewi.bean.User;
import in.co.codeplanet.rewi.schedular.TimeOut;
import in.co.codeplanet.rewi.service.EmailService;
import in.co.codeplanet.rewi.utility.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ReWiController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private TimeOut timeOut;

    @PostMapping("register")
    public String signUp(@RequestBody User user) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query1 = " select * from user where username=? or email=?";
            PreparedStatement stmt = con.prepareStatement(query1);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true)
                return "this username or email already exist";
            else {
                int otp = Integer.parseInt(Otp.generateOtp(4));
                EmailDetails emailDetails = new EmailDetails(user.getEmail(), "otp verification", "your otp is" + otp);
                emailService.sendMail(emailDetails);
                String query = " insert into user(username,email,password,otp,is_verified) values(?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query);
                stmt1.setString(1, user.getUserName());
                stmt1.setString(2, user.getEmail());
                stmt1.setString(3, user.getPassword());
                stmt1.setInt(4, otp);
                stmt1.setInt(5, 0);
                int result = stmt1.executeUpdate();
                return "your profile has been created successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "something went wrong";
        }
    }

    @PostMapping("verification")
    public String emailverification(@RequestBody User user) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query = "select otp from user where email=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, user.getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                if (rs.getInt(1) == user.getOtp()) {
                    String query1 = "update user set is_verified=1 where email=?";
                    PreparedStatement stmt1 = con.prepareStatement(query1);
                    stmt1.setString(1, user.getEmail());
                    stmt1.executeUpdate();
                    return "your account has been successfully verified";
                } else
                    return "otp didnt match kindly try again";
            } else
                return "there is no account corresponding to this email";

        } catch (Exception e) {
            return "something went wrong";
        }

    }

    @PostMapping("login")
    public String signIn(@RequestBody User user) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query1 = "select * from user where email=? and password=? and is_verified=1";
            PreparedStatement stmt = con.prepareStatement(query1);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                return "login successful";
            } else
                return "either invalid email,password or your account is not verified";
        } catch (Exception e) {
            return "something went wrong";
        }
    }

    @GetMapping("forgotpassword")
    public String forgetPassword(@RequestParam String userName) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query = "select email from user where username=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                String email = rs.getString(1);
                String password = Otp.generateOtp(8);
                EmailDetails emailDetails = new EmailDetails(email, "new password", "your new password is" + password);
                emailService.sendMail(emailDetails);
                String query1 = "update user set password=? where username=?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                stmt1.setString(1, password);
                stmt1.setString(2, userName);
                stmt1.executeUpdate();
                return "your new password has been successfully sent over your mai id";
            } else
                return "username doesnt exists";

        } catch (Exception e) {
            return "something went wrong";
        }
    }

    @PostMapping("passwordchange")
    public String passwordChange(@RequestBody User user) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query = "select * from user where email=? and password=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                String query1 = "update user set password=? where email=?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                stmt1.setString(1, user.getNewPassword());
                stmt1.setString(2, user.getEmail());
                stmt1.executeUpdate();
                return "your password has been updated successfully";
            } else
                return "either email or old password is wrong ";

        } catch (Exception e) {
            return "something went  wrong";
        }

    }

    //    @PostMapping("mobileverify")
//    public String mobileVerification(@RequestBody User user) {
//        Pattern p= Pattern.compile("(0/91)?[6-9][0-9]{9}");
//        String no=user.getMobileNo();
//        Matcher m=p.matcher(no);
//        if(m.find()==true&&m.group().equals(no)==true) {
//            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
//                String wifipassword=Otp.generateOtp(4);
//                EmailDetails emailDetails=new EmailDetails(user.getEmail(), "wifipassword","your wifipassword is"+wifipassword);
//                emailService.sendMail(emailDetails);
//                String query="select * from user where email=?";
//                PreparedStatement stmt = con.prepareStatement(query);
//                stmt.setString(1, user.getEmail());
//                ResultSet rs= stmt.executeQuery();
//                if(rs.next()) {
//                    String query1 = "insert into user(mobile_no,wifi_password) values(?,?)";
//                    PreparedStatement stmt1 = con.prepareStatement(query1);
////                    stmt1.setString(1, rs.getString(7));
////                    stmt1.setString(2, rs.getString(8));
//                    stmt1.setString(1, user.getMobileNo());
//                    stmt1.setString(2, wifipassword);
//
//                    int result = stmt1.executeUpdate();
//                    return "valid mobile number and wifipassword has been sent over your mail";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "something went  wrong";
//            }
//        }
//       return "no account regarding this email";
//    }
    @PostMapping("mobileverify")
    public String mobileVerification(@RequestBody User user) throws Exception {
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        String no = user.getMobileNo();
        Matcher m = p.matcher(no);
        if (m.find() == true && m.group().equals(no) == true) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
                String wifipassword = Otp.generateOtp(4);
                EmailDetails emailDetails = new EmailDetails(user.getEmail(), "wifipassword", "your wifipassword is" + wifipassword);
                emailService.sendMail(emailDetails);
                String query = "insert into wifi values(?,?,?,?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, user.getUserid());
                stmt.setString(2, user.getMobileNo());
                stmt.setString(3, wifipassword);
                stmt.setInt(4, 0);
                stmt.executeUpdate();
                return "valid mobile number and wifipassword has been sent over your mail";
            } catch (Exception e) {
                e.printStackTrace();
                return "something went  wrong";
            }
        } else
            return "not a valid mobileNO";
    }

    @PostMapping("wifilogin")
    public String wifiaccess(@RequestBody User user) throws Exception {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query = "select wifi_password from wifi where mobile_no=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, user.getMobileNo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                if (rs.getString(1).equals(user.getWifiPassword()) == true) {
                    LocalTime start = LocalTime.now();
                    LocalTime newTime = start.plusMinutes(1);
//                    timeOut.timeSpent();
//                    LocalTime stop = LocalTime.now();
//                    System.out.println(stop);
                    Long elapsedTime = ChronoUnit.MINUTES.between(start, newTime);
                    if (elapsedTime == 1) {
                        String query1 = "update wifi set duration=1,login=?,logout=? where mobile_no=?";
                        PreparedStatement stmt1 = con.prepareStatement(query1);

                        stmt1.setTime(1, Time.valueOf(start));
                        stmt1.setTime(2, Time.valueOf(newTime));
                        stmt1.setString(3, user.getMobileNo());
                        stmt1.executeUpdate();
                        return "login successful";
                    }
                } else
                    return "wrong wifipassword";
            } else
                return "no account exist regarding this mobileno";

        } catch (Exception e) {
            e.printStackTrace();
            return "something went  wrong";
        }
        return null;
    }

     public static  void timeOver() throws Exception {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifi_manager", "root", "root");) {
            String query1 = "select * from wifi where duration=1 ";
            Statement stmt1 = con.createStatement();
            ResultSet rs=stmt1.executeQuery(query1);
            if(rs.next()) {
                String query = "delete from user where userid in (select userid from wifi where duration=1)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}