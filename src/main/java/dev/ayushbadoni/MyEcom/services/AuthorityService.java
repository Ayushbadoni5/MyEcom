package dev.ayushbadoni.MyEcom.services;


import dev.ayushbadoni.MyEcom.entities.Authority;
import dev.ayushbadoni.MyEcom.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private  AuthorityRepository authorityRepository;

    public Authority getOrCreateAuthority(String roleCode, String roleDescription){
        return authorityRepository.findByRoleCode(roleCode)
                .stream()
                .findFirst()
                .orElseGet(()->{
                    Authority authority = Authority.builder()
                            .roleCode(roleCode)
                            .roleDescription(roleDescription)
                            .build();
                    return authorityRepository.save(authority);
                });
    }

    public List<Authority> getAuthorities(List<String> roles){
        List<Authority> authorities = new ArrayList<>();
        for (String role: roles){
            String formattedRole = role.toUpperCase();
                    if(formattedRole.equals("ROLE_ADMIN") || formattedRole.equals("ADMIN")){
                        authorities.add(getOrCreateAuthority("ROLE_ADMIN","Administrator access"));
                    } else {
                        authorities.add(getOrCreateAuthority("ROLE_USER","User access"));
                    }

        }
        return authorities;
    }

    public List<Authority> getDeafultUserAuthority(){
        return List.of(getOrCreateAuthority("USER","DEFAULT USER ACCESS"));
    }

}
