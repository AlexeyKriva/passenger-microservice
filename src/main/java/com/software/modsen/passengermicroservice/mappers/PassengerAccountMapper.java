package com.software.modsen.passengermicroservice.mappers;

import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountCancelDto;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountIncreaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerAccountMapper {
    PassengerAccountMapper INSTANCE = Mappers.getMapper(PassengerAccountMapper.class);

    PassengerAccount fromPassengerAccountIncreaseDtoToPassengerAccount(
            PassengerAccountIncreaseDto passengerAccountIncreaseDto);

    PassengerAccount fromPassengerAccountCancelDtoToPassengerAccount(
            PassengerAccountCancelDto passengerAccountCancelDto);

}