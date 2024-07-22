package com.studentManagement.service;

import com.studentManagement.dtos.UserDto;
import com.studentManagement.models.User;
import com.studentManagement.repositories.UserRepository;
import com.studentManagement.utils.DtoConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Cacheable(cacheNames = "allUsers")
    @Override
    public List<UserDto> getAllUsers() {
        logger.info("# getAllUsers");
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            userDtoList.add(DtoConverterUtil.toUserDto(user));
        }
        return userDtoList;
    }
}
