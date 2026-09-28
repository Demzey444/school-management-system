package com.niit.sms.service;

import com.niit.sms.dto.AttendanceSummaryResponse;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.model.Attendance;
import com.niit.sms.model.Student;
import com.niit.sms.model.enums.AttendanceStatus;
import com.niit.sms.repository.AttendanceRepository;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttendanceServiceTest {

    private AttendanceRepository attendanceRepository;
    private StudentRepository studentRepository;
    private ClassRepository classRepository;
    private AttendanceService attendanceService;

    @BeforeEach
    void setup() {
        attendanceRepository = mock(AttendanceRepository.class);
        studentRepository = mock(StudentRepository.class);
        classRepository = mock(ClassRepository.class);
        attendanceService = new AttendanceService(attendanceRepository, studentRepository, classRepository);
    }

    @Test
    void shouldRejectDuplicateAttendanceForSameDay() {
        Attendance existing = new Attendance();
        when(attendanceRepository.findByStudentIdAndDate("s1", LocalDate.of(2026, 1, 1)))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() ->
                attendanceService.record(new com.niit.sms.dto.AttendanceRequest(
                        "s1", LocalDate.of(2026, 1, 1), AttendanceStatus.PRESENT, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void shouldComputeAttendanceSummary() {
        Student s = new Student();
        s.setId("s1");
        s.setFullName("John Doe");
        when(studentRepository.findById("s1")).thenReturn(Optional.of(s));

        Attendance a1 = new Attendance(); a1.setStatus(AttendanceStatus.PRESENT);
        Attendance a2 = new Attendance(); a2.setStatus(AttendanceStatus.PRESENT);
        Attendance a3 = new Attendance(); a3.setStatus(AttendanceStatus.ABSENT);
        Attendance a4 = new Attendance(); a4.setStatus(AttendanceStatus.PRESENT);

        when(attendanceRepository.findByStudentId("s1")).thenReturn(List.of(a1, a2, a3, a4));

        AttendanceSummaryResponse summary = attendanceService.summaryFor("s1");

        assertThat(summary.present()).isEqualTo(3);
        assertThat(summary.absent()).isEqualTo(1);
        assertThat(summary.total()).isEqualTo(4);
        assertThat(summary.percentage()).isEqualTo(75.0);
    }

    @Test
    void shouldReturnZeroPercentWhenNoRecords() {
        Student s = new Student();
        s.setId("s1");
        when(studentRepository.findById("s1")).thenReturn(Optional.of(s));
        when(attendanceRepository.findByStudentId("s1")).thenReturn(List.of());

        AttendanceSummaryResponse summary = attendanceService.summaryFor("s1");
        assertThat(summary.percentage()).isEqualTo(0.0);
    }
}