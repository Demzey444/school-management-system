package com.niit.sms.controller;

import com.niit.sms.dto.ReportCardResponse;
import com.niit.sms.dto.ResultRequest;
import com.niit.sms.model.Result;
import com.niit.sms.model.enums.Term;
import com.niit.sms.service.ResultService;
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
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<Result> list(@RequestParam(required = false) String studentId,
                             @RequestParam(required = false) String classId,
                             @RequestParam(required = false) String subjectId,
                             @RequestParam(required = false) String session) {
        return resultService.findAll(studentId, classId, subjectId, session);
    }

    @GetMapping("/{id}")
    public Result getOne(@PathVariable String id) {
        return resultService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Result> create(@Valid @RequestBody ResultRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.create(req));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @Valid @RequestBody ResultRequest req) {
        return resultService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        resultService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report/{studentId}")
    public ReportCardResponse reportCard(@PathVariable String studentId,
                                         @RequestParam String session,
                                         @RequestParam Term term) {
        return resultService.buildReportCard(studentId, session, term);
    }
}