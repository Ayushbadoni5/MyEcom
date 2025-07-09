package dev.ayushbadoni.MyEcom.auth.controller;


import dev.ayushbadoni.MyEcom.auth.config.JWTTokenHelper;
import dev.ayushbadoni.MyEcom.auth.entities.User;
import dev.ayushbadoni.MyEcom.auth.services.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/oauth")
public class OAuth2Controller {

    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> callbackAuth2(@AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {
        String userName = oAuth2User.getAttribute("email");
        User user = oAuth2Service.getUser(userName);
        if (null==user){
            user = oAuth2Service.cresteUser(oAuth2User,"google");
        }
        String token =  jwtTokenHelper.generateToken(user.getUsername());
        Map<String , String> response = new HashMap<>();
        response.put("token",token);
        return  ResponseEntity.ok(response);

    }
}
