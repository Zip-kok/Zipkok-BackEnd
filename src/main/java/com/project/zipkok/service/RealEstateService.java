package com.project.zipkok.service;

import com.project.zipkok.common.enums.RealEstateType;
import com.project.zipkok.common.enums.TransactionType;
import com.project.zipkok.common.exception.DatabaseException;
import com.project.zipkok.common.exception.InternalServerErrorException;
import com.project.zipkok.common.exception.RealEstateException;
import com.project.zipkok.dto.GetRealEstateResponse;
import com.project.zipkok.dto.GetRealEstatesResponse;
import com.project.zipkok.model.RealEstate;
import com.project.zipkok.model.RealEstateImage;
import com.project.zipkok.model.User;
import com.project.zipkok.repository.KokRepository;
import com.project.zipkok.repository.RealEstateRepository;
import com.project.zipkok.repository.UserRepository;
import com.project.zipkok.repository.ZimRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.INVALID_PROPERTY_ID;
import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.PROPERTY_MAP_QUERY_FAILIURE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final UserRepository userRepository;
    private final ZimRepository zimRepository;
    private final KokRepository kokRepository;


    @Transactional
    public GetRealEstateResponse getRealEstateInfo(Long userId, Long realEstateId) {

        log.info("[RealEstateService.getRealEstateInfo]");

        try {
            RealEstate realEstate = realEstateRepository.findById(realEstateId).get();
            User user = userRepository.findByUserId(userId);

            List<String> realEstateImages = realEstate.getRealEstateImages()
                    .stream()
                    .map(RealEstateImage::getImageUrl)
                    .collect(Collectors.toList());

            boolean isZimmed = false;
            boolean isKokked = false;

            if (zimRepository.findFirstByUserAndRealEstate(user, realEstate) != null) {
                isZimmed = true;
            }

            if (kokRepository.findFirstByUserAndRealEstate(user, realEstate) != null) {
                isKokked = true;
            }


            GetRealEstateResponse response = GetRealEstateResponse.builder()
                    .realEstateId(realEstate.getRealEstateId())
                    .imageInfo(new GetRealEstateResponse.ImageInfo(realEstateImages.size(), realEstateImages))
                    .address(realEstate.getAddress())
                    .detailAddress(realEstate.getDetailAddress())
                    .transactionType(realEstate.getTransactionType().getDescription())
                    .deposit(realEstate.getDeposit())
                    .price(realEstate.getPrice())
                    .detail(realEstate.getDetail())
                    .areaSize(realEstate.getAreaSize())
                    .pyeongsu(realEstate.getPyeongsu())
                    .realEstateType(realEstate.getRealEstateType().getDescription())
                    .floorNum(realEstate.getFloorNum())
                    .administrativeFee(realEstate.getAdministrativeFee())
                    .latitude(realEstate.getLatitude())
                    .longitude(realEstate.getLongitude())
                    .isZimmed(isZimmed)
                    .isKokked(isKokked)
                    .build();

            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RealEstateException(INVALID_PROPERTY_ID);
        }
    }

    public GetRealEstatesResponse getRealEstates(Long userId, double southWestLat, double southWestLon, double northEastLat, double northEastLon ) {

        log.info("[RealEstateService.getRealEstates]");

        try {

            User user = userRepository.findByUserId(userId);

            TransactionType transactionTypeFilter = user.getTransactionType();

            RealEstateType realEstateTypeFilter = user.getReslEstateType();

            Long depositMin = 0L;
            Long depositMax = 0L;
            Long priceMin = 0L;
            Long priceMax = 0L;

            if (transactionTypeFilter == TransactionType.MONTHLY) {
                depositMin = user.getTransactionPriceConfig().getMDepositMin();
                depositMax = user.getTransactionPriceConfig().getMDepositMax();
                priceMin = user.getTransactionPriceConfig().getMPriceMin();
                priceMax = user.getTransactionPriceConfig().getMPriceMax();
            } else if (transactionTypeFilter == TransactionType.YEARLY) {
                depositMin = user.getTransactionPriceConfig().getYDepositMin();
                depositMax = user.getTransactionPriceConfig().getYDepositMax();
            } else {
                priceMin = user.getTransactionPriceConfig().getPurchaseMin();
                priceMax = user.getTransactionPriceConfig().getPurchaseMax();
            }

            List<RealEstate> realEstatesQueryResult = realEstateRepository.findByCoordinates(southWestLat, southWestLon, northEastLat, northEastLon)
                    .stream()
                    .filter(realEstate -> realEstate.getTransactionType().equals(transactionTypeFilter))
                    .filter(realEstate -> realEstate.getRealEstateType().equals(realEstateTypeFilter))
                    .filter(realEstate -> (realEstate.getUser() == null || realEstate.getUser().equals(user)))
                    .toList();

            GetRealEstatesResponse response = GetRealEstatesResponse.builder()
                    .filterInfo(GetRealEstatesResponse.FilterInfo.builder()
                            .transactionType(transactionTypeFilter.getDescription())
                            .realEstateType(realEstateTypeFilter.getDescription())
                            .depositMin(depositMin)
                            .depositMax(depositMax)
                            .priceMin(priceMin)
                            .priceMax(priceMax)
                            .build())
                    .realEstateInfoList(realEstatesQueryResult.stream().map(realEstate -> GetRealEstatesResponse.RealEstateInfo.builder()
                                    .realEstateId(realEstate.getRealEstateId())
                                    .imageURL(realEstate.getImageUrl())
                                    .transactionType(realEstate.getTransactionType().getDescription())
                                    .realEstateType(realEstate.getRealEstateType().getDescription())
                                    .deposit(realEstate.getDeposit())
                                    .price(realEstate.getPrice())
                                    .address(realEstate.getAddress())
                                    .detailAddress(realEstate.getDetailAddress())
                                    .latitude(realEstate.getLatitude())
                                    .longitude(realEstate.getLongitude())
                                    .agent(realEstate.getAgent() != null ? realEstate.getAgent() : "직접 등록한 매물입니다.")
                                    .isZimmed(zimRepository.findFirstByUserAndRealEstate(user, realEstate) != null)
                                    .isKokked(kokRepository.findFirstByUserAndRealEstate(user, realEstate) != null)
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            return response;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new RealEstateException(PROPERTY_MAP_QUERY_FAILIURE);
        }

    }

}
