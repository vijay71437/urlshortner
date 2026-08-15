package com.url.controller;


import com.url.dto.ApiResponse;
import com.url.dto.CreateUrlRequest;
import com.url.dto.PaginationResponse;
import com.url.dto.UrlResponse;
import com.url.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<ApiResponse<UrlResponse>> createShortUrl(
            @Valid @RequestBody CreateUrlRequest request,
            Authentication authentication
    ) {

        if (request.expiresAt() != null &&
                request.expiresAt().isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Expiration date must be in the future"
            );
        }

        UrlResponse response =
                urlService.createShortUrl(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Short URL created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<UrlResponse>>> getAllUrls(

            Authentication authentication,

            @RequestParam(required = false)
            String search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<UrlResponse> result =
                urlService.getAllUrls(
                        authentication.getName(),
                        search,
                        pageable
                );

        PaginationResponse<UrlResponse> pagination =
                new PaginationResponse<>(
                        result.getContent(),
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalElements(),
                        result.getTotalPages(),
                        result.hasNext(),
                        result.hasPrevious()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "URLs fetched successfully",
                        pagination
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UrlResponse>> getUrlById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        UrlResponse response =
                urlService.getUrlById(
                        id,
                        authentication.getName()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "URL fetched successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUrl(
            @PathVariable Long id,
            Authentication authentication
    ) {

        urlService.deleteUrl(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "URL deleted successfully",
                        null
                )
        );
    }
}