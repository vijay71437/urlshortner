package com.url.controller;

import com.url.dto.CreateUrlRequest;
import com.url.dto.UrlResponse;
import com.url.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse createShortUrl(
            @Valid @RequestBody CreateUrlRequest request
    ) {
        return urlService.createShortUrl(request);
    }

    @GetMapping
    public List<UrlResponse> getAllUrls() {

        return urlService.getAllUrls();
    }

    @GetMapping("/{id}")
    public UrlResponse getUrlById(
            @PathVariable Long id
    ) {

        return urlService.getUrlById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUrl(
            @PathVariable Long id
    ) {

        urlService.deleteUrl(id);
    }
}
