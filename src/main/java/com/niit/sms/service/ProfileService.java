package com.niit.sms.service;

import com.niit.sms.exception.ResourceNotFoundException;
import com.niit.sms.model.Student;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.User;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public ProfileService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public Map<String, Object> profileFor(User u) {
        Map<String, Object> out = new HashMap<>();
        out.put("userId", u.getId());
        out.put("username", u.getUsername());
        out.put("email", u.getEmail());
        out.put("role", u.getRole().name());

        switch (u.getRole()) {
            case STUDENT -> {
                Student s = studentRepository.findById(u.getProfileId())
                        .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
                out.put("profile", s);
            }
            case TEACHER -> {
                Teacher t = teacherRepository.findById(u.getProfileId())
                        .orElseThrow(() -> new ResourceNotFoundException("Teacher profile not found"));
                out.put("profile", t);
            }
            default -> out.put("profile", null);
        }
        return out;
    }
}