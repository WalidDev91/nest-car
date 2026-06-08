package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "admin.id", target = "adminId")
    @Mapping(source = "admin.firstName", target = "adminName")
    UserDTO toDTO(User user);
}
