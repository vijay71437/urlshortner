package com.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUrlRequest (
        @NotBlank(message = "URL is required")
        @Pattern(regexp = "^(https?://).+",
        message = "URL must be start with https:// or http://")
        String originalUrl
){
}
