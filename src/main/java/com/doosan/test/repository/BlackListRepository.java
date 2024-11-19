package com.doosan.test.repository;

import com.doosan.test.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, String> {

    // 차단된 IP 조회
    Optional<BlackList> findByIpAddress(String ipAddress);

    // 차단 상태 확인
    boolean existsByIpAddressAndIsBlockedTrue(String ipAddress);
}