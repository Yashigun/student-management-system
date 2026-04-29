package com.sms.controller;

import com.sms.dto.AttendanceSummary;
import com.sms.model.Attendance;
import com.sms.service.AttendanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Attendance> mark(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(service.mark(attendance));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<Attendance>> get(
        @PathVariable Long studentId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(service.getByStudent(studentId, from, to));
    }

    @GetMapping("/{studentId}/summary")
    public ResponseEntity<AttendanceSummary> summary(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getSummary(studentId));
    }
}
