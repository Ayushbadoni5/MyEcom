package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.RegistrationRequest;
import dev.ayushbadoni.MyEcom.dto.RegistrationResponse;
import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.helper.VerificationCodeGenrator;
import dev.ayushbadoni.MyEcom.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

@Service
public class RegistrationService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private EmailService emailService;

    public RegistrationResponse createUser(RegistrationRequest request) {
        User existing = userDetailRepository.findByEmail(request.getEmail());

        if (null != existing){
            return RegistrationResponse.builder().code(400).message("Email Already exist!").build();
        }
        
        try {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProvider("manual");

            String code = VerificationCodeGenrator.generateCode();

            user.setVerificationCode(code);

            user.setAuthorities(authorityService.getUserAuthority());
            userDetailRepository.save(user);

            //call method to sent mail
            emailService.sendMail(user);
            return RegistrationResponse.builder().code(200).message("User Created").build();

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw  new ServerErrorException(e.getMessage(),e.getCause());
        }

    }

    public void verifyUser(String username) {
        User user = userDetailRepository.findByEmail(username);
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
