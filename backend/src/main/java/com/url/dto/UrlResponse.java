package com.url.dto;

import java.time.LocalDateTime;

public record UrlResponse(Long id,
                          String orignalUrl,
                          String shortCode,
                          String shortUrl,
                            Long clickCount,
                          LocalDateTime createdAt,
                          LocalDateTime expiresAt
                          ) {
}
