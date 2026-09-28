package com.niit.sms.controller;

import com.niit.sms.dto.CurrentUserResponse;
import com.niit.sms.dto.LoginRequest;
import com.niit.sms.model.Student;
import com.niit.sms.model.Teacher;
import com.niit.sms.model.User;
import com.niit.sms.repository.StudentRepository;
import com.niit.sms.repository.TeacherRepository;
import com.niit.sms.repository.UserRepository;
import com.niit.sms.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SecurityUtils securityUtils;

    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepository,
                          StudentRepository studentRepository,
                          TeacherRepository teacherRepository,
                          SecurityUtils securityUtils) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/login")
    public CurrentUserResponse login(@Valid @RequestBody LoginRequest req,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password()));

            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            new HttpSessionSecurityContextRepository().saveContext(ctx, request, response);

            User u = userRepository.findByUsername(req.username()).orElseThrow();
            return toCurrentUser(u);
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public CurrentUserResponse me() {
        return toCurrentUser(securityUtils.currentUser());
    }

    private CurrentUserResponse toCurrentUser(User u) {
        String fullName = u.getUsername();
        if (u.getRole() == com.niit.sms.model.enums.Role.STUDENT && u.getProfileId() != null) {
            fullName = studentRepository.findById(u.getProfileId()).map(Student::getFullName).orElse(fullName);
        } else if (u.getRole() == com.niit.sms.model.enums.Role.TEACHER && u.getProfileId() != null) {
            fullName = teacherRepository.findById(u.getProfileId()).map(Teacher::getFullName).orElse(fullName);
        }
        return new CurrentUserResponse(
                u.getId(), u.getUsername(), u.getEmail(),
                u.getRole().name(), u.getProfileId(), fullName);
    }
}