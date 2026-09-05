package com.mvpnest.fleetmanagement.mapper;

import com.mvpnest.fleetmanagement.dto.userrequest.UserRequestDTO;
import com.mvpnest.fleetmanagement.entity.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(target = "requesterId", expression = "java(request.getRequester().getId())")
    @Mapping(target = "requesterName", expression = "java(request.getRequester().getFirstName() + \" \" + request.getRequester().getLastName())")
    UserRequestDTO toDTO(UserRequest request);

}