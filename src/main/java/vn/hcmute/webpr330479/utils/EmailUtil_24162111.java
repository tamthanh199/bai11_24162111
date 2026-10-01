package vn.hcmute.webpr330479.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public final class EmailUtil_24162111 {
    private EmailUtil_24162111() {}

    public static boolean sendOtpIfConfigured(String to, String otp) throws MessagingException {
        String username = System.getenv("MAIL_USERNAME"), password = System.getenv("MAIL_PASSWORD");
        if (username == null || username.isBlank() || password == null || password.isBlank())
            return false;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session =
                Session.getInstance(
                        props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Mã OTP kích hoạt tài khoản - Đề 05");
        message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.");
        Transport.send(message);
        return true;
    }
}
