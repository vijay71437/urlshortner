package com.url.service;

import com.url.dto.CreateUrlRequest;
import com.url.dto.UrlResponse;
import com.url.entity.Url;
import com.url.entity.User;
import com.url.exception.UrlNotFoundException;
import com.url.repository.UrlRepository;
import com.url.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlClickService urlClickService;
    private final UserRepository userRepository;


    public String redirect(String shortCode,String ipAddress,String userAgent){
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("SHort code is not found!! " + shortCode));
        if (url.getExpiresAt() != null &&
                url.getExpiresAt().isBefore(LocalDateTime.now())) {

            throw new UrlNotFoundException(
                    "This short URL has expired"
            );
        }
        urlClickService.recordClicked(url,ipAddress,userAgent);
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
        return url.getOrginalUrl();
    }



    public UrlResponse createShortUrl(
            CreateUrlRequest request,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = Url.builder()
                .user(user)
                .orginalUrl(request.originalUrl())
                .shortCode(shortCode)
                .clickCount(0L)
                .expiresAt(request.expiresAt())
                .build();

        Url savedUrl = urlRepository.save(url);

        return toResponse(savedUrl);
    }

    public UrlResponse getUrlById(
            Long id,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        Url url = urlRepository
                .findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "URL not found with id: " + id
                        )
                );

        if (!url.getUser().getId().equals(user.getId())) {
            throw new UrlNotFoundException(
                    "URL not found with id: " + id
            );
        }

        return toResponse(url);
    }

    public void deleteUrl(
            Long id,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        Url url = urlRepository
                .findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "URL not found with id: " + id
                        )
                );

        if (!url.getUser().getId().equals(user.getId())) {
            throw new UrlNotFoundException(
                    "URL not found with id: " + id
            );
        }

        urlRepository.delete(url);
    }

    public Page<UrlResponse> getAllUrls(
            String email,
            String search,
            Pageable pageable
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        Page<Url> urls;

        if (search == null || search.isBlank()) {

            urls = urlRepository
                    .findByUserOrderByCreatedAtDesc(
                            user,
                            pageable
                    );

        } else {

            urls = urlRepository.searchUrls(
                    user,
                    search,
                    pageable
            );
        }

        return urls.map(this::toResponse);
    }

    private UrlResponse toResponse(Url url) {

        String shortUrl =
                "http://localhost:8080/" + url.getShortCode();

        return new UrlResponse(
                url.getId(),
                url.getOrginalUrl(),
                url.getShortCode(),
                shortUrl,
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiresAt()
        );
    }
}
