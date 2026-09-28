package com.niit.sms.dto;

import com.niit.sms.model.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record AttendanceRequest(

        @NotBlank(message = "Student id is required")
        String studentId,

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        LocalDate date,

        @NotNull(message = "Status is required")
        AttendanceStatus status,

        String remark
) {
}