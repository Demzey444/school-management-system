package com.niit.sms.dto;

import com.niit.sms.model.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TeacherRequest(

        @NotBlank(message = "Staff number is required")
        @Size(max = 30, message = "Staff number must be at most 30 characters")
        String staffNumber,

        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must be at most 100 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Size(max = 20, message = "Phone must be at most 20 characters")
        String phone,

        @NotNull(message = "Gender is required")
        Gender gender,

        @Size(max = 100, message = "Qualification must be at most 100 characters")
        String qualification,

        List<String> subjectIds,

        List<String> classIds
) {
}