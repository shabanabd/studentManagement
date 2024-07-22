package com.studentManagement.service;

import com.studentManagement.dtos.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
}
