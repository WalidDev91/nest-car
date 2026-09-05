package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.userrequest.CreateRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.ReviewRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.UserRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserRequestService {

    UserRequestDTO createRequest(UUID requesterId, CreateRequestRequest request);

    List<UserRequestDTO> getMyRequests(UUID requesterId);

    List<UserRequestDTO> getVisibleRequests(UUID currentUserId);

    UserRequestDTO reviewRequest(UUID requestId, ReviewRequestRequest request);

}