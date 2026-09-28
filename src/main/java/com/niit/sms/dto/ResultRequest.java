package com.niit.sms.dto;

import com.niit.sms.model.enums.Term;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResultRequest(

        @NotBlank(message = "Student id is required")
        String studentId,

        @NotBlank(message = "Subject id is required")
        String subjectId,

        @NotBlank(message = "Session is required")
        String session,

        @NotNull(message = "Term is required")
        Term term,

        @NotNull(message = "Test score is required")
        @Min(value = 0, message = "Test score must be at least 0")
        @Max(value = 40, message = "Test score must be at most 40")
        Integer testScore,

        @NotNull(message = "Exam score is required")
        @Min(value = 0, message = "Exam score must be at least 0")
        @Max(value = 60, message = "Exam score must be at most 60")
        Integer examScore
) {
}