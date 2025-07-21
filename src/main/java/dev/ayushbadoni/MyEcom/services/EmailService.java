package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.entities.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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


        String mailContent = "<p>Hello <strong> "+ user.getUsername()+"</strong>,</p>";
        mailContent += "<p>Your verification code is : <strong>"+ user.getVerificationCode() +"</strong></p>";
        mailContent += "<p>Please enter this code to verify your account.</p>";
        mailContent += "<br><strong>" + senderName +"</strong>";

        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(mailContent,true);

            javaMailSender.send(mailMessage);

        }catch (Exception e){
            e.printStackTrace(); // helpful for debugging
            return "Error while Sending Mail";
        }
        return "Email sent";
    }
}
