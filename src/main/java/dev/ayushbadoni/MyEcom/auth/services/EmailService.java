package dev.ayushbadoni.MyEcom.auth.services;

import dev.ayushbadoni.MyEcom.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendMail(User user){
        String subject = " Verify your email";
        String senderName = "MyEcom";
        String mailContent = "Hello "+ user.getUsername();
        mailContent += "Your verification code is : <strong>"+ user.getVerificationCode() +"</strong><br>";
        mailContent += "Please enter this code to verify your account.<br>";
        mailContent += "<strong>" + senderName +"<strong>";

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setText(mailContent);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            return "Error while Sending Mail";
        }
        return "Email sent";
    }
}
