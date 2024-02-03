package com.project.zipkok.controller;

import com.project.zipkok.common.argument_resolver.PreAuthorize;
import com.project.zipkok.common.exception.RealEstateException;
import com.project.zipkok.common.response.BaseResponse;
import com.project.zipkok.dto.GetRealEstateResponse;
import com.project.zipkok.dto.GetRealEstatesResponse;
import com.project.zipkok.service.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/realEstate")
@RequiredArgsConstructor
@Tag(name = "RealEstate API", description = "매물 관련 API")
public class RealEstateController {

    private final RealEstateService realEstateService;

    @Operation(summary = "매물 상세정보 API", description = "매물의 상세정보를 응답하는 API입니다.")
    @GetMapping("/{realEstateId}")
    public BaseResponse<GetRealEstateResponse> getRealEstate(@Parameter(hidden = true) @PreAuthorize long userId, @Parameter(name = "realEstateId", description = "매물의 Id", in = ParameterIn.PATH)
                                                                 @PathVariable(value = "realEstateId") Long realEstateId) {
        log.info("[RealEstateController.getRealEstate]");

        if (realEstateId == null) {
            throw new RealEstateException(INVALID_PROPERTY_ID);
        }

        return new BaseResponse<>(PROPERTY_DETAIL_QUERY_SUCCESS, realEstateService.getRealEstateInfo(userId, realEstateId));
    }

    @Operation(summary = "지도에 표시할 매물을 불러오는 API", description = "인자로 넘어온 지도 영역 안쪽의 모든 매물을 반환")
    @GetMapping("")
    public BaseResponse<GetRealEstatesResponse> getRealEstates(@Parameter(hidden = true) @PreAuthorize long userId,
                                                               @Parameter(name = "southWestLat", description = "지도의 좌측하단 모서리 위도") @RequestParam(value = "southWestLat") double southWestLat,
                                                               @Parameter(name = "southWestLon", description = "지도의 좌측하단 모서리 경도") @RequestParam(value = "southWestLon") double southWestLon,
                                                               @Parameter(name = "northEastLat", description = "지도의 우상단 모서리 위도") @RequestParam(value = "northEastLat") double northEastLat,
                                                               @Parameter(name = "northEastLon", description = "지도의 우상단 모서리 경도") @RequestParam(value = "northEastLon") double northEastLon) {
        log.info("[RealEstateController.getRealEstates]");

        return new BaseResponse<>(PROPERTY_MAP_QUERY_SUCCESS, realEstateService.getRealEstates(userId, southWestLat, southWestLon, northEastLat, northEastLon));
    }
}
