package com.url.repository;

import com.url.entity.Url;
import com.url.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url,Long> {
    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    Page<Url> findByUserOrderByCreatedAtDesc(
            User user,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM Url u
        WHERE u.user = :user
        AND LOWER(u.orginalUrl) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY u.createdAt DESC
        """)
    Page<Url> searchUrls(
            @Param("user") User user,
            @Param("search") String search,
            Pageable pageable
    );
}
