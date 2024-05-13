package in.co.codeplanet.rewi;

import in.co.codeplanet.rewi.bean.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] csd){
        Pattern p= Pattern.compile("(0/91)?[6-9][0-9]{9}");
        String no="9466553322";
        Matcher m=p.matcher(no);
        if(m.find()==true&&m.group().equals(no)==true)
            System.out.println("valid mobileno");
        else
            System.out.println("invalid mobileno");

    }
}
