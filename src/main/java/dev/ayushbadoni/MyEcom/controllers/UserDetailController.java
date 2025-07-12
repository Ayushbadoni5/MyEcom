package dev.ayushbadoni.MyEcom.controllers;


import dev.ayushbadoni.MyEcom.dto.UserDetailDto;
import dev.ayushbadoni.MyEcom.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserDetailController {
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/profile")
    public ResponseEntity<UserDetailDto> getUserProfile(Principal loggedInUser){
        User user = (User) userDetailsService.loadUserByUsername(loggedInUser.getName());
        if (null == user){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetailDto userDetailDto = UserDetailDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .addressList(user.getAddressList())
                .authorityList(user.getAuthorities().toArray())
                .build();

        return new ResponseEntity<>(userDetailDto, HttpStatus.OK);
    }
}
