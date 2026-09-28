package com.niit.sms.service;

import com.niit.sms.dto.StudentRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.model.Student;
import com.niit.sms.model.User;
import com.niit.sms.model.enums.Role;
import com.niit.sms.repository.ClassRepository;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final String DEFAULT_STUDENT_PASSWORD = "student123";

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          ClassRepository classRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> findAll() { return studentRepository.findAll(); }

    public List<Student> search(String term) {
        if (term == null || term.isBlank()) return studentRepository.findAll();
        return studentRepository.search(term);
    }

    public Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student create(StudentRequest req) {
        if (studentRepository.existsByAdmissionNumber(req.admissionNumber())) {
            throw new DuplicateResourceException(
                    "A student with admission number '" + req.admissionNumber() + "' already exists");
        }

        Student s = new Student();
        applyRequest(s, req);
        Student saved = studentRepository.save(s);

        // Create User login: username = admissionNumber, default password
        try {
            User u = new User();
            u.setUsername(saved.getAdmissionNumber());
            u.setEmail(saved.getEmail());
            u.setPassword(passwordEncoder.encode(DEFAULT_STUDENT_PASSWORD));
            u.setRole(Role.STUDENT);
            u.setProfileId(saved.getId());
            User savedUser = userRepository.save(u);

            saved.setUserId(savedUser.getId());
            saved = studentRepository.save(saved);

            log.info("Created login for student {} (username={}, default password={})",
                    saved.getFullName(), saved.getAdmissionNumber(), DEFAULT_STUDENT_PASSWORD);
        } catch (Exception ex) {
            log.error("Failed to create login for student {}: {}", saved.getFullName(), ex.getMessage());
        }

        return saved;
    }

    public Student update(String id, StudentRequest req) {
        Student s = findById(id);
        if (!s.getAdmissionNumber().equals(req.admissionNumber())
                && studentRepository.existsByAdmissionNumber(req.admissionNumber())) {
            throw new DuplicateResourceException(
                    "A student with admission number '" + req.admissionNumber() + "' already exists");
        }
        applyRequest(s, req);
        return studentRepository.save(s);
    }

    public void delete(String id) {
        Student s = findById(id);
        if (s.getUserId() != null) {
            userRepository.findById(s.getUserId()).ifPresent(userRepository::delete);
        }
        studentRepository.delete(s);
    }

    private void applyRequest(Student s, StudentRequest req) {
        s.setAdmissionNumber(req.admissionNumber());
        s.setFullName(req.fullName());
        s.setEmail(req.email());
        s.setPhone(req.phone());
        s.setGender(req.gender());
        s.setDateOfBirth(req.dateOfBirth());
        s.setAddress(req.address());

        if (req.classId() != null && !req.classId().isBlank()) {
            SchoolClass sc = classRepository.findById(req.classId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + req.classId()));
            s.setClassId(sc.getId());
            s.setClassName(sc.getName());
        } else {
            s.setClassId(null);
            s.setClassName(null);
        }
    }
}