package com.doosan.test.controller;

import com.doosan.test.config.IpFilter;
import com.doosan.test.entity.BlackList;
import com.doosan.test.repository.BlackListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/ip")
public class IpFilterController {

    private static final Logger logger = LoggerFactory.getLogger(IpFilterController.class);

    private final IpFilter ipFilter;
    private final BlackListRepository blackListRepository;

    public IpFilterController(IpFilter ipFilter, BlackListRepository blackListRepository) {
        this.ipFilter = ipFilter;
        this.blackListRepository = blackListRepository;
    }

    // IP 차단
    @PostMapping("/block")
    @Transactional  // 트랜잭션을 명시적으로 설정
    public ResponseEntity<String> blockIp(@RequestParam String ip) {
        // IP 차단 처리
        if (blackListRepository.existsByIpAddressAndIsBlockedTrue(ip)) {
            return ResponseEntity.badRequest().body("IP " + ip + "주소는 이미 차단된 상태");
        }

        // 새로운 BlackList 엔티티 생성
        BlackList blackList = new BlackList();
        blackList.setIpAddress(ip);
        blackList.setBlockedAt(LocalDateTime.now());
        blackList.setIsBlocked(true);
        blackList.setBlockCount(1);  // 차단 횟수 1로 설정
        blackList.setUnblockCount(0); // 차단 해제 횟수 0

        logger.info("블랙 리스트 아이피 저장 요청 시도 : {}", ip);
        blackListRepository.save(blackList);  // DB에 저장
        logger.info("아이피가 블락 처리 되고 , 저장되었음 : {}", ip);

        blackListRepository.flush();  // 강제로 DB에 반영
        logger.info(" flush : {}", ip);

        // IpFilter에 차단된 IP 추가
        ipFilter.blockIp(ip);
        return ResponseEntity.ok("IP " + ip + " 주소 차단 ");
    }

    // IP 차단 해제
    @PostMapping("/unblock")
    @Transactional  // 트랜잭션을 명시적으로 설정
    public ResponseEntity<String> unblockIp(@RequestParam String ip) {
        // DB에서 차단된 IP 조회
        BlackList blackList = blackListRepository.findByIpAddress(ip)
                .orElse(null);

        if (blackList == null || !blackList.getIsBlocked()) {
            return ResponseEntity.badRequest().body("IP " + ip + " 는 차단된 아이피 주소 아님.");
        }

        // 차단 해제 처리
        blackList.setIsBlocked(false);
        blackList.setUnblockedAt(LocalDateTime.now());
        blackList.setUnblockCount(blackList.getUnblockCount() + 1); // 차단 해제 횟수 증가
        blackListRepository.save(blackList);  // DB에 차단 해제 내용 저장
        blackListRepository.flush();  // 강제로 DB에 반영

        // IpFilter에서 차단된 IP 제거
        ipFilter.unblockIp(ip);
        return ResponseEntity.ok("IP " + ip + "주소 가 차단 해제 됨");
    }

    // 현재 차단된 IP 목록 확인
    @GetMapping("/blocked")
    public Set<String> getBlockedIps() {
        // 차단된 IP 목록 반환
        return ipFilter.getBlockedIps();
    }
}
