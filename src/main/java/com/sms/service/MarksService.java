package com.sms.service;

import com.sms.dto.MarksSummary;
import com.sms.model.Marks;
import com.sms.repository.MarksRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarksService {

    private final MarksRepository repository;

    public MarksService(MarksRepository repository) {
        this.repository = repository;
    }

    public Marks add(Marks marks) {
        return repository.save(marks);
    }

    public List<Marks> getByStudent(Long studentId, String examType) {
        if (examType != null && !examType.isBlank()) {
            return repository.findByStudentIdAndExamType(studentId, examType);
        }
        return repository.findByStudentId(studentId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public MarksSummary getSummary(Long studentId) {
        List<Marks> list = repository.findByStudentId(studentId);
        int totalObtained = 0, totalMax = 0;
        List<MarksSummary.MarksDetail> details = new ArrayList<>();

        for (Marks m : list) {
            double pct = m.getTotalMarks() == 0 ? 0 : Math.round(m.getMarksObtained() * 10000.0 / m.getTotalMarks()) / 100.0;
            details.add(new MarksSummary.MarksDetail(
                m.getId(), m.getSubject(), m.getExamType(),
                m.getMarksObtained(), m.getTotalMarks(), pct, grade(pct)
            ));
            totalObtained += m.getMarksObtained();
            totalMax += m.getTotalMarks();
        }

        double overallPct = totalMax == 0 ? 0 : Math.round(totalObtained * 10000.0 / totalMax) / 100.0;
        return new MarksSummary(details, totalObtained, totalMax, overallPct, grade(overallPct));
    }

    private String grade(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }
}
