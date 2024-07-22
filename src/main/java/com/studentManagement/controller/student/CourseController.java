package com.studentManagement.controller.student;

import com.studentManagement.controller.BaseController;
import com.studentManagement.dtos.CourseDto;
import com.studentManagement.dtos.EnrollmentDto;
import com.studentManagement.service.CourseService;
import com.studentManagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/student/course")
@PreAuthorize("hasAuthority('student')")
public class CourseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/mycourses")
    public ResponseEntity<List<CourseDto>> getMyCourses() {
        return new ResponseEntity<>(courseService.getCoursesByStudentId(getCurrentUser().getUserId()), HttpStatus.OK);
    }

    @GetMapping("/allcourses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<String> registCourse(@RequestBody EnrollmentDto enrollmentDto) {
        boolean saved = courseService.registCourse(enrollmentDto.getCourseId(), getCurrentUser().getUserId());
        logger.info("#Regist course ");
        if (saved)
            return new ResponseEntity<>("Registed", HttpStatus.OK);
        else
            return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/cancelcourse")
    public ResponseEntity<String> cancelCourse(@RequestBody EnrollmentDto enrollmentDto) {
        boolean saved = courseService.cancelCourse(enrollmentDto, getCurrentUser().getUserId());
        logger.info("#cancel course ");
        if (saved)
            return new ResponseEntity<>("Course has been cancelled", HttpStatus.OK);
        else
            return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/schedule")
    public void exportSchedule(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=schedule.pdf";
        response.setHeader(headerkey, headervalue);
        courseService.exportStudentSchedule(response, getCurrentUser().getUserId());
    }
}
