package com.software.modsen.passengermicroservice.mappers;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRating;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPatchDto;
import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingPutDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerRatingMapper {
    PassengerRatingMapper INSTANCE = Mappers.getMapper(PassengerRatingMapper.class);

    PassengerRating fromPassengerRatingDtoToPassengerRating(PassengerRatingDto passengerRatingDto);

    PassengerRating fromPassengerRatingPutDtoToPassengerRating(PassengerRatingPutDto passengerRatingPutDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePassengerRatingFromPassengerRatingPatchDto(PassengerRatingPatchDto passengerRatingPatchDto,
                                                          @MappingTarget PassengerRating passengerRating);

    default void updatePassengerRatingFromPassengerRatingDto(PassengerRatingDto passengerRatingDto,
                                                             @MappingTarget PassengerRating passengerRating) {
        Float newPassengerRating = (passengerRating.getRatingValue()
                * Float.valueOf(passengerRating.getNumberOfRatings())
                + Float.valueOf(passengerRatingDto.getRatingValue()))
                / (float) (passengerRating.getNumberOfRatings() + 1);
        passengerRating.setRatingValue(newPassengerRating);
        passengerRating.setNumberOfRatings(passengerRating.getNumberOfRatings() + 1);
    }
}
