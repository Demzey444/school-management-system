package com.niit.sms.service;

import com.niit.sms.dto.StudentRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Student;
import com.niit.sms.model.enums.Gender;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceTest {

    private StudentRepository studentRepository;
    private ClassRepository classRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private StudentService studentService;

    @BeforeEach
    void setup() {
        studentRepository = mock(StudentRepository.class);
        classRepository = mock(ClassRepository.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        studentService = new StudentService(studentRepository, classRepository, userRepository, passwordEncoder);
    }

    private StudentRequest sampleRequest(String admissionNumber) {
        return new StudentRequest(
                admissionNumber, "John Doe", "john@school.com", "08012345678",
                Gender.MALE, LocalDate.of(2010, 5, 14), null, "12 Lagos Street"
        );
    }

    @Test
    void shouldCreateStudent() {
        when(studentRepository.existsByAdmissionNumber("STU/2025/001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("hashed");

        studentService.create(sampleRequest("STU/2025/001"));

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        org.mockito.Mockito.verify(studentRepository, org.mockito.Mockito.atLeastOnce()).save(captor.capture());
        Student saved = captor.getValue();

        assertThat(saved.getAdmissionNumber()).isEqualTo("STU/2025/001");
        assertThat(saved.getFullName()).isEqualTo("John Doe");
        assertThat(saved.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void shouldRejectDuplicateAdmissionNumber() {
        when(studentRepository.existsByAdmissionNumber("STU/2025/001")).thenReturn(true);

        assertThatThrownBy(() -> studentService.create(sampleRequest("STU/2025/001")))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldFindStudentById() {
        Student s = new Student();
        s.setId("abc");
        s.setFullName("Jane");
        when(studentRepository.findById("abc")).thenReturn(Optional.of(s));

        Student found = studentService.findById("abc");
        assertThat(found.getFullName()).isEqualTo("Jane");
    }

    @Test
    void shouldThrowWhenStudentNotFound() {
        when(studentRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteStudent() {
        Student s = new Student();
        s.setId("abc");
        when(studentRepository.findById("abc")).thenReturn(Optional.of(s));

        studentService.delete("abc");

        org.mockito.Mockito.verify(studentRepository).delete(s);
    }
}