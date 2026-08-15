package com.url.dto;

public record UrlResponse(Long id,
                          String orignalUrl,
                          String shortCode,
                          String shortUrl,
                            Long clickCount
                          ) {
}
