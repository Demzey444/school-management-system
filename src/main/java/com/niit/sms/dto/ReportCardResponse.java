package com.niit.sms.dto;

import com.niit.sms.model.Result;

import java.util.List;

public record ReportCardResponse(
        String studentId,
        String studentName,
        String admissionNumber,
        String className,
        String session,
        String term,
        List<Result> results,
        int totalSubjects,
        int totalScore,
        double average,
        String overallGrade
) {
}