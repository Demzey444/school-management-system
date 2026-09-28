package com.niit.sms.service;

import com.niit.sms.dto.TeacherRequest;
import com.niit.sms.exception.DuplicateResourceException;
import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.User;
import com.niit.sms.model.enums.Role;
import com.niit.sms.repository.TeacherRepository;
import com.niit.sms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);
    private static final String DEFAULT_TEACHER_PASSWORD = "teacher123";

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> findAll() { return teacherRepository.findAll(); }

    public List<Teacher> search(String term) {
        if (term == null || term.isBlank()) return teacherRepository.findAll();
        return teacherRepository.search(term);
    }

    public Teacher findById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }

    public Teacher create(TeacherRequest req) {
        if (teacherRepository.existsByStaffNumber(req.staffNumber())) {
            throw new DuplicateResourceException(
                    "A teacher with staff number '" + req.staffNumber() + "' already exists");
        }
        Teacher t = new Teacher();
        applyRequest(t, req);
        Teacher saved = teacherRepository.save(t);

        try {
            User u = new User();
            u.setUsername(saved.getStaffNumber());
            u.setEmail(saved.getEmail());
            u.setPassword(passwordEncoder.encode(DEFAULT_TEACHER_PASSWORD));
            u.setRole(Role.TEACHER);
            u.setProfileId(saved.getId());
            User savedUser = userRepository.save(u);

            saved.setUserId(savedUser.getId());
            saved = teacherRepository.save(saved);

            log.info("Created login for teacher {} (username={}, default password={})",
                    saved.getFullName(), saved.getStaffNumber(), DEFAULT_TEACHER_PASSWORD);
        } catch (Exception ex) {
            log.error("Failed to create login for teacher {}: {}", saved.getFullName(), ex.getMessage());
        }

        return saved;
    }

    public Teacher update(String id, TeacherRequest req) {
        Teacher t = findById(id);
        if (!t.getStaffNumber().equals(req.staffNumber())
                && teacherRepository.existsByStaffNumber(req.staffNumber())) {
            throw new DuplicateResourceException(
                    "A teacher with staff number '" + req.staffNumber() + "' already exists");
        }
        applyRequest(t, req);
        return teacherRepository.save(t);
    }

    public void delete(String id) {
        Teacher t = findById(id);
        if (t.getUserId() != null) {
            userRepository.findById(t.getUserId()).ifPresent(userRepository::delete);
        }
        teacherRepository.delete(t);
    }

    private void applyRequest(Teacher t, TeacherRequest req) {
        t.setStaffNumber(req.staffNumber());
        t.setFullName(req.fullName());
        t.setEmail(req.email());
        t.setPhone(req.phone());
        t.setGender(req.gender());
        t.setQualification(req.qualification());
        t.setSubjectIds(req.subjectIds() != null ? new ArrayList<>(req.subjectIds()) : new ArrayList<>());
        t.setClassIds(req.classIds() != null ? new ArrayList<>(req.classIds()) : new ArrayList<>());
    }
}