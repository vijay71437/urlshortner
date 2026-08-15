package com.url.service;

import com.url.dto.CreateUrlRequest;
import com.url.dto.UrlResponse;
import com.url.entity.Url;
import com.url.exception.UrlNotFoundException;
import com.url.repository.UrlRepository;
import com.url.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlClickService urlClickService;

    public UrlResponse createUrl(CreateUrlRequest request){
        String shortCode;
        do{
            shortCode=shortCodeGenerator.generate();
        }while (urlRepository.existsByShortCode(shortCode));
        Url url=Url.builder()
                .shortCode(shortCode)
                .orginalUrl(request.originalUrl())
                .clickCount(0L)
                .build();
        Url savedUrl=urlRepository.save(url);
        String shortUrl="http://localhost:8080/"+shortCode;
        return new UrlResponse(savedUrl.getId(), savedUrl.getOrginalUrl(),savedUrl.getShortCode(),shortUrl, savedUrl.getClickCount());
    }

    public String redirect(String shortCode,String ipAddress,String userAgent){
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("SHort code is not found!! " + shortCode));
        urlClickService.recordClicked(url,ipAddress,userAgent);
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
        return url.getOrginalUrl();
    }
    public List<UrlResponse> getAllUrls() {

        return urlRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UrlResponse createShortUrl(CreateUrlRequest request) {

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = Url.builder()
                .orginalUrl(request.originalUrl())
                .shortCode(shortCode)
                .clickCount(0L)
                .build();

        Url savedUrl = urlRepository.save(url);

        return toResponse(savedUrl);
    }

    public UrlResponse getUrlById(Long id) {

        Url url = urlRepository.findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "URL not found with id: " + id
                        )
                );

        return toResponse(url);
    }

    public void deleteUrl(Long id) {

        Url url = urlRepository.findById(id)
                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "URL not found with id: " + id
                        )
                );

        urlRepository.delete(url);
    }

    private UrlResponse toResponse(Url url) {

        String shortUrl =
                "http://localhost:8080/" + url.getShortCode();

        return new UrlResponse(
                url.getId(),
                url.getOrginalUrl(),
                url.getShortCode(),
                shortUrl,
                url.getClickCount()
        );
    }
}
