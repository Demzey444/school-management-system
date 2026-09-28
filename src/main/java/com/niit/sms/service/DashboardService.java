package com.niit.sms.service;

import com.niit.sms.dto.AdminDashboardResponse;
import com.niit.sms.dto.StudentDashboardResponse;
import com.niit.sms.dto.TeacherDashboardResponse;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Result;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Student;
import com.niit.sms.model.Subject;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.enums.AttendanceStatus;
import com.niit.sms.repository.AttendanceRepository;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.ResultRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.SubjectRepository;
import com.niit.sms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final ResultRepository resultRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardService(StudentRepository studentRepository,
                            TeacherRepository teacherRepository,
                            ClassRepository classRepository,
                            SubjectRepository subjectRepository,
                            ResultRepository resultRepository,
                            AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.resultRepository = resultRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public AdminDashboardResponse adminStats() {
        return new AdminDashboardResponse(
                studentRepository.count(),
                teacherRepository.count(),
                classRepository.count(),
                subjectRepository.count(),
                resultRepository.count(),
                attendanceRepository.count()
        );
    }

    public TeacherDashboardResponse teacherStats(String teacherId) {
        Teacher t = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));

        List<Subject> mySubjects = subjectRepository.findByTeacherId(teacherId);

        List<SchoolClass> myClasses = t.getClassIds() == null ? List.of()
                : classRepository.findAllById(t.getClassIds());

        return new TeacherDashboardResponse(
                t.getFullName(),
                mySubjects.size(),
                myClasses.size(),
                mySubjects,
                myClasses
        );
    }

    public StudentDashboardResponse studentStats(String studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        List<Result> results = resultRepository.findByStudentId(studentId);

        var attendance = attendanceRepository.findByStudentId(studentId);
        long total = attendance.size();
        long present = attendance.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        double pct = total == 0 ? 0.0 : Math.round((present * 10000.0 / total)) / 100.0;

        List<Result> recent = results.size() <= 5 ? results : results.subList(0, 5);

        return new StudentDashboardResponse(
                s.getFullName(),
                s.getAdmissionNumber(),
                s.getClassName(),
                results.size(),
                total,
                pct,
                recent
        );
    }
}