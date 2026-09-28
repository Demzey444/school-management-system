package com.niit.sms.dto;

import com.niit.sms.model.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentRequest(

        @NotBlank(message = "Admission number is required")
        @Size(max = 30, message = "Admission number must be at most 30 characters")
        String admissionNumber,

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

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        String classId,

        @Size(max = 200, message = "Address must be at most 200 characters")
        String address
) {
}