package com.noobprogrammer.scoutspace.controller;

import com.noobprogrammer.scoutspace.auth.AuthenticationRequest;
import com.noobprogrammer.scoutspace.model.User;
import com.noobprogrammer.scoutspace.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody User user) {
        return authenticationService.signUp(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody AuthenticationRequest request) {
        return authenticationService.signIn(request);
    }

}
