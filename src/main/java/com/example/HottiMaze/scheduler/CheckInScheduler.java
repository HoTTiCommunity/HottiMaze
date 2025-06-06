package com.example.HottiMaze.scheduler;

import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckInScheduler {

    private final UserService userService;

    /**
     * 매일 자정(00:00)마다 출석 체크 가능 상태를 초기화.
     * "0 0 0 * * *" → 매일 00시 00분 00초 실행
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyCheckIn() {
        userService.resetCheckInAvailability();
    }
}
