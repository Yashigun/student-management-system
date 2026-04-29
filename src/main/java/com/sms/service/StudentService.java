package com.sms.service;

import com.sms.dto.StudentFilterRequest;
import com.sms.model.Student;
import com.sms.model.StudentStatus;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.MarksRepository;
import com.sms.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    public StudentService(StudentRepository repository, AttendanceRepository attendanceRepository, MarksRepository marksRepository) {
        this.repository = repository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
    }

    public Student add(Student student) {
        return repository.save(student);
    }

    public Page<Student> getAll(StudentFilterRequest req) {
        Specification<Student> spec = Specification.where(null);

        if (req.getSearch() != null && !req.getSearch().isBlank()) {
            String q = "%" + req.getSearch().toLowerCase() + "%";
            try {
                Long searchId = Long.parseLong(req.getSearch().trim());
                spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), q),
                    cb.equal(root.get("id"), searchId)
                ));
            } catch (NumberFormatException e) {
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), q));
            }
        }

        if (req.getClassName() != null && !req.getClassName().isBlank()) {
            String cn = req.getClassName();
            spec = spec.and((root, query, cb) -> cb.equal(root.get("className"), cn));
        }

        if (req.getSection() != null && !req.getSection().isBlank()) {
            String sec = req.getSection();
            spec = spec.and((root, query, cb) -> cb.equal(root.get("section"), sec));
        }

        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            try {
                StudentStatus s = StudentStatus.valueOf(req.getStatus());
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), s));
            } catch (IllegalArgumentException ignored) {}
        }

        return repository.findAll(spec, PageRequest.of(req.getPage(), req.getSize(), Sort.by("name")));
    }

    public Student getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    public Student update(Long id, Student updated) {
        Student s = getById(id);
        s.setName(updated.getName());
        s.setDateOfBirth(updated.getDateOfBirth());
        s.setGender(updated.getGender());
        s.setEmail(updated.getEmail());
        s.setPhone(updated.getPhone());
        s.setAddress(updated.getAddress());
        s.setClassName(updated.getClassName());
        s.setSection(updated.getSection());
        s.setParentName(updated.getParentName());
        s.setParentPhone(updated.getParentPhone());
        s.setParentEmail(updated.getParentEmail());
        return repository.save(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Student not found: " + id);
        attendanceRepository.deleteByStudentId(id);
        marksRepository.deleteByStudentId(id);
        repository.deleteById(id);
    }

    public Student updateStatus(Long id, String status) {
        Student s = getById(id);
        s.setStatus(StudentStatus.valueOf(status));
        return repository.save(s);
    }

    public Student updatePhoto(Long id, String base64DataUrl) {
        Student s = getById(id);
        s.setProfilePhoto(base64DataUrl);
        return repository.save(s);
    }
}
