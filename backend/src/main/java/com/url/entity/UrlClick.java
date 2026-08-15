package com.url.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "url_clicks",
        indexes = {
                @Index(
                        name = "idx_url_clicks_url_id",
                        columnList = "url_id"
                ),
                @Index(
                        name = "idx_url_clicks_clicked_at",
                        columnList = "clicked_at"
                )
        }
)
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
