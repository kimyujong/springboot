package com.likeyou.safety.domain.dat.sos.service;

import com.likeyou.safety.domain.dat.sos.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SosStatusService {

    // 임시 메모리 저장소 (나중에 DB로 교체)
    private final Map<String, SosEventResponse> eventStore = new ConcurrentHashMap<>();

    public SosStatusService() {
        initDummyData();
    }

    private void initDummyData() {
        List<SosEventResponse> dummyEvents = Arrays.asList(
                SosEventResponse.builder()
                        .id("e1")
                        .time("14:32:05")
                        .type("emergency")
                        .message("광안리 해변 이상 행동 감지")
                        .location("광안리 해수욕장")
                        .isCctv(true)
                        .status("new")
                        .responder("-")
                        .build(),
                SosEventResponse.builder()
                        .id("e2")
                        .time("14:30:12")
                        .type("warning")
                        .message("민락수변공원 높은 혼잡도")
                        .location("민락수변공원")
                        .isCctv(true)
                        .status("new")
                        .responder("-")
                        .build(),
                SosEventResponse.builder()
                        .id("e3")
                        .time("14:25:45")
                        .type("emergency")
                        .message("남천동 낙상 감지")
                        .location("남천동 24-1")
                        .isCctv(true)
                        .status("dispatched")
                        .responder("김요원")
                        .build(),
                SosEventResponse.builder()
                        .id("e4")
                        .time("14:20:30")
                        .type("warning")
                        .message("광안대교 입구 혼잡")
                        .location("광안대교 입구")
                        .isCctv(true)
                        .status("new")
                        .responder("-")
                        .build(),
                SosEventResponse.builder()
                        .id("e5")
                        .time("14:10:15")
                        .type("emergency")
                        .message("수영역 SOS 호출")
                        .location("수영역 2번 출구")
                        .isCctv(false)
                        .status("resolved")
                        .responder("박요원")
                        .build(),
                SosEventResponse.builder()
                        .id("e6")
                        .time("13:55:00")
                        .type("warning")
                        .message("광남로 교통 혼잡")
                        .location("광남로 130번길")
                        .isCctv(true)
                        .status("resolved")
                        .responder("이요원")
                        .build()
        );

        for (SosEventResponse event : dummyEvents) {
            eventStore.put(event.getId(), event);
        }
    }

    /**
     * 기존 요약 API (호환성 유지)
     */
    public SosListResponse getSosStatus() {
        long pendingCount = eventStore.values().stream()
                .filter(e -> "new".equals(e.getStatus()))
                .count();
        long resolvedCount = eventStore.values().stream()
                .filter(e -> "resolved".equals(e.getStatus()) || "dispatched".equals(e.getStatus()))
                .count();

        SosSummaryResponse summary = SosSummaryResponse.builder()
                .totalCount((int) eventStore.size())
                .pendingCount((int) pendingCount)
                .resolvedCount((int) resolvedCount)
                .build();

        List<SosItemResponse> list = eventStore.values().stream()
                .limit(5)
                .map(e -> SosItemResponse.builder()
                        .sosId(e.getId())
                        .location(e.getLocation())
                        .time(e.getTime())
                        .status(e.getStatus().toUpperCase())
                        .build())
                .toList();

        return SosListResponse.builder()
                .summary(summary)
                .recentList(list)
                .build();
    }

    /**
     * 이벤트 목록 조회
     */
    public List<SosEventResponse> getEvents(int limit) {
        return eventStore.values().stream()
                .sorted((a, b) -> b.getTime().compareTo(a.getTime()))
                .limit(limit)
                .toList();
    }

    /**
     * 시간대별 통계
     */
    public List<HourlyStatResponse> getHourlyStats() {
        // 현재 시각 기준 최근 6시간 통계 (더미)
        int currentHour = LocalTime.now().getHour();
        List<HourlyStatResponse> stats = new ArrayList<>();
        Random random = new Random(42); // 일관된 더미 데이터

        for (int i = 5; i >= 0; i--) {
            int hour = (currentHour - i + 24) % 24;
            stats.add(HourlyStatResponse.builder()
                    .time(String.format("%02d:00", hour))
                    .count(random.nextInt(15) + 1)
                    .build());
        }
        return stats;
    }

    /**
     * 구역별 통계
     */
    public List<ZoneStatResponse> getZoneStats() {
        return Arrays.asList(
                ZoneStatResponse.builder().region("광안리").count(12).build(),
                ZoneStatResponse.builder().region("민락동").count(8).build(),
                ZoneStatResponse.builder().region("남천동").count(5).build(),
                ZoneStatResponse.builder().region("수영역").count(15).build(),
                ZoneStatResponse.builder().region("기타").count(3).build()
        );
    }

    /**
     * 이벤트 상태 업데이트
     */
    public boolean updateEventStatus(String eventId, SosUpdateRequest request) {
        SosEventResponse existing = eventStore.get(eventId);
        if (existing == null) {
            log.warn("[SOS] 이벤트를 찾을 수 없음: {}", eventId);
            return false;
        }

        SosEventResponse updated = SosEventResponse.builder()
                .id(existing.getId())
                .time(existing.getTime())
                .type(existing.getType())
                .message(existing.getMessage())
                .location(existing.getLocation())
                .isCctv(existing.isCctv())
                .status(request.getStatus())
                .responder(request.getResponder())
                .build();

        eventStore.put(eventId, updated);
        log.info("[SOS] 이벤트 상태 업데이트: {} -> {} (담당: {})", 
                eventId, request.getStatus(), request.getResponder());
        return true;
    }
    
}

