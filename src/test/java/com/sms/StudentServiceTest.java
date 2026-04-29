package com.sms;

import com.sms.model.Student;
import com.sms.model.StudentStatus;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.MarksRepository;
import com.sms.repository.StudentRepository;
import com.sms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentRepository repository;
    @Mock AttendanceRepository attendanceRepository;
    @Mock MarksRepository marksRepository;

    @InjectMocks StudentService service;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Alice");
        student.setEmail("alice@example.com");
        student.setClassName("10");
        student.setSection("A");
        student.setStatus(StudentStatus.ACTIVE);
    }

    @Test
    void add_savesAndReturnsStudent() {
        when(repository.save(any())).thenReturn(student);
        Student result = service.add(student);
        assertEquals("Alice", result.getName());
        verify(repository).save(student);
    }

    @Test
    void getById_returnsStudent() {
        when(repository.findById(1L)).thenReturn(Optional.of(student));
        Student result = service.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(99L));
    }

    @Test
    void delete_cascadesAndDeletesStudent() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(attendanceRepository).deleteByStudentId(1L);
        doNothing().when(marksRepository).deleteByStudentId(1L);
        service.delete(1L);
        verify(attendanceRepository).deleteByStudentId(1L);
        verify(marksRepository).deleteByStudentId(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void updateStatus_changesStatus() {
        when(repository.findById(1L)).thenReturn(Optional.of(student));
        when(repository.save(any())).thenReturn(student);
        service.updateStatus(1L, "INACTIVE");
        assertEquals(StudentStatus.INACTIVE, student.getStatus());
    }

    @Test
    void updatePhoto_setsBase64() {
        when(repository.findById(1L)).thenReturn(Optional.of(student));
        when(repository.save(any())).thenReturn(student);
        service.updatePhoto(1L, "data:image/png;base64,abc123");
        assertEquals("data:image/png;base64,abc123", student.getProfilePhoto());
    }
}
