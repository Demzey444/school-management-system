package com.niit.sms.dto;

import com.niit.sms.model.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BulkAttendanceEntry(
        @NotBlank(message = "Student id is required")
        String studentId,

        @NotNull(message = "Status is required")
        AttendanceStatus status,

        String remark
) {
}