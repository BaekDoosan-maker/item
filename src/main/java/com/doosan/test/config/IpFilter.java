package com.doosan.test.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

// IpFilter를 수정하여, 요청을 많이 보낸 IP를 추적하고, 일정 기준 이상일 경우 차단
@Component
public class IpFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(IpFilter.class);

    private Set<String> blockedIps = new HashSet<>();
    private Map<String, List<Long>> ipRequestTimes = new HashMap<>();

    private static final int MAX_REQUESTS = 10; // 차단 기준 (최대 요청 횟수)
    private static final long TIME_WINDOW = TimeUnit.MINUTES.toMillis(1); // 1분 동안의 요청 수 추적

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        // IP가 차단된 경우 바로 403을 반환
        if (blockedIps.contains(ip)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Blocked IP");
            return;
        }

        // IP의 요청 시간을 추적
        ipRequestTimes.putIfAbsent(ip, new ArrayList<>());
        ipRequestTimes.get(ip).add(currentTime);

        // 요청이 시간 범위 내에 있는지 체크하고, 너무 많으면 차단
        List<Long> requestTimes = ipRequestTimes.get(ip);
        requestTimes.removeIf(time -> time < currentTime - TIME_WINDOW); // 오래된 요청은 삭제

        if (requestTimes.size() > MAX_REQUESTS) {
            blockedIps.add(ip); // 너무 많은 요청을 보낸 IP는 차단
            logger.warn("IP {} has been blocked due to too many requests.", ip);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Too many requests, IP is blocked.");
            return;
        }

        filterChain.doFilter(request, response); // 필터 체인 계속 진행
    }

    public void blockIp(String ip) {
        blockedIps.add(ip);
    }

    public void unblockIp(String ip) {
        if (!blockedIps.contains(ip)) {
            logger.warn("이 IP : {} 주소는 차단된 아이피가 아님.", ip);
            return;
        }


        blockedIps.remove(ip); // 차단된 IP 제거
        ipRequestTimes.remove(ip); // IP에 대한 요청 기록 삭제
    }

    public Set<String> getBlockedIps() {
        return blockedIps;
    }

}
