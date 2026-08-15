package com.url.controller;

import com.url.dto.ApiResponse;
import com.url.dto.UrlAnalyticsResponse;
import com.url.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/urls")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/{id}/analytics")
    public ResponseEntity<ApiResponse<UrlAnalyticsResponse>> getAnalytics(
            @PathVariable Long id,
            Authentication authentication
    ) {

        UrlAnalyticsResponse response =
                analyticsService.getAnalytics(
                        id,
                        authentication.getName()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Analytics fetched successfully",
                        response
                )
        );
    }
}
