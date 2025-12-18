package com.likeyou.safety.domain.com.location.controller;

import com.likeyou.safety.domain.com.location.dto.LocationResponse;
import com.likeyou.safety.domain.com.location.service.LocationService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/com/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /**
     * 전체 Location 조회
     * GET /admin/com/locations/all
     */
    @GetMapping("/all")
    public ApiResponse<List<LocationResponse>> getAllLocations() {
        List<LocationResponse> result = locationService.getAllLocations();
        return ApiResponse.success(result);
    }

    /**
     * 단일 Location 조회
     * GET /admin/com/locations/{unique_road_id}?hour=10
     */
    @GetMapping("/{uniqueRoadId}")
    public ApiResponse<LocationResponse> getLocationDetail(
            @PathVariable("uniqueRoadId") Long uniqueRoadId,
            @RequestParam("hour") Integer hour
    ) {
        LocationResponse result = locationService.getLocationDetail(uniqueRoadId, hour);
        return ApiResponse.success(result);
    }
}
