package com.doosan.test.controller;

import com.doosan.test.config.IpFilter;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("/api/ip")
public class IpFilterController {

    private final IpFilter ipFilter;

    public IpFilterController(IpFilter ipFilter) {
        this.ipFilter = ipFilter;
    }

    // IP 차단
    @PostMapping("/block")
    public String blockIp(@RequestParam String ip) {
        ipFilter.blockIp(ip);
        return "IP " + ip + " blocked";
    }

    // IP 차단 해제
    @PostMapping("/unblock")
    public String unblockIp(@RequestParam String ip) {
        ipFilter.unblockIp(ip);
        return "IP " + ip + " unblocked";
    }

    // 현재 차단된 IP 목록 확인
    @GetMapping("/blocked")
    public Set<String> getBlockedIps() {
        return ipFilter.getBlockedIps();
    }
}

