package com.niit.sms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClassRequest(

        @NotBlank(message = "Class name is required")
        @Size(max = 50, message = "Class name must be at most 50 characters")
        String name,

        @Size(max = 20, message = "Level must be at most 20 characters")
        String level,

        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        String classTeacherId
) {
}