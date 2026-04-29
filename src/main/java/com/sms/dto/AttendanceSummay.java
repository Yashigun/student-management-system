package com.sms.dto;

public class AttendanceSummary {
    private long total;
    private long present;
    private long absent;
    private double percentage;

    public AttendanceSummary(long total, long present, long absent, double percentage) {
        this.total = total;
        this.present = present;
        this.absent = absent;
        this.percentage = percentage;
    }

    public long getTotal() { return total; }
    public long getPresent() { return present; }
    public long getAbsent() { return absent; }
    public double getPercentage() { return percentage; }
}
