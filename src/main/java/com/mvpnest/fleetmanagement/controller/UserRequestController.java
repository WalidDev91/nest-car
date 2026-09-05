package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.userrequest.CreateRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.ReviewRequestRequest;
import com.mvpnest.fleetmanagement.dto.userrequest.UserRequestDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.UserRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class UserRequestController {

    private final UserRequestService requestService;

    @PostMapping
    public UserRequestDTO create(@RequestBody CreateRequestRequest request, Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return requestService.createRequest(currentUser.getId(), request);

    }

    @GetMapping("/mine")
    public List<UserRequestDTO> getMine(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return requestService.getMyRequests(currentUser.getId());

    }

    @GetMapping("/visible")
    public List<UserRequestDTO> getVisible(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return requestService.getVisibleRequests(currentUser.getId());

    }

    @PatchMapping("/{id}/review")
    public UserRequestDTO review(@PathVariable UUID id, @RequestBody ReviewRequestRequest request) {

        return requestService.reviewRequest(id, request);

    }

}