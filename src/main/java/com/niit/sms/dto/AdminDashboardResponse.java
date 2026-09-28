package com.niit.sms.dto;

public record AdminDashboardResponse(
        long totalStudents,
        long totalTeachers,
        long totalClasses,
        long totalSubjects,
        long totalResults,
        long totalAttendance
) {
}