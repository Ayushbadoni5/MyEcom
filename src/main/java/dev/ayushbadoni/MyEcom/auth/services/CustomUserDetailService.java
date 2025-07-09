package dev.ayushbadoni.MyEcom.auth.services;

import dev.ayushbadoni.MyEcom.auth.entities.User;
import dev.ayushbadoni.MyEcom.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDetailRepository.findByEmail(username);
        if (null == user){
            throw new UsernameNotFoundException("User Not found with UserName" + username);
        }

        return user;
    }
}
