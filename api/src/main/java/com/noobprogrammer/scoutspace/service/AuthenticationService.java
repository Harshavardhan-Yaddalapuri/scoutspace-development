package com.noobprogrammer.scoutspace.service;

import com.noobprogrammer.scoutspace.auth.AuthenticationRequest;
import com.noobprogrammer.scoutspace.config.JwtService;
import com.noobprogrammer.scoutspace.model.User;
import com.noobprogrammer.scoutspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<Map<String, String>> signUp(User user) {
        Map<String, String> response = new HashMap<>();

        // Check if the username is already taken
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.put("message", "Username already exists: " + user.getUsername());
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        // Check if the email is already taken
        if (userRepository.findByEmail(user.getEmail()) != null) {
            response.put("message", "Email already exists: " + user.getEmail());
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        // Encrypt the password using BCrypt
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setCreatedby(user.getUsername());
        user.setCreateddate(new Date());

        // Save the user in the database
        userRepository.save(user);
        // Generate JWT token
        String token = jwtService.generateToken(user);
        response.put("message", "User registered successfully");
        response.put("token", token);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }


    public ResponseEntity<Map<String, String>> signIn(AuthenticationRequest request) {
        User user = userRepository.findByEmailOrUsername(request.getEmail(), request.getPassword());
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("success", "false");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Verify the provided password against the stored hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.put("message", "Invalid credentials");
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }


        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));

        // If authentication is successful, update SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        response.put("message", "User signed in successfully");
        response.put("token", token);
        response.put("success", "true");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
