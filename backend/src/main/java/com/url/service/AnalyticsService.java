package com.url.service;


import com.url.dto.UrlAnalyticsResponse;
import com.url.entity.Url;
import com.url.exception.UrlNotFoundException;
import com.url.repository.UrlClickRepository;
import com.url.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UrlRepository urlRepository;
    private final UrlClickRepository urlClickRepository;

    public UrlAnalyticsResponse getAnalytics(Long urlId) {

        Url url = urlRepository.findById(urlId)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "URL not found with id: " + urlId
                        )
                );

        LocalDate today = LocalDate.now();

        LocalDateTime todayStart =
                today.atStartOfDay();

        LocalDateTime tomorrowStart =
                today.plusDays(1).atStartOfDay();

        LocalDateTime weekStart =
                today.with(DayOfWeek.MONDAY)
                        .atStartOfDay();

        LocalDateTime monthStart =
                today.withDayOfMonth(1)
                        .atStartOfDay();

        Long totalClicks =
                urlClickRepository.countByUrl(url);

        Long todayClicks =
                urlClickRepository.countByUrlAndClickedAtBetween(
                        url,
                        todayStart,
                        tomorrowStart
                );

        Long weekClicks =
                urlClickRepository.countByUrlAndClickedAtBetween(
                        url,
                        weekStart,
                        tomorrowStart
                );

        Long monthClicks =
                urlClickRepository.countByUrlAndClickedAtBetween(
                        url,
                        monthStart,
                        tomorrowStart
                );

        return new UrlAnalyticsResponse(
                url.getId(),
                url.getShortCode(),
                url.getOrginalUrl(),
                totalClicks,
                todayClicks,
                weekClicks,
                monthClicks
        );
    }
}
