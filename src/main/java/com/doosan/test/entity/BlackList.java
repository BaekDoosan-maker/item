package com.doosan.test.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "BlackList")
public class BlackList {

    @Id
    @Column(name = "ip_address")
    private String ipAddress; // IP 주소

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt; // 차단된 시간

    @Column(name = "unblocked_at")
    private LocalDateTime unblockedAt; // 차단 해제 시간

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked; // 차단 상태 (Boolean 객체로 변경)

    @Column(name = "block_count", nullable = false)
    private int blockCount; // 차단 횟수

    @Column(name = "unblock_count", nullable = false)
    private int unblockCount; // 차단 해제 횟수

    @Column(name = "last_request_time")
    private LocalDateTime lastRequestTime; // 마지막 요청 시간

    @Column(name = "reason")
    private String reason; // 차단 이유

    @Column(name = "block_duration")
    private Integer blockDuration; // 차단 기간 (단위: 시간, Integer로 변경)

    @Column(name = "comments")
    private String comments; // 추가 설명

}
