package com.niit.sms.repository;

import com.niit.sms.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByStudentId(String studentId);
    List<Attendance> findByClassId(String classId);
    List<Attendance> findByClassIdAndDate(String classId, LocalDate date);
    List<Attendance> findByStudentIdAndDateBetween(String studentId, LocalDate from, LocalDate to);

    Optional<Attendance> findByStudentIdAndDate(String studentId, LocalDate date);
}
