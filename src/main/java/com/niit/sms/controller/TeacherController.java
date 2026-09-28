package com.niit.sms.controller;

import com.niit.sms.dto.TeacherRequest;
import com.niit.sms.model.Teacher;
import com.niit.sms.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> list(@RequestParam(required = false) String search) {
        return teacherService.search(search);
    }

    @GetMapping("/{id}")
    public Teacher getOne(@PathVariable String id) {
        return teacherService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Teacher> create(@Valid @RequestBody TeacherRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(req));
    }

    @PutMapping("/{id}")
    public Teacher update(@PathVariable String id, @Valid @RequestBody TeacherRequest req) {
        return teacherService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}