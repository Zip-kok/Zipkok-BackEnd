package com.project.zipkok.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PinInfo {

    @NotNull @Positive
    private Long id;

    @NotNull @Size(max = 12)
    private String name;

    @Valid
    private PinAddressInfo address;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PinAddressInfo {

        @NotNull @Size(max = 200)
        @JsonProperty("address_name")
        private String addressName;

        @NotNull
        private Double x;

        @NotNull
        private Double y;
    }
}
