package com.project.zipkok.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetRealEstatesResponse {

    private FilterInfo filterInfo;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FilterInfo {
        private String transactionType;
        private String realEstateType;

        private Long depositMin;
        private Long depositMax;

        private Long priceMin;
        private Long priceMax;

    }

    private List<RealEstateInfo> realEstateInfoList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RealEstateInfo {
        private Long realEstateId;
        private String imageURL;
        private String transactionType;
        private String realEstateType;
        private Long deposit;
        private Long price;
        private String address;
        private String detailAddress;
        private Double latitude;
        private Double longitude;
        private String agent;

        @Getter(AccessLevel.NONE)
        @JsonProperty("isZimmed")
        private boolean isZimmed;

        @Getter(AccessLevel.NONE)
        @JsonProperty("isKokked")
        private boolean isKokked;
    }
}
