package com.niit.sms.dto;

public record AttendanceSummaryResponse(
        String studentId,
        String studentName,
        long present,
        long absent,
        long total,
        double percentage
) {
}