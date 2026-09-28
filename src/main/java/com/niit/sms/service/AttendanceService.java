package com.niit.sms.service;

import com.niit.sms.dto.AttendanceRequest;
import com.niit.sms.dto.AttendanceSummaryResponse;
import com.niit.sms.dto.BulkAttendanceEntry;
import com.niit.sms.dto.BulkAttendanceRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Attendance;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Student;
import com.niit.sms.model.enums.AttendanceStatus;
import com.niit.sms.repository.AttendanceRepository;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             ClassRepository classRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    public List<Attendance> findAll(String studentId, String classId, LocalDate date) {
        if (classId != null && !classId.isBlank() && date != null) {
            return attendanceRepository.findByClassIdAndDate(classId, date);
        }
        if (studentId != null && !studentId.isBlank()) {
            return attendanceRepository.findByStudentId(studentId);
        }
        if (classId != null && !classId.isBlank()) {
            return attendanceRepository.findByClassId(classId);
        }
        return attendanceRepository.findAll();
    }

    public Attendance findById(String id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
    }

    public Attendance record(AttendanceRequest req) {
        attendanceRepository.findByStudentIdAndDate(req.studentId(), req.date())
                .ifPresent(a -> {
                    throw new DuplicateResourceException(
                            "Attendance for this student on " + req.date() + " already exists");
                });

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + req.studentId()));

        Attendance a = new Attendance();
        populate(a, student, req.date(), req.status(), req.remark(), null, null);
        return attendanceRepository.save(a);
    }

    public List<Attendance> recordBulk(BulkAttendanceRequest req) {
        SchoolClass sc = classRepository.findById(req.classId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + req.classId()));

        List<Attendance> saved = new ArrayList<>();

        for (BulkAttendanceEntry entry : req.entries()) {
            Student student = studentRepository.findById(entry.studentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + entry.studentId()));

            Attendance existing = attendanceRepository
                    .findByStudentIdAndDate(entry.studentId(), req.date())
                    .orElse(null);

            if (existing != null) {
                existing.setStatus(entry.status());
                existing.setRemark(entry.remark());
                existing.setClassId(sc.getId());
                existing.setClassName(sc.getName());
                saved.add(attendanceRepository.save(existing));
            } else {
                Attendance a = new Attendance();
                populate(a, student, req.date(), entry.status(), entry.remark(), sc.getId(), sc.getName());
                saved.add(attendanceRepository.save(a));
            }
        }

        return saved;
    }

    public Attendance update(String id, AttendanceRequest req) {
        Attendance a = findById(id);

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + req.studentId()));

        a.setStudentId(student.getId());
        a.setStudentName(student.getFullName());
        a.setAdmissionNumber(student.getAdmissionNumber());
        a.setClassId(student.getClassId());
        a.setClassName(student.getClassName());
        a.setDate(req.date());
        a.setStatus(req.status());
        a.setRemark(req.remark());

        return attendanceRepository.save(a);
    }

    public void delete(String id) {
        Attendance a = findById(id);
        attendanceRepository.delete(a);
    }

    public AttendanceSummaryResponse summaryFor(String studentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<Attendance> records = attendanceRepository.findByStudentId(studentId);
        long present = records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absent  = records.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long total   = records.size();
        double pct = total == 0 ? 0.0 : Math.round((present * 10000.0 / total)) / 100.0;

        return new AttendanceSummaryResponse(s.getId(), s.getFullName(), present, absent, total, pct);
    }

    private void populate(Attendance a, Student student, LocalDate date,
                          AttendanceStatus status, String remark,
                          String classIdOverride, String classNameOverride) {
        a.setStudentId(student.getId());
        a.setStudentName(student.getFullName());
        a.setAdmissionNumber(student.getAdmissionNumber());
        a.setClassId(classIdOverride != null ? classIdOverride : student.getClassId());
        a.setClassName(classNameOverride != null ? classNameOverride : student.getClassName());
        a.setDate(date);
        a.setStatus(status);
        a.setRemark(remark);
    }
}