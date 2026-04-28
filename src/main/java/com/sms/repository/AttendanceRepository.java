package com.sms.repository;

import com.sms.model.Attendance;
import com.sms.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<Attendance> findByStudentIdOrderByDateDesc(Long studentId);
    List<Attendance> findByStudentIdAndDateBetweenOrderByDateDesc(Long studentId, LocalDate from, LocalDate to);
    long countByStudentId(Long studentId);
    long countByStudentIdAndStatus(Long studentId, AttendanceStatus status);
    void deleteByStudentId(Long studentId);
}
