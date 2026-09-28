package com.niit.sms.controller;

import com.niit.sms.dto.SubjectRequest;
import com.niit.sms.model.Subject;
import com.niit.sms.service.SubjectService;
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
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> list(@RequestParam(required = false) String classId,
                              @RequestParam(required = false) String teacherId) {
        return subjectService.findAll(classId, teacherId);
    }

    @GetMapping("/{id}")
    public Subject getOne(@PathVariable String id) {
        return subjectService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Subject> create(@Valid @RequestBody SubjectRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.create(req));
    }

    @PutMapping("/{id}")
    public Subject update(@PathVariable String id, @Valid @RequestBody SubjectRequest req) {
        return subjectService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}