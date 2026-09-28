package com.niit.sms.controller;

import com.niit.sms.dto.ClassRequest;
import com.niit.sms.model.SchoolClass;
import com.niit.sms.service.ClassService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public List<SchoolClass> list() {
        return classService.findAll();
    }

    @GetMapping("/{id}")
    public SchoolClass getOne(@PathVariable String id) {
        return classService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SchoolClass> create(@Valid @RequestBody ClassRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classService.create(req));
    }

    @PutMapping("/{id}")
    public SchoolClass update(@PathVariable String id, @Valid @RequestBody ClassRequest req) {
        return classService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        classService.delete(id);
        return ResponseEntity.noContent().build();
    }
}