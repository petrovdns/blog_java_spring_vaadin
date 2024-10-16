package com.petrovdns.vaadin.data.mapper;

import com.petrovdns.vaadin.data.dto.UserDTO;
import com.petrovdns.vaadin.data.entity.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity mapToUser(UserDTO userDTO, @Context CycleAvoidingMappingContext context);

    List<UserDTO> mapToUserDto(List<UserEntity> userEntity, @Context CycleAvoidingMappingContext context);
}
