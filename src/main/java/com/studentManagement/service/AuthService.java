package com.studentManagement.service;

import com.studentManagement.dtos.JwtRequest;

public interface AuthService {
    String login(JwtRequest jwtRequest);
}
