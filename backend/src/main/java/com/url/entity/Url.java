package com.url.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "urls")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;
    @Column(nullable = false,columnDefinition ="TEXT")
    private String orginalUrl;
    @Column(nullable = false,unique = true,length = 10)
    private String shortCode;
    @Column(nullable = false)
    private Long clickCount=0L;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @OneToMany(
            mappedBy = "url",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<UrlClick> clicks = new ArrayList<>();
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        if(clickCount==null){
            clickCount=0L;
        }
    }


}
