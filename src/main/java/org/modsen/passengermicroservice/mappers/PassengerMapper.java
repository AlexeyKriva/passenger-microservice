package org.modsen.passengermicroservice.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.modsen.passengermicroservice.entities.Passenger;
import org.modsen.passengermicroservice.entities.PassengerDto;
import org.modsen.passengermicroservice.entities.PassengerPatchDto;

@Mapper
public interface PassengerMapper {
    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);
    Passenger fromPassengerDtoToPassenger(PassengerDto passengerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePassengerFromPassengerPatchDto(PassengerPatchDto passengerPatchDto,
                                              @MappingTarget Passenger passenger);
}