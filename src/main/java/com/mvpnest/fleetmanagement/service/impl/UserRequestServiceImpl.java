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
import com.mvpnest.fleetmanagement.service.NotificationService;
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
    private final NotificationService notificationService;

    @Override
    public UserRequestDTO createRequest(UUID requesterId, CreateRequestRequest request) {

        User requester = userRepository.findById(requesterId).orElseThrow(() -> new RuntimeException("User not found"));

        Integer nextRequestNumber = requestRepository.findTopByOrderByRequestNumberDesc().map(r -> r.getRequestNumber() + 1).orElse(1);

        UserRequest userRequest = UserRequest.builder().requestNumber(nextRequestNumber).type(request.getType()).subject(request.getSubject()).description(request.getDescription()).requester(requester).build();

        UserRequest saved = requestRepository.save(userRequest);

        notificationService.notifyRequestSubmitted(saved);

        return requestMapper.toDTO(saved);
    }

    @Override
    public List<UserRequestDTO> getMyRequests(UUID requesterId) {

        return requestRepository.findByRequesterId(requesterId).stream().map(requestMapper::toDTO).toList();

    }

    @Override
    public List<UserRequestDTO> getVisibleRequests(UUID currentUserId) {

        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("User not found"));

        // Only Super Admin sees everything, system-wide (excluding their own requests — see below).
        if (currentUser.getRole() == RoleType.SUPER_ADMIN) {
            return requestRepository.findAll().stream().filter(request -> !request.getRequester().getId().equals(currentUserId)).map(requestMapper::toDTO).toList();
        }

        return requestRepository.findAll().stream().filter(request -> !request.getRequester().getId().equals(currentUserId)).filter(request -> isInSupervisionChain(currentUserId, request.getRequester())).map(requestMapper::toDTO).toList();

    }

    private boolean isInSupervisionChain(UUID supervisorId, User requester) {

        User current = requester;

        while (current != null) {

            if (current.getId().equals(supervisorId)) {
                return true;
            }

            current = current.getAdmin();

        }

        return false;

    }

    @Override
    public UserRequestDTO reviewRequest(UUID requestId, ReviewRequestRequest request) {

        UserRequest userRequest = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));

        userRequest.setStatus(request.getStatus());
        userRequest.setAdminResponse(request.getAdminResponse());

        UserRequest saved = requestRepository.save(userRequest);

        notificationService.notifyRequestReviewed(saved);

        return requestMapper.toDTO(saved);

    }

}