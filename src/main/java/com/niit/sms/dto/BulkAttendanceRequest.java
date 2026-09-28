package com.niit.sms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record BulkAttendanceRequest(

        @NotBlank(message = "Class id is required")
        String classId,

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        LocalDate date,

        @NotEmpty(message = "At least one entry is required")
        @Valid
        List<BulkAttendanceEntry> entries
) {
}