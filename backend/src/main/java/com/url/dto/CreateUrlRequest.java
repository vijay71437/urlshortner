package com.url.dto;

import com.url.validator.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record CreateUrlRequest (
        @NotBlank(message = "URL is required")
        @ValidUrl
        String originalUrl,
        LocalDateTime expiresAt
){
}
