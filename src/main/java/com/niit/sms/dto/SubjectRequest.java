package com.niit.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectRequest(

        @NotBlank(message = "Subject code is required")
        @Size(max = 20, message = "Subject code must be at most 20 characters")
        String code,

        @NotBlank(message = "Subject name is required")
        @Size(max = 100, message = "Subject name must be at most 100 characters")
        String name,

        String classId,

        String teacherId,

        @Size(max = 300, message = "Description must be at most 300 characters")
        String description
) {
}