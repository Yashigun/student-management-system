package com.sms.controller;

import com.sms.dto.MarksSummary;
import com.sms.model.Marks;
import com.sms.service.MarksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marks")
@CrossOrigin(origins = "*")
public class MarksController {

    private final MarksService service;

    public MarksController(MarksService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Marks> add(@RequestBody Marks marks) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(marks));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<Marks>> get(
        @PathVariable Long studentId,
        @RequestParam(required = false) String examType
    ) {
        return ResponseEntity.ok(service.getByStudent(studentId, examType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}/summary")
    public ResponseEntity<MarksSummary> summary(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getSummary(studentId));
    }
}
