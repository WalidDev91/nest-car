package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "admin.id", target = "adminId")
    @Mapping(expression = "java(user.getAdmin() != null ? user.getAdmin().getFirstName() + \" \" + user.getAdmin().getLastName() : null)", target = "adminName")
    @Mapping(source = "imageUrl", target = "imageUrl")
    UserDTO toDTO(User user);

}
