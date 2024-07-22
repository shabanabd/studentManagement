package com.studentManagement.utils;

import com.studentManagement.dtos.CourseDto;
import com.studentManagement.dtos.UserDto;
import com.studentManagement.models.Course;
import com.studentManagement.models.User;

public class DtoConverterUtil {
    public static CourseDto toCourseDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());
        courseDto.setStartDate(course.getStartDate());
        courseDto.setEndDate(course.getEndDate());
        return courseDto;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setType(user.getType());
        return userDto;
    }
}
