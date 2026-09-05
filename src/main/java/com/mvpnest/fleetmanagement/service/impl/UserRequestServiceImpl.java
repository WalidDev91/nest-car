package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.userrequest.CreateRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.ReviewRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.UserRequestDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.UserRequest;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.mapper.UserRequestMapper;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.UserRequestRepository;
import com.mvpnest.fleetmanagement.service.UserRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {

    private final UserRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserRequestMapper requestMapper;

    @Override
    public UserRequestDTO createRequest(UUID requesterId, CreateRequestRequest request) {

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer nextRequestNumber = requestRepository.findTopByOrderByRequestNumberDesc()
                .map(r -> r.getRequestNumber() + 1)
                .orElse(1);

        UserRequest userRequest = UserRequest.builder()
                .requestNumber(nextRequestNumber)
                .type(request.getType())
                .subject(request.getSubject())
                .description(request.getDescription())
                .requester(requester)
                .build();

        return requestMapper.toDTO(requestRepository.save(userRequest));
    }

    @Override
    public List<UserRequestDTO> getMyRequests(UUID requesterId) {

        return requestRepository.findByRequesterId(requesterId).stream()
                .map(requestMapper::toDTO)
                .toList();

    }

    @Override
    public List<UserRequestDTO> getVisibleRequests(UUID currentUserId) {

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Admins and Super Admins see everything.
        if (currentUser.getRole() == RoleType.ADMIN || currentUser.getRole() == RoleType.SUPER_ADMIN) {
            return requestRepository.findAll().stream()
                    .map(requestMapper::toDTO)
                    .toList();
        }

        // Everyone else (e.g. Fleet Manager) only sees requests from people they directly supervise.
        return requestRepository.findByRequesterAdminId(currentUserId).stream()
                .map(requestMapper::toDTO)
                .toList();

    }

    @Override
    public UserRequestDTO reviewRequest(UUID requestId, ReviewRequestRequest request) {

        UserRequest userRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        userRequest.setStatus(request.getStatus());
        userRequest.setAdminResponse(request.getAdminResponse());

        return requestMapper.toDTO(requestRepository.save(userRequest));

    }

}