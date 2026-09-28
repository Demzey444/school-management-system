package com.niit.sms.service;

import com.niit.sms.dto.TeacherRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.enums.Gender;
import com.niit.sms.repository.TeacherRepository;
import com.niit.sms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherServiceTest {

    private TeacherRepository teacherRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TeacherService teacherService;

    @BeforeEach
    void setup() {
        teacherRepository = mock(TeacherRepository.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        teacherService = new TeacherService(teacherRepository, userRepository, passwordEncoder);
    }

    private TeacherRequest sampleRequest(String staffNumber) {
        return new TeacherRequest(
                staffNumber, "Mrs. Ada Obi", "ada@school.com", "08011112222",
                Gender.FEMALE, "B.Sc Mathematics", List.of(), List.of()
        );
    }

    @Test
    void shouldCreateTeacher() {
        when(teacherRepository.existsByStaffNumber("TCH/2025/001")).thenReturn(false);
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("hashed");

        Teacher t = teacherService.create(sampleRequest("TCH/2025/001"));

        assertThat(t.getStaffNumber()).isEqualTo("TCH/2025/001");
        assertThat(t.getFullName()).isEqualTo("Mrs. Ada Obi");
    }

    @Test
    void shouldRejectDuplicateStaffNumber() {
        when(teacherRepository.existsByStaffNumber("TCH/2025/001")).thenReturn(true);

        assertThatThrownBy(() -> teacherService.create(sampleRequest("TCH/2025/001")))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void shouldFindTeacher() {
        Teacher t = new Teacher();
        t.setId("t1");
        t.setFullName("Ada");
        when(teacherRepository.findById("t1")).thenReturn(Optional.of(t));

        assertThat(teacherService.findById("t1").getFullName()).isEqualTo("Ada");
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(teacherRepository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> teacherService.findById("x"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}