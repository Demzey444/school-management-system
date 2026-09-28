package com.niit.sms.controller;

import com.niit.sms.dto.AttendanceRequest;
import com.niit.sms.dto.AttendanceSummaryResponse;
import com.niit.sms.dto.BulkAttendanceRequest;
import com.niit.sms.model.Attendance;
import com.niit.sms.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<Attendance> list(@RequestParam(required = false) String studentId,
                                 @RequestParam(required = false) String classId,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.findAll(studentId, classId, date);
    }

    @GetMapping("/{id}")
    public Attendance getOne(@PathVariable String id) {
        return attendanceService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Attendance> record(@Valid @RequestBody AttendanceRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.record(req));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Attendance>> recordBulk(@Valid @RequestBody BulkAttendanceRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.recordBulk(req));
    }

    @PutMapping("/{id}")
    public Attendance update(@PathVariable String id, @Valid @RequestBody AttendanceRequest req) {
        return attendanceService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public AttendanceSummaryResponse summary(@RequestParam String studentId) {
        return attendanceService.summaryFor(studentId);
    }
}