package com.studentManagement.controller;

import com.studentManagement.dtos.JwtAuthResponse;
import com.studentManagement.dtos.JwtRequest;
import com.studentManagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class JwtAuthenticationController {

    private final AuthService authService;

    @Autowired
    public JwtAuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtRequest jwtRequest){
        String token = authService.login(jwtRequest);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}
