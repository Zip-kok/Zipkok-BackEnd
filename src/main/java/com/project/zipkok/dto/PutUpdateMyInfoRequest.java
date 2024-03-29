package com.project.zipkok.dto;

import com.project.zipkok.common.enums.Gender;
import com.project.zipkok.common.enums.RealEstateType;
import com.project.zipkok.common.enums.TransactionType;
import com.project.zipkok.common.enums.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutUpdateMyInfoRequest {

    @NotBlank
    @Max(12)
    private String nickname;

    @NotBlank
    @Size(max =6)
    private String birthday;

    @ValidEnum(enumClass = Gender.class)
    private Gender gender;

    @ValidEnum(enumClass = RealEstateType.class)
    private RealEstateType realEstateType;

    @NotBlank
    @Size(max = 200)
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @ValidEnum(enumClass = TransactionType.class)
    private TransactionType transactionType;

    @NotNull
    @PositiveOrZero
    private Long mpriceMin;

    @NotNull
    @PositiveOrZero
    private Long mpriceMax;

    @NotNull
    @PositiveOrZero
    private Long mdepositMin;

    @NotNull
    @PositiveOrZero
    private Long mdepositMax;

    @NotNull
    @PositiveOrZero
    private Long ydepositMin;

    @NotNull
    @PositiveOrZero
    private Long ydepositMax;

    @NotNull
    @PositiveOrZero
    private Long purchaseMin;

    @NotNull
    @PositiveOrZero
    private Long purchaseMax;

    @AssertTrue(message = "최소가격은 최대가격을 넘을 수 없습니다.")
    private boolean isSmallerthanMax(){
        return
                this.mpriceMax >= this.mpriceMin &&
                        this.mdepositMax >= this.mdepositMin &&
                        this.ydepositMax >= this.ydepositMin &&
                        this.purchaseMax >= this.purchaseMin;
    }
}
