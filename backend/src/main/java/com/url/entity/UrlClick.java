package com.url.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "url_clicks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlClick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "url_id")
    private Url url;

    private String ipAddress;
    @Column(length = 500)
    private String userAgent;
    private LocalDateTime clickedAt;
    @PrePersist
    protected void onCreate(){
        clickedAt=LocalDateTime.now();
    }
}
