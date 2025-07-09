package dev.ayushbadoni.MyEcom.auth.init;

import dev.ayushbadoni.MyEcom.auth.services.AuthorityService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer {
    @Autowired
    private AuthorityService authorityService;

    @PostConstruct
    public void init() {
        authorityService.createAuthority("USER", "Normal User");
//        authorityService.createAuthority("ADMIN", "Administrator");
    }
}
