package com.example.employee_management.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled tasks cho hệ thống.
 */
@Slf4j
@Component
public class SystemHealthScheduler {

    /**
     * Log "System running" ra console mỗi 30 giây.
     * fixedRate = 30_000ms, initialDelay = 5_000ms (chờ app khởi động xong).
     */
    @Scheduled(fixedRate = 30_000, initialDelay = 5_000)
    public void logSystemStatus() {
        log.info(">>> [Scheduler] System running");
    }
}
