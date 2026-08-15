package com.url.service;

import com.url.entity.Url;
import com.url.entity.UrlClick;
import com.url.repository.UrlClickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlClickService {
    private final UrlClickRepository urlClickRepository;

    public void recordClicked(Url url,String ipAddress,String userAgent){
        UrlClick build = UrlClick.builder().url(url).ipAddress(ipAddress).userAgent(userAgent).build();
        urlClickRepository.save(build);
    }
}
