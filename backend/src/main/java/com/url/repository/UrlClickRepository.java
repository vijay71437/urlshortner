package com.url.repository;

import com.url.entity.Url;
import com.url.entity.UrlClick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UrlClickRepository extends JpaRepository<UrlClick,Long> {
    List<UrlClick> findByUrlOrderByClickedAtDesc(Url url);

    long countByUrl(Url url);

    long countByUrlAndClickedAtBetween(
            Url url,
            LocalDateTime start,
            LocalDateTime end
    );
}
