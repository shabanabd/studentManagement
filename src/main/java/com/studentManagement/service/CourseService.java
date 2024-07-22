package com.studentManagement.service;

import com.studentManagement.dtos.CourseDto;
import com.studentManagement.dtos.EnrollmentDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CourseService {

    List<CourseDto> getCoursesByStudentId(long studentName);
    List<CourseDto> getAllCourses();
    boolean registCourse(long courseId, long studentId);
    boolean cancelCourse(EnrollmentDto enrollmentDto, long studentId);
    void exportStudentSchedule(HttpServletResponse response, long studentId);
}
