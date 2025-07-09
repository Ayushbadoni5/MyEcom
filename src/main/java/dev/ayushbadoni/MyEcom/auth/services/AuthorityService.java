package dev.ayushbadoni.MyEcom.auth.services;


import dev.ayushbadoni.MyEcom.auth.entities.Authority;
import dev.ayushbadoni.MyEcom.auth.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private  AuthorityRepository authorityRepository;


    public List<Authority> getUserAuthority(){
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findByRoleCode("ROLE_USER");
        authorities.add(authority);
        return authorities;
    }
    public Authority createAuthority(String role, String description){
        Authority authority =Authority.builder().roleCode("ROLE_" + role.toUpperCase()).roleDescription(description).build();
        return authorityRepository.save(authority);
    }
}
