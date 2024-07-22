package com.studentManagement.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.studentManagement.dtos.CourseDto;
import com.studentManagement.dtos.EnrollmentDto;
import com.studentManagement.exceptions.MessagingException;
import com.studentManagement.exceptions.ResourceNotFoundException;
import com.studentManagement.models.Course;
import com.studentManagement.models.Enrollment;
import com.studentManagement.models.EnrollmentId;
import com.studentManagement.models.Session;
import com.studentManagement.models.User;
import com.studentManagement.repositories.CourseRepository;
import com.studentManagement.repositories.EnrollmentRepository;
import com.studentManagement.repositories.UserRepository;
import com.studentManagement.utils.DtoConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = "myCourses", key = "#studentId")
    @Override
    public List<CourseDto> getCoursesByStudentId(long studentId) {
        List<CourseDto> courseDtos = new ArrayList<>();
        User user = userRepository.findOne(studentId);
        if (user == null) {
            logger.error("User doesn't exist!");
            throw new ResourceNotFoundException("User doesn't exist!");
        }
        for (Enrollment enrollment : user.getEnrollments()) {
            if (enrollment.getCancelled() == null || enrollment.getCancelled() == 0) {
                courseDtos.add(DtoConverterUtil.toCourseDto(enrollment.getCourse()));//courseRepository.findOne(enrollment.getId().getCourseId())));
            }
        }
        return courseDtos;
    }

    @Cacheable(cacheNames = "availableCourses")
    @Override
    public List<CourseDto> getAllCourses() {
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            courseDtos.add(DtoConverterUtil.toCourseDto(course));
        }
        return courseDtos;
    }

    @CacheEvict(cacheNames = "myCourses", key = "#studentId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean registCourse(long courseId, long studentId) {
        Course course = courseRepository.findOne(courseId);
        if (course == null)
            throw new ResourceNotFoundException("Course " + courseId + " doesn't exist!");
        User user = userRepository.findOne(studentId);
        if (user == null)
            throw new ResourceNotFoundException("User doesn't exist!");
        EnrollmentId enrollmentId = new EnrollmentId();
        enrollmentId.setCourseId(courseId);
        enrollmentId.setUserId(studentId);
        Enrollment enrollment = enrollmentRepository.findOne(enrollmentId);
        if (enrollment != null) {
            if (enrollment.getCancelled() == 0) {
                logger.error("User is already registered!");
                throw new MessagingException("You are already registered!");
            }
            enrollment.setCancelled(0);
            enrollment.setCancellationReason("");
            enrollmentRepository.save(enrollment);
        } else {
            enrollment = new Enrollment();
            enrollment.setId(enrollmentId);
            enrollment.setCourse(course);
            enrollment.setUser(user);
            enrollment.setCancelled(0);
            enrollmentRepository.save(enrollment);
        }
        return true;
    }

    @CacheEvict(cacheNames = "myCourses", key = "#studentId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean cancelCourse(EnrollmentDto enrollmentDto, long studentId) {
        Course course = courseRepository.findOne(enrollmentDto.getCourseId());
        if (course == null)
            throw new ResourceNotFoundException("Course " + enrollmentDto.getCourseId() + " doesn't exist!");
        User user = userRepository.findOne(studentId);
        if (user == null)
            throw new ResourceNotFoundException("User doesn't exist!");

        EnrollmentId enrollmentId = new EnrollmentId();
        enrollmentId.setCourseId(enrollmentDto.getCourseId());
        enrollmentId.setUserId(studentId);
        Enrollment enrollment = enrollmentRepository.findOne(enrollmentId);
        if (enrollment == null || enrollment.getCancelled() == 1) {
            logger.error("User has not already registered for this course!");
            throw new MessagingException("You have not already registered for this course!");
        } else {
            enrollment.setCancelled(1);
            enrollment.setCancellationReason(enrollmentDto.getCancellationReason());
            enrollmentRepository.save(enrollment);
        }

        return true;
    }

    @Override
    public void exportStudentSchedule(HttpServletResponse response, long studentId) {
        try {
            User user = userRepository.findOne(studentId);
            if (user == null)
                throw new ResourceNotFoundException("User doesn't exist!");

            DateFormat dateFormat = new SimpleDateFormat("HH:MM");

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontTiltle.setSize(20);

            Paragraph paragraph = new Paragraph("Student Schedule", fontTiltle);

            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(paragraph);

            PdfPTable table = new PdfPTable(6);

            table.setWidthPercentage(100f);
            table.setWidths(new int[]{3, 3, 3, 3, 3, 3});
            table.setSpacingBefore(5);

            PdfPCell cell = new PdfPCell();

            cell.setBackgroundColor(CMYKColor.MAGENTA);
            cell.setPadding(5);

            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            font.setColor(CMYKColor.WHITE);

            cell.setPhrase(new Phrase("Course ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Course Name", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Teachers", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Day", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Start Time", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("End Time", font));
            table.addCell(cell);

            for (Enrollment enrollment : user.getEnrollments()) {
                if (enrollment.getCancelled() == null || enrollment.getCancelled() == 0) {
                    Course course = enrollment.getCourse();

                    String teacherName = "";
                    for (User teacher : course.getTeachers()) {
                        if (!teacherName.isEmpty())
                            teacherName+= " - ";
                        teacherName+= teacher.getName();
                    }

                    for (Session session: course.getSessions()) {
                        table.addCell(String.valueOf(course.getId()));
                        table.addCell(course.getName());
                        table.addCell(teacherName);
                        table.addCell(session.getDay());
                        table.addCell(dateFormat.format(session.getStartTime()));
                        table.addCell(dateFormat.format(session.getEndTime()));
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new MessagingException(ex.getMessage());
        }
    }

}
