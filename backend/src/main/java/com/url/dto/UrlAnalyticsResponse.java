package com.url.dto;



public record UrlAnalyticsResponse(
        Long urlId,
        String shortCode,
        String originalUrl,
        Long totalClicks,
        Long todayClicks,
        Long weekClicks,
        Long monthClicks
) {
}
