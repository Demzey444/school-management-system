package com.niit.sms.dto;

import com.niit.sms.model.Result;

import java.util.List;

public record StudentDashboardResponse(
        String studentName,
        String admissionNumber,
        String className,
        long myResults,
        long myAttendanceRecords,
        double attendancePercentage,
        List<Result> recentResults
) {
}