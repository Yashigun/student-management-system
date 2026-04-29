package com.sms.service;

import com.sms.dto.AttendanceSummary;
import com.sms.model.Attendance;
import com.sms.model.AttendanceStatus;
import com.sms.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public Attendance mark(Attendance attendance) {
        return repository.findByStudentIdAndDate(attendance.getStudentId(), attendance.getDate())
            .map(existing -> {
                existing.setStatus(attendance.getStatus());
                return repository.save(existing);
            })
            .orElseGet(() -> repository.save(attendance));
    }

    public List<Attendance> getByStudent(Long studentId, LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return repository.findByStudentIdAndDateBetweenOrderByDateDesc(studentId, from, to);
        }
        return repository.findByStudentIdOrderByDateDesc(studentId);
    }

    public AttendanceSummary getSummary(Long studentId) {
        long total = repository.countByStudentId(studentId);
        long present = repository.countByStudentIdAndStatus(studentId, AttendanceStatus.PRESENT);
        long absent = total - present;
        double pct = total == 0 ? 0.0 : Math.round(present * 10000.0 / total) / 100.0;
        return new AttendanceSummary(total, present, absent, pct);
    }
}
