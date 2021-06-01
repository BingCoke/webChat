package com.dzt.bean;


import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MyMail {
    private static InternetAddress sender;
    private final static HashMap<String,String> REGISTER ;
    private final static HashMap<String,String> FIND;
    private final static HashMap<String,String> OLD_MAIL;
    private final static HashMap<String,String> NEW_MAIL;
    static {
        HashMap register = new HashMap();
        register.put("subject","【账户注册】");
        register.put("before","账号注册验证码为:");
        register.put("after",",有效时间一分钟，请勿回复此邮箱");
        REGISTER = register;

        HashMap find = new HashMap();
        find.put("subject","【账户密码找回】");
        find.put("before","账号密码找回验证码为:");
        find.put("after",",有效时间一分钟,请勿回复此邮箱");
        FIND = find;

        HashMap oldMail = new HashMap();
        oldMail.put("subject","【邮箱更改验证】");
        oldMail.put("before","旧邮箱的验证码为：");
        oldMail.put("after",",有效时间一分钟,请勿回复此邮箱");
        OLD_MAIL = oldMail;


        HashMap newMail = new HashMap();
        newMail.put("subject","【新邮箱更改验证】");
        newMail.put("before","新邮箱的验证码为：");
        newMail.put("after",",有效时间一分钟,请勿回复此邮箱");
        NEW_MAIL = oldMail;

    }


    public static void sendFindMsg(String address,String code){
        sendMessage(address,code,FIND);
    }

    public static void sendRegisterMsg(String address,String code){
        sendMessage(address,code,REGISTER);
    }

    public static void sendToOldMai(String address, String code){
        sendMessage(address,code,OLD_MAIL);
    }

    public static void sendToNewMai(String address, String code){
        sendMessage(address,code,NEW_MAIL);
    }

    private static void sendMessage(String address,String code,Map<String,String> map){
        try {
            Properties prop = new Properties();
            prop.load(MyMail.class.getClassLoader().getResourceAsStream("mail.properties"));
            sender = new InternetAddress(prop.getProperty("mailAddress"));
            Session session = Session.getInstance(prop);
            Transport ts = session.getTransport();
            ts.connect(prop.getProperty("mail.host"), prop.getProperty("user"), prop.getProperty("password"));

            MimeMessage message = new MimeMessage(session);
            // 设定发件人
            message.setFrom(sender);
            //设定收件人
            message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(address));
            message.setSubject(map.get("subject"));
            message.setContent(map.get("before") + code + map.get("after"), "text/html;charset=UTF-8");

            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCode(){
        //  获取6为随机验证码
        String[] letters = new String[] {
                "q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m",
                "A","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M",
                "0","1","2","3","4","5","6","7","8","9"};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(letters[(int)Math.floor(Math.random()*letters.length)]);
        }
        return stringBuilder.toString();
    }
}
