package com.sms.dto;

import java.util.List;

public class MarksSummary {
    private List<MarksDetail> details;
    private int totalObtained;
    private int totalMax;
    private double percentage;
    private String grade;

    public MarksSummary(List<MarksDetail> details, int totalObtained, int totalMax, double percentage, String grade) {
        this.details = details;
        this.totalObtained = totalObtained;
        this.totalMax = totalMax;
        this.percentage = percentage;
        this.grade = grade;
    }

    public List<MarksDetail> getDetails() { return details; }
    public int getTotalObtained() { return totalObtained; }
    public int getTotalMax() { return totalMax; }
    public double getPercentage() { return percentage; }
    public String getGrade() { return grade; }

    public static class MarksDetail {
        private Long id;
        private String subject;
        private String examType;
        private int marksObtained;
        private int totalMarks;
        private double percentage;
        private String grade;

        public MarksDetail(Long id, String subject, String examType, int marksObtained, int totalMarks, double percentage, String grade) {
            this.id = id;
            this.subject = subject;
            this.examType = examType;
            this.marksObtained = marksObtained;
            this.totalMarks = totalMarks;
            this.percentage = percentage;
            this.grade = grade;
        }

        public Long getId() { return id; }
        public String getSubject() { return subject; }
        public String getExamType() { return examType; }
        public int getMarksObtained() { return marksObtained; }
        public int getTotalMarks() { return totalMarks; }
        public double getPercentage() { return percentage; }
        public String getGrade() { return grade; }
    }
}
