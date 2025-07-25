package dev.ayushbadoni.MyEcom.services;


import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service {
    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private  AuthorityService authorityService;

    public User getUser(String userName) {
        return userDetailRepository.findByEmail(userName).orElseThrow(()-> new RuntimeException("User not found with email: " + userName));
    }

    public User cresteUser(OAuth2User oAuth2User, String provider) {

        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String email = oAuth2User.getAttribute("email");

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .provider(provider)
                .enabled(true)
                .authorities(authorityService.getDeafultUserAuthority())
                .build();
        return userDetailRepository.save(user);
    }
}
