package com.software.modsen.passengermicroservice.mappers;

import com.software.modsen.passengermicroservice.entities.Passenger;
import com.software.modsen.passengermicroservice.entities.PassengerDto;
import com.software.modsen.passengermicroservice.entities.PassengerPatchDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerMapper {
    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);
    Passenger fromPassengerDtoToPassenger(PassengerDto passengerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePassengerFromPassengerPatchDto(PassengerPatchDto passengerPatchDto,
                                              @MappingTarget Passenger passenger);
}