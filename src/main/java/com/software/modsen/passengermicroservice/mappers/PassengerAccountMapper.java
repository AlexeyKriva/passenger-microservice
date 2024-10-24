package com.software.modsen.passengermicroservice.mappers;

import com.software.modsen.passengermicroservice.entities.account.PassengerAccount;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceDownDto;
import com.software.modsen.passengermicroservice.entities.account.PassengerAccountBalanceUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerAccountMapper {
    PassengerAccountMapper INSTANCE = Mappers.getMapper(PassengerAccountMapper.class);

    PassengerAccount fromPassengerAccountIncreaseDtoToPassengerAccount(
            PassengerAccountBalanceUpDto passengerAccountBalanceUpDto);

    PassengerAccount fromPassengerAccountCancelDtoToPassengerAccount(
            PassengerAccountBalanceDownDto passengerAccountBalanceDownDto);

}