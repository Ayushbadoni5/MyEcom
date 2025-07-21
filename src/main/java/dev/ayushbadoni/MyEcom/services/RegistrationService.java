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

import java.util.List;

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
        User existing = userDetailRepository.findByEmail(request.getEmail()).orElse(null);

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
            user.setPhoneNumber(request.getPhoneNumber());


            String code = VerificationCodeGenrator.generateCode();

            user.setVerificationCode(code);

            List<String> roles = request.getRoles() != null && !request.getRoles().isEmpty()
                    ?request.getRoles().stream().map(role-> role.startsWith("ROLE_")?role:"ROLE_" + role.toUpperCase()).toList()
                    :List.of("ROLE_USER");

            user.setAuthorities(authorityService.getAuthorities(roles));
            userDetailRepository.save(user);

            emailService.sendMail(user);

            String joinedRoles = String.join(" and ", roles.stream().map(String::toUpperCase).toList());

            return RegistrationResponse.builder().code(200).message(joinedRoles + " Created").build();

        }catch (Exception e){
            System.out.println("Registration Error:" + e.getMessage());
            throw  new ServerErrorException(e.getMessage(),e.getCause());
        }

    }

    public void verifyUser(String username) {
        User user = userDetailRepository.findByEmail(username)
                .orElseThrow(()->new RuntimeException("User not found with email: " + username));
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
